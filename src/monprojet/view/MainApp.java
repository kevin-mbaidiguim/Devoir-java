package monprojet.view;

import monprojet.entity.Categorie;
import monprojet.entity.Produit;
import monprojet.service.ICategorieService;
import monprojet.service.IProduitService;
import monprojet.service.ICommandeService;
import monprojet.service.CategorieService;
import monprojet.service.ProduitService;
import monprojet.service.CommandeService;
import monprojet.entity.Commande;
import monprojet.entity.LigneCommande;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class MainApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ICategorieService categorieService = new CategorieService();
    private static final IProduitService produitService = new ProduitService();
    private static final ICommandeService commandeService = new CommandeService();

    public static void main(String[] args) {
        System.out.println("=== Application de Gestion de Stock et Commandes ===");
        afficherMenuPrincipal();
    }

    private static void afficherMenuPrincipal() {
        int choix = -1;
        while (choix != 0) {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Responsable Stock");
            System.out.println("2. Boutiquier");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choix = -1;
            }

            switch (choix) {
                case 1:
                    menuResponsableStock();
                    break;
                case 2:
                    menuBoutiquier();
                    break;
                case 0:
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }
    
    // ===============================================
    //           MENU RESPONSABLE STOCK
    // ===============================================

    private static void menuResponsableStock() {
        int choix = -1;
        while (choix != 6) {
            System.out.println("\n--- Menu Responsable Stock ---");
            System.out.println("1. Ajouter Catégorie de Produit");
            System.out.println("2. Lister les catégories");
            System.out.println("3. Ajouter des Produits");
            System.out.println("4. Lister les Produits");
            System.out.println("5. Filtrer les Produits par Catégorie");
            System.out.println("6. Quitter (Retour au menu principal)");
            System.out.print("Votre choix : ");
            
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choix = -1;
            }

            switch (choix) {
                case 1:
                    ajouterCategorie();
                    break;
                case 2:
                    listerCategories();
                    break;
                case 3:
                    ajouterProduit();
                    break;
                case 4:
                    listerProduits(produitService.listerTousLesProduits());
                    break;
                case 5:
                    filtrerProduits();
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    // Cas d'utilisation 1 : Ajouter Catégorie de Produit
    private static void ajouterCategorie() {
        System.out.print("Nom de la catégorie à ajouter : ");
        String nom = scanner.nextLine();
        
        Categorie nouvelleCat = new Categorie();
        nouvelleCat.setNom(nom);

        if (categorieService.ajouterCategorie(nouvelleCat)) {
            System.out.println("✅ Catégorie '" + nom + "' ajoutée avec succès !");
        } else {
            System.err.println("❌ Échec de l'ajout de la catégorie.");
        }
    }

    // Cas d'utilisation 2 : Lister les catégories
    private static void listerCategories() {
        System.out.println("\n--- Liste des Catégories ---");
        List<Categorie> categories = categorieService.listerCategories();
        if (categories.isEmpty()) {
            System.out.println("Aucune catégorie trouvée.");
            return;
        }
        for (Categorie cat : categories) {
            System.out.println("ID: " + cat.getId() + ", Nom: " + cat.getNom());
        }
    }
    
    // Cas d'utilisation 3 : Ajouter des Produits
    private static void ajouterProduit() {
        System.out.println("\n--- Ajouter un Produit ---");
        
        listerCategories();
        System.out.print("ID de la Catégorie (doit exister) : ");
        int categorieId = lireEntier();
        
        System.out.print("Nom du produit : ");
        String nom = scanner.nextLine();
        
        System.out.print("Prix unitaire : ");
        double prix = lireDouble();
        
        System.out.print("Quantité en stock initiale : ");
        int stock = lireEntier();

        Produit nouveauProd = new Produit(0, nom, prix, stock, categorieId);

        if (produitService.ajouterProduit(nouveauProd)) {
            System.out.println("✅ Produit '" + nom + "' ajouté avec succès !");
        } else {
            System.err.println("❌ Échec de l'ajout du produit. Vérifiez l'ID de catégorie.");
        }
    }
    
    // Cas d'utilisation 4 : Lister les Produits (Méthode générique d'affichage)
    private static void listerProduits(List<Produit> produits) {
        if (produits.isEmpty()) {
            System.out.println("Aucun produit trouvé.");
            return;
        }
        System.out.println("\n--- Liste des Produits ---");
        System.out.printf("%-5s | %-20s | %-10s | %-10s | %-5s%n", "ID", "Nom", "Prix (€)", "Stock", "CatID");
        System.out.println("-----------------------------------------------------------------");
        for (Produit prod : produits) {
            System.out.printf("%-5d | %-20s | %-10.2f | %-10d | %-5d%n", 
                                prod.getId(), prod.getNom(), prod.getPrixUnitaire(), prod.getQuantiteStock(), prod.getCategorieId());
        }
    }

    // Cas d'utilisation 5 : Filtrer les Produits par Catégorie
    private static void filtrerProduits() {
        listerCategories();
        System.out.print("Entrez l'ID de la catégorie à filtrer : ");
        int catId = lireEntier();
        
        List<Produit> produitsFiltres = produitService.filtrerProduitsParCategorie(catId);
        listerProduits(produitsFiltres);
    }

    // ===============================================
    //              MENU BOUTIQUIER
    // ===============================================

    private static void menuBoutiquier() {
        int choix = -1;
        while (choix != 4) {
            System.out.println("\n--- Menu Boutiquier ---");
            System.out.println("1. Enregistrer une commande");
            System.out.println("2. Lister les commandes");
            System.out.println("3. Voir les Détails une Commande");
            System.out.println("4. Quitter (Retour au menu principal)");
            System.out.print("Votre choix : ");
            
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choix = -1;
            }

            switch (choix) {
                case 1:
                    enregistrerCommande();
                    break;
                case 2:
                    listerCommandes();
                    break;
                case 3:
                    voirDetailsCommande();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }
    
    // Cas d'utilisation 1 : Enregistrer une commande
    private static void enregistrerCommande() {
        Commande nouvelleCommande = new Commande();
        nouvelleCommande.setEtat("En cours"); // État initial
        
        List<LigneCommande> lignes = new ArrayList<>();
        boolean ajouterLigne = true;
        
        System.out.println("\n--- Enregistrement d'une nouvelle Commande ---");
        
        while (ajouterLigne) {
            listerProduits(produitService.listerTousLesProduits());
            System.out.print("\nID du Produit à commander (0 pour terminer) : ");
            int prodId = lireEntier();
            
            if (prodId == 0) {
                ajouterLigne = false;
                break;
            }

            Produit produitSelectionne = produitService.getProduitById(prodId);
            if (produitSelectionne == null) {
                System.err.println("❌ Produit non trouvé. Réessayez.");
                continue;
            }

            System.out.print("Quantité désirée : ");
            int qte = lireEntier();
            
            if (qte <= 0) {
                System.err.println("❌ La quantité doit être supérieure à zéro.");
                continue;
            }
            
            // Le service vérifiera le stock de manière définitive, mais c'est un bon feedback
            if (produitSelectionne.getQuantiteStock() < qte) {
                System.err.println("⚠️ Stock insuffisant ! Stock disponible: " + produitSelectionne.getQuantiteStock());
                continue;
            }
            
            // Création de la ligne de commande
            LigneCommande ligne = new LigneCommande();
            ligne.setProduitId(prodId);
            ligne.setQuantiteCommandee(qte);
            lignes.add(ligne);

            System.out.print("Ajouter un autre produit ? (o/n) : ");
            String rep = scanner.nextLine().trim().toLowerCase();
            if (!rep.equals("o")) {
                ajouterLigne = false;
            }
        }
        
        if (lignes.isEmpty()) {
            System.out.println("Commande annulée car aucune ligne n'a été ajoutée.");
            return;
        }

        nouvelleCommande.setLignes(lignes);

        if (commandeService.enregistrerCommande(nouvelleCommande)) {
            System.out.println("✅ Commande enregistrée avec succès ! (ID: " + nouvelleCommande.getId() + "). Le stock a été mis à jour.");
        } else {
            System.err.println("❌ Échec de l'enregistrement de la commande. Le stock n'a pas été modifié.");
        }
    }
    
    // Cas d'utilisation 2 : Lister les commandes
    private static void listerCommandes() {
        System.out.println("\n--- Liste des Commandes ---");
        List<Commande> commandes = commandeService.listerLesCommandes();
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande trouvée.");
            return;
        }
        System.out.printf("%-5s | %-20s | %-10s%n", "ID", "Date", "État");
        System.out.println("---------------------------------------------");
        for (Commande cmd : commandes) {
            System.out.printf("%-5d | %-20s | %-10s%n", 
                                cmd.getId(), 
                                cmd.getDateCommande().toLocalDate(),
                                cmd.getEtat());
        }
    }

    // Cas d'utilisation 3 : Voir les Détails une Commande
    private static void voirDetailsCommande() {
        System.out.print("Entrez l'ID de la commande à consulter : ");
        int id = lireEntier();
        
        Commande commande = commandeService.voirDetailsCommande(id);
        
        if (commande != null) {
            System.out.println("\n--- Détails Commande ID " + commande.getId() + " ---");
            System.out.println("Date: " + commande.getDateCommande());
            System.out.println("État: " + commande.getEtat());
            
            double totalGénéral = 0.0;
            System.out.println("Lignes de Commande:");
            
            System.out.printf("%-5s | %-20s | %-10s | %-10s%n", "ProdID", "Nom Produit", "Quantité", "Prix Ligne (€)");
            System.out.println("----------------------------------------------------------");
            
            for (LigneCommande ligne : commande.getLignes()) {
                Produit prod = produitService.getProduitById(ligne.getProduitId());
                String nomProd = (prod != null) ? prod.getNom() : "Inconnu";
                
                System.out.printf("%-5d | %-20s | %-10d | %-10.2f%n", 
                                    ligne.getProduitId(), 
                                    nomProd, 
                                    ligne.getQuantiteCommandee(), 
                                    ligne.getPrixTotalLigne());
                totalGénéral += ligne.getPrixTotalLigne();
            }
            System.out.println("----------------------------------------------------------");
            System.out.printf("TOTAL GÉNÉRAL : %.2f €%n", totalGénéral);
            
        } else {
            System.err.println("❌ Commande avec l'ID " + id + " non trouvée.");
        }
    }
    
    // ===============================================
    //              MÉTHODES UTILITAIRES
    // ===============================================

    private static int lireEntier() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.print("Saisie invalide. Veuillez entrer un nombre entier : ");
            }
        }
    }
    
    private static double lireDouble() {
        while (true) {
            try {
                String input = scanner.nextLine().replace(',', '.'); // Accepte virgule ou point
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.err.print("Saisie invalide. Veuillez entrer un nombre (ex: 12.99) : ");
            }
        }
    }
}