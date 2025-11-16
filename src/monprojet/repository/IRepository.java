package monprojet.repository;

import java.util.List;

/**
 * Interface de base pour les opérations CRUD.
 * @param <T> Le type de l'entité (ex: Categorie, Produit).
 * @param <ID> Le type de l'identifiant (ex: Integer).
 */
public interface IRepository<T, ID> {
    
    T findById(ID id);
    List<T> findAll();
    boolean save(T entity); // Pour l'ajout ou la mise à jour
    boolean delete(ID id);
}