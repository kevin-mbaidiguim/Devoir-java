package monprojet.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    // Configuration de la connexion JDBC (À MODIFIER)
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_stock_commandes";
    private static final String USER = "votre_utilisateur"; 
    private static final String PASSWORD = "votre_mot_de_passe"; 
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Retourne une nouvelle connexion à la base de données.
     * @return L'objet Connection ou null en cas d'erreur.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Chargement du driver JDBC
            Class.forName(DRIVER);
            // Établissement de la connexion
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur: Driver JDBC non trouvé.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur: Connexion à la base de données échouée.");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Ferme une connexion SQL.
     * @param connection La connexion à fermer.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion.");
                e.printStackTrace();
            }
        }
    }
}