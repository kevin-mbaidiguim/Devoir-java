package monprojet.service;

import java.util.List;
import monprojet.entity.Produit;

// Cas d'utilisation: 3. Ajouter des Produits, 4. Lister les Produits, 5. Filtrer les Produits par Catégorie
public interface IProduitService {
    
    /**
     * Ajoute un nouveau produit.
     */
    boolean ajouterProduit(Produit produit);

    /**
     * Récupère la liste de tous les produits.
     */
    List<Produit> listerTousLesProduits();

    /**
     * Récupère la liste des produits filtrés par ID de catégorie.
     */
    List<Produit> filtrerProduitsParCategorie(int categorieId);
    
    /**
     * Récupère un produit par son ID.
     */
    Produit getProduitById(int id);
}