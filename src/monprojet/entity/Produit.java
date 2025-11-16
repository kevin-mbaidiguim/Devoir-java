package monprojet.entity;

public class Produit {
    private int id;
    private String nom;
    private double prixUnitaire;
    private int quantiteStock;
    private int categorieId; // Clé étrangère vers Categorie

    // Constructeurs
    public Produit() {}

    public Produit(int id, String nom, double prixUnitaire, int quantiteStock, int categorieId) {
        this.id = id;
        this.nom = nom;
        this.prixUnitaire = prixUnitaire;
        this.quantiteStock = quantiteStock;
        this.categorieId = categorieId;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public int getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(int quantiteStock) { this.quantiteStock = quantiteStock; }
    public int getCategorieId() { return categorieId; }
    public void setCategorieId(int categorieId) { this.categorieId = categorieId; }

    @Override
    public String toString() {
        return "Produit{" + "id=" + id + ", nom='" + nom + "', prix=" + prixUnitaire + ", stock=" + quantiteStock + ", catId=" + categorieId + '}';
    }
}
