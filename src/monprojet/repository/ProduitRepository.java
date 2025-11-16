package monprojet.repository;

import monprojet.db.DatabaseManager;
import monprojet.entity.Produit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitRepository implements IRepository<Produit, Integer> {

    // Méthode unifiée pour l'ajout et la mise à jour (nécessaire pour la gestion du stock)
    @Override
    public boolean save(Produit produit) {
        // Si l'ID est > 0, on met à jour (UPDATE); sinon, on insère (INSERT).
        if (produit.getId() > 0) {
            return update(produit);
        } else {
            return insert(produit);
        }
    }
    
    // Logique d'insertion (Ajouter des Produits - Cas d'usage 3)
    private boolean insert(Produit produit) {
        String sql = "INSERT INTO PRODUIT (nom_prod, prix_unit, qte_stock, id_cat) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseManager.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produit.getNom());
            stmt.setDouble(2, produit.getPrixUnitaire());
            stmt.setInt(3, produit.getQuantiteStock());
            stmt.setInt(4, produit.getCategorieId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        produit.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'INSERT du produit : " + e.getMessage());
            return false;
        } finally {
            DatabaseManager.closeConnection(conn);
        }
    }
    
    // Logique de mise à jour (Utilisée par CommandeService pour décrémenter le stock)
    private boolean update(Produit produit) {
        String sql = "UPDATE PRODUIT SET nom_prod = ?, prix_unit = ?, qte_stock = ?, id_cat = ? WHERE id_prod = ?";
        Connection conn = DatabaseManager.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produit.getNom());
            stmt.setDouble(2, produit.getPrixUnitaire());
            stmt.setInt(3, produit.getQuantiteStock());
            stmt.setInt(4, produit.getCategorieId());
            stmt.setInt(5, produit.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'UPDATE du produit : " + e.getMessage());
            return false;
        } finally {
            DatabaseManager.closeConnection(conn);
        }
    }

    // Récupération par ID (Utilisé par CommandeService pour vérifier le stock)
    @Override
    public Produit findById(Integer id) {
        Produit produit = null;
        String sql = "SELECT * FROM PRODUIT WHERE id_prod = ?";
        Connection conn = DatabaseManager.getConnection();
        if (conn == null) return null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    produit = extractProduitFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la recherche du produit par ID : " + e.getMessage());
        } finally {
            DatabaseManager.closeConnection(conn);
        }
        return produit;
    }

    // Cas d'utilisation 4: Lister les Produits
    @Override
    public List<Produit> findAll() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM PRODUIT";
        Connection conn = DatabaseManager.getConnection();
        if (conn == null) return produits;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produits.add(extractProduitFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération de tous les produits : " + e.getMessage());
        } finally {
            DatabaseManager.closeConnection(conn);
        }
        return produits;
    }
    
    // Cas d'utilisation 5: Filtrer les Produits par Catégorie
    public List<Produit> findByCategorie(int categorieId) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM PRODUIT WHERE id_cat = ?";
        Connection conn = DatabaseManager.getConnection();
        if (conn == null) return produits;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categorieId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produits.add(extractProduitFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors du filtrage des produits : " + e.getMessage());
        } finally {
            DatabaseManager.closeConnection(conn);
        }
        return produits;
    }

    // Méthode utilitaire
    private Produit extractProduitFromResultSet(ResultSet rs) throws SQLException {
        Produit produit = new Produit();
        produit.setId(rs.getInt("id_prod"));
        produit.setNom(rs.getString("nom_prod"));
        produit.setPrixUnitaire(rs.getDouble("prix_unit"));
        produit.setQuantiteStock(rs.getInt("qte_stock"));
        produit.setCategorieId(rs.getInt("id_cat"));
        return produit;
    }

    @Override
    public boolean delete(Integer id) { return false; /* Non implémenté ici */ }
}