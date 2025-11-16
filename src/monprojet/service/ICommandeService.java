package monprojet.service;

import java.util.List;
import monprojet.entity.Commande;
import monprojet.entity.LigneCommande;

// Cas d'utilisation: 1. Enregistrer une commande, 2. Lister les commandes, 3. Voir les Détails une Commande
public interface ICommandeService {
    
    /**
     * Enregistre une nouvelle commande dans la DB.
     * @param commande L'objet Commande contenant les lignes.
     * @return true si l'enregistrement est réussi.
     */
    boolean enregistrerCommande(Commande commande);

    /**
     * Liste toutes les commandes enregistrées.
     * @return La liste des objets Commande (sans les détails de lignes au complet).
     */
    List<Commande> listerLesCommandes();

    /**
     * Récupère une commande spécifique avec tous ses détails de lignes.
     * @param id L'ID de la commande.
     * @return L'objet Commande complet.
     */
    Commande voirDetailsCommande(int id);
}