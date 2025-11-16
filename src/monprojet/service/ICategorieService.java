package monprojet.service;

import java.util.List;
import monprojet.entity.Categorie;

// Acteur Responsable Stock : 1. Ajouter Catégorie de Produit, 2. Lister les catégorie
public interface ICategorieService {
    /**
     * Ajoute une nouvelle catégorie dans le système.
     * @param categorie L'objet Categorie à ajouter.
     * @return true si l'ajout a réussi, false sinon.
     */
    boolean ajouterCategorie(Categorie categorie);

    /**
     * Récupère la liste de toutes les catégories.
     * @return La liste des objets Categorie.
     */
    List<Categorie> listerCategories();

    // Vous pouvez ajouter d'autres méthodes (ex: getCategorieById, supprimerCategorie, etc.)
}