package monprojet.service;

import java.util.List;
import monprojet.entity.Produit;
import monprojet.repository.ProduitRepository;

public class ProduitService implements IProduitService {

    private final ProduitRepository repository;

    public ProduitService() {
        this.repository = new ProduitRepository(); // Le service dépend du repository
    }

    @Override
    public boolean ajouterProduit(Produit produit) {
        // Logique de validation métier ici (ex: prix positif, stock initial >= 0, etc.)
        if (produit.getPrixUnitaire() <= 0 || produit.getQuantiteStock() < 0) {
            System.err.println("Validation échouée: Prix ou stock invalide.");
            return false;
        }
        return repository.save(produit);
    }

    @Override
    public List<Produit> listerTousLesProduits() {
        return repository.findAll();
    }

    @Override
    public List<Produit> filtrerProduitsParCategorie(int categorieId) {
        return repository.findByCategorie(categorieId);
    }
    
    @Override
    public Produit getProduitById(int id) {
        // Cette méthode doit être implémentée dans ProduitRepository
        return repository.findById(id); 
    }
}