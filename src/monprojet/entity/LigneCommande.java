package monprojet.entity;

public class LigneCommande {
    private int id;
    private int commandeId; // Clé étrangère vers Commande
    private int produitId;  // Clé étrangère vers Produit
    private int quantiteCommandee;
    private double prixTotalLigne; // Prix unitaire * Quantité

    // Constructeurs
    public LigneCommande() {}
    
    // ... (ajouter d'autres constructeurs si nécessaire)

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCommandeId() { return commandeId; }
    public void setCommandeId(int commandeId) { this.commandeId = commandeId; }
    public int getProduitId() { return produitId; }
    public void setProduitId(int produitId) { this.produitId = produitId; }
    public int getQuantiteCommandee() { return quantiteCommandee; }
    public void setQuantiteCommandee(int quantiteCommandee) { this.quantiteCommandee = quantiteCommandee; }
    public double getPrixTotalLigne() { return prixTotalLigne; }
    public void setPrixTotalLigne(double prixTotalLigne) { this.prixTotalLigne = prixTotalLigne; }
}