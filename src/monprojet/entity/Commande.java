package monprojet.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Commande {
    private int id;
    private LocalDateTime dateCommande;
    private String etat; // Ex: "En cours", "Terminée", "Annulée"
    private List<LigneCommande> lignes; // Pour stocker les détails de la commande

    // Constructeurs
    public Commande() {
        this.dateCommande = LocalDateTime.now();
    }
    
    // ... (ajouter d'autres constructeurs si nécessaire)

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDateTime dateCommande) { this.dateCommande = dateCommande; }
    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
    public List<LigneCommande> getLignes() { return lignes; }
    public void setLignes(List<LigneCommande> lignes) { this.lignes = lignes; }
}