package monprojet.repository;

import monprojet.db.DatabaseManager;
import monprojet.entity.Categorie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Implémente l'interface IRepository pour les opérations CRUD
public class CategorieRepository implements IRepository<Categorie, Integer> {

    // Cas d'utilisation 1: Ajouter Catégorie de Produit
    @Override
    public boolean save(Categorie categorie) {
        // SQL pour insérer une nouvelle catégorie
        String sql = "INSERT INTO CATEGORIE (nom_cat) VALUES (?)";
        Connection conn = DatabaseManager.getConnection();
        if (conn == null) return false;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categorie.getNom());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Récupérer l'ID généré par la base de données
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        categorie.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'ajout de la catégorie : " + e.getMessage());
            return false;
        } finally {
            DatabaseManager.closeConnection(conn);
        }
    }

    // Cas d'utilisation 2: Lister les catégories
    @Override
    public List<Categorie> findAll() {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT id_cat, nom_cat FROM CATEGORIE ORDER BY nom_cat";
        Connection conn = DatabaseManager.getConnection();
        if (conn == null) return categories;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categorie cat = new Categorie();
                cat.setId(rs.getInt("id_cat"));
                cat.setNom(rs.getString("nom_cat"));
                categories.add(cat);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la récupération des catégories : " + e.getMessage());
        } finally {
            DatabaseManager.closeConnection(conn);
        }
        return categories;
    }
    
    // Implémentations restantes de IRepository (laissées vides ou à implémenter selon les besoins)
    @Override
    public Categorie findById(Integer id) {
        // Non utilisé pour les cas d'usage principaux, mais bonne pratique
        return null; 
    }

    @Override
    public boolean delete(Integer id) {
        // Non nécessaire pour les cas d'usage demandés
        return false;
    }
}
