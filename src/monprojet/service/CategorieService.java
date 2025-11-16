package monprojet.service;

import java.util.List;
import monprojet.entity.Categorie;
import monprojet.repository.CategorieRepository;

public class CategorieService implements ICategorieService {
    // Le Service utilise le Repository pour accéder à la DB.
    private CategorieRepository repository;

    // Injection de dépendance (simple dans ce contexte)
    public CategorieService() {
        this.repository = new CategorieRepository();
    }

    @Override
    public boolean ajouterCategorie(Categorie categorie) {
        // Logique métier avant l'appel à la DB (ex: validation du nom)
        if (categorie.getNom() == null || categorie.getNom().trim().isEmpty()) {
            System.err.println("Le nom de la catégorie ne peut pas être vide.");
            return false;
        }
        return repository.save(categorie);
    }

    @Override
    public List<Categorie> listerCategories() {
        return repository.findAll();
    }
}