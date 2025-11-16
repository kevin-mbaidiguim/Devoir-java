package monprojet.repository;

import monprojet.db.DatabaseManager;
import monprojet.entity.Commande;
import monprojet.entity.LigneCommande;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeRepository implements IRepository<Commande, Integer> {
    
    // Cas d'utilisation 1: Enregistrer une commande
    @Override
    public boolean save(Commande commande) {
        Connection conn = DatabaseManager.getConnection();
        if (conn == null) return false;
        
        // La transaction assure que la commande et ses lignes sont sauvegardées ensemble
        try {
            conn.setAutoCommit(false); // Début de la transaction
            
            // 1. Enregistrer l'en-tête de la Commande
            String sqlCmd = "INSERT INTO COMMANDE (date_cmd, etat_cmd) VALUES (?, ?)";
            try (PreparedStatement stmtCmd = conn.prepareStatement(sqlCmd, Statement.RETURN_GENERATED_KEYS)) {
                stmtCmd.setTimestamp(1, Timestamp.valueOf(commande.getDateCommande()));
                stmtCmd.setString(2, commande.getEtat());
                stmtCmd.executeUpdate();

                try (ResultSet generatedKeys = stmtCmd.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        commande.setId(generatedKeys.getInt(1));
                    }
                }
            }
            
            // 2. Enregistrer les Lignes de Commande (détails)
            String sqlLigne = "INSERT INTO LIGNE_COMMANDE (id_cmd, id_prod, quantite_cmd, prix_total_ligne) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmtLigne = conn.prepareStatement(sqlLigne)) {
                for (LigneCommande ligne : commande.getLignes()) {
                    stmtLigne.setInt(1, commande.getId()); // Utilise l'ID généré
                    stmtLigne.setInt(2, ligne.getProduitId());
                    stmtLigne.setInt(3, ligne.getQuantiteCommandee());
                    stmtLigne.setDouble(4, ligne.getPrixTotalLigne());
                    stmtLigne.addBatch(); // Ajoute la ligne à l'exécution groupée
                }
                stmtLigne.executeBatch();
            }

            conn.commit(); // Valide la transaction
            return true;
            
        } catch (SQLException e) {
            try {
                conn.rollback(); // Annule la transaction en cas d'erreur
            } catch (SQLException ex) {
                System.err.println("Erreur lors du rollback: " + ex.getMessage());
            }
            System.err.println("Erreur SQL lors de l'enregistrement de la commande: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ignored) {}
            DatabaseManager.closeConnection(conn);
        }
    }
    
    // Cas d'utilisation 2: Lister les commandes
    @Override
    public List<Commande> findAll() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM COMMANDE ORDER BY date_cmd DESC";
        Connection conn = DatabaseManager.getConnection();
        if (conn == null) return commandes;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                commandes.add(extractCommandeFromResultSet(rs, false)); // false pour ne pas charger les lignes
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des commandes: " + e.getMessage());
        } finally {
            DatabaseManager.closeConnection(conn);
        }
        return commandes;
    }
    
    // Cas d'utilisation 3: Voir les Détails une Commande
    @Override
    public Commande findById(Integer id) {
        Commande commande = null;
        String sql = "SELECT * FROM COMMANDE WHERE id_cmd = ?";
        Connection conn = DatabaseManager.getConnection();
        if (conn == null) return null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    commande = extractCommandeFromResultSet(rs, true); // true pour charger les lignes
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération de la commande: " + e.getMessage());
        } finally {
            DatabaseManager.closeConnection(conn);
        }
        return commande;
    }
    
    // Méthode pour lire les lignes de commande
    private List<LigneCommande> findLignesByCommandeId(int commandeId, Connection conn) throws SQLException {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT * FROM LIGNE_COMMANDE WHERE id_cmd = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LigneCommande ligne = new LigneCommande();
                    ligne.setId(rs.getInt("id_ligne"));
                    ligne.setCommandeId(rs.getInt("id_cmd"));
                    ligne.setProduitId(rs.getInt("id_prod"));
                    ligne.setQuantiteCommandee(rs.getInt("quantite_cmd"));
                    ligne.setPrixTotalLigne(rs.getDouble("prix_total_ligne"));
                    lignes.add(ligne);
                }
            }
        }
        return lignes;
    }

    private Commande extractCommandeFromResultSet(ResultSet rs, boolean chargerLignes) throws SQLException {
        Commande commande = new Commande();
        commande.setId(rs.getInt("id_cmd"));
        commande.setDateCommande(rs.getTimestamp("date_cmd").toLocalDateTime());
        commande.setEtat(rs.getString("etat_cmd"));
        
        if (chargerLignes) {
            // Nécessite une nouvelle connexion si findAll appelle cette méthode. 
            // Ici, nous supposons qu'elle est appelée via findById, qui gère la connexion.
            // Pour l'exercice, nous utilisons DatabaseManager.getConnection() pour charger les lignes.
            Connection lignesConn = DatabaseManager.getConnection();
            if (lignesConn != null) {
                commande.setLignes(findLignesByCommandeId(commande.getId(), lignesConn));
                DatabaseManager.closeConnection(lignesConn);
            }
        }
        return commande;
    }

    @Override
    public boolean delete(Integer id) { return false; /* Non implémenté ici */ }
}