package pizzas;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente une commande passée par un client.
 * Une commande peut contenir plusieurs pizzas.
 *
 * @author Youssef
 */
public class Commande {

  /** États possibles d'une commande. */
  public enum EtatCommande {
    CREEE, VALIDEE, TRAITEE
  }

  /** Client ayant passé la commande. */
  private Client client;

  /** Liste des pizzas commandées. */
  private List<Pizza> pizzas;

  /** Quantités correspondantes à chaque pizza. */
  private List<Integer> quantites;

  /** État de la commande. */
  private EtatCommande etat;

  /** Date et heure de création. */
  private LocalDateTime dateHeure;

  // ************************ CONSTRUCTEUR ************************

  /**
   * Crée une nouvelle commande vide pour un client.
   *
   * @param client le client qui passe la commande
   */
  public Commande(Client client) {
    this.client = client;
    this.pizzas = new ArrayList<>();
    this.quantites = new ArrayList<>();
    this.etat = EtatCommande.CREEE;
    this.dateHeure = LocalDateTime.now();
  }

  // ************************ MÉTHODES ************************

  public Commande(String string, Pizza pizza, int i) {
    // TODO Auto-generated constructor stub
  }

  /**
   * Ajoute une pizza à la commande.
   *
   * @param pizza la pizza à ajouter
   * @param quantite la quantité commandée
   */
  public void ajouterPizza(Pizza pizza, int quantite) {
    if (pizza != null && quantite > 0) {
      pizzas.add(pizza);
      quantites.add(quantite);
    }
  }

  /**
   * Calcule le prix total de la commande.
   *
   * @return prix total en euros
   */
  public double getPrixTotal() {
    double total = 0;
    for (int i = 0; i < pizzas.size(); i++) {
      total += pizzas.get(i).getPrixVente() * quantites.get(i);
    }
    return total;
  }

  /** @return le client ayant passé la commande */
  public Client getClient() {
    return client;
  }

  /** @return la liste des pizzas commandées */
  public List<Pizza> getPizzas() {
    return pizzas;
  }

  /** @return la liste des quantités commandées */
  public List<Integer> getQuantites() {
    return quantites;
  }

  /** @return état actuel */
  public EtatCommande getEtat() {
    return etat;
  }

  /** Modifie l'état de la commande. */
  public void setEtat(EtatCommande etat) {
    this.etat = etat;
  }

  /** @return date et heure de la commande */
  public LocalDateTime getDateHeure() {
    return dateHeure;
  }

  @Override
  public int hashCode() {
    return Objects.hash(client, dateHeure);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Commande)) {
      return false;
    }
    Commande other = (Commande) obj;
    return Objects.equals(client, other.client)
        && Objects.equals(dateHeure, other.dateHeure);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Commande[")
      .append(client.getEmail())
        .append(" - ");
    for (int i = 0; i < pizzas.size(); i++) {
      sb.append(pizzas.get(i).getNom())
        .append(" x")
          .append(quantites.get(i));
      if (i < pizzas.size() - 1) {
        sb.append(", ");
      }
    }
    sb.append(" - ").append(etat)
      .append(" - ").append(String.format("%.2f", getPrixTotal())).append(" €]");
    return sb.toString();
  }
}