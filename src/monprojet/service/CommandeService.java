package monprojet.service;

import java.util.List;
import monprojet.entity.Commande;
import monprojet.entity.LigneCommande;
import monprojet.repository.CommandeRepository;
import monprojet.entity.Produit;

public class CommandeService implements ICommandeService {

    private final CommandeRepository repository;
    private final ProduitService produitService; // Nécessaire pour mettre à jour le stock

    public CommandeService() {
        this.repository = new CommandeRepository();
        this.produitService = new ProduitService(); 
    }

    @Override
    public boolean enregistrerCommande(Commande commande) {
        // Logique métier : validation et mise à jour du stock
        
        // 1. Calculer les totaux de lignes et vérifier le stock
        for (LigneCommande ligne : commande.getLignes()) {
            // Récupérer le produit pour avoir le prix actuel et le stock
            Produit produit = produitService.getProduitById(ligne.getProduitId());
            
            if (produit == null || ligne.getQuantiteCommandee() <= 0) {
                System.err.println("Commande invalide: Produit non trouvé ou quantité <= 0.");
                return false;
            }
            
            if (produit.getQuantiteStock() < ligne.getQuantiteCommandee()) {
                System.err.println("Stock insuffisant pour le produit: " + produit.getNom());
                return false;
            }
            
            // Calculer le prix total de la ligne
            double prixTotal = produit.getPrixUnitaire() * ligne.getQuantiteCommandee();
            ligne.setPrixTotalLigne(prixTotal);
        }
        
        // 2. Enregistrer la commande (Repository gère la transaction)
        boolean success = repository.save(commande);
        
        // 3. Si l'enregistrement est réussi, décrémenter le stock
        if (success) {
            for (LigneCommande ligne : commande.getLignes()) {
                Produit produit = produitService.getProduitById(ligne.getProduitId());
                produit.setQuantiteStock(produit.getQuantiteStock() - ligne.getQuantiteCommandee());
                // Mettre à jour le stock dans la DB (implémentation de save dans ProduitRepository doit gérer la mise à jour)
                produitService.ajouterProduit(produit); 
            }
        }
        
        return success;
    }

    @Override
    public List<Commande> listerLesCommandes() {
        return repository.findAll();
    }

    @Override
    public Commande voirDetailsCommande(int id) {
        return repository.findById(id);
    }
}