package pizzas;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente une pizza disponible dans l'application.
 * Une pizza contient :
 * - un nom,
 * - un type (viande, végétarienne, régionale),
 * - des ingrédients,
 * - un prix de vente,
 * - un chemin d'image optionnel,
 * - une liste d'évaluations.
 */
public class Pizza {

  /** Nom unique de la pizza. */
  private String nom;

  /** Type de la pizza. */
  private TypePizza type;

  /** Liste des ingrédients (noms). */
  private List<String> ingredients;

  /** Prix de vente. */
  private double prixVente;

  /** Chemin vers la photo de la pizza. */
  private String cheminPhoto;

  /** Liste des évaluations laissées par les clients. */
  private List<Evaluation> evaluations;

    /**
     * Constructeur d'une pizza.
     *
     * @param unNom nom de la pizza
     * @param unType type de la pizza
     */
  public Pizza(String unNom, TypePizza unType) {
    this.nom = unNom;
    this.type = unType;
    this.ingredients = new ArrayList<>();
    this.evaluations = new ArrayList<>();
  }


  /** @return nom de la pizza */
  public String getNom() {
    return nom;
  }

  /** @return type de la pizza */
  public TypePizza getType() {
    return type;
  }

  /** @return liste des ingrédients */
  public List<String> getIngredients() {
    return ingredients;
  }

  /** @return prix de vente */
  public double getPrixVente() {
    return prixVente;
  }

  /**
   * Modifie le prix de vente.
   *
   * @param unPrix prix à définir
   */
  public void setPrixVente(double unPrix) {
    this.prixVente = unPrix;
  }

  /** @return chemin de la photo */
  public String getCheminPhoto() {
    return cheminPhoto;
  }

  /**
   * Modifie le chemin de la photo.
   *
   * @param unChemin chemin à définir
   */
  public void setCheminPhoto(String unChemin) {
    this.cheminPhoto = unChemin;
  }

  /** @return liste des évaluations */
  public List<Evaluation> getEvaluations() {
    return evaluations;
  }

  /**
   * Ajoute un ingrédient.
   *
   * @param ingr nom de l'ingrédient
   */
  public void addIngredient(String ingr) {
    ingredients.add(ingr);
  }

  /**
   * Ajoute une évaluation à la pizza.
   *
   * @param eval l'évaluation à ajouter
   */
  public void addEvaluation(Evaluation eval) {
    evaluations.add(eval);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nom);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Pizza)) {
      return false;
    }
    Pizza other = (Pizza) obj;
    return Objects.equals(nom, other.nom);
  }

  @Override
  public String toString() {
    return "Pizza[nom=" + nom
              + ", type=" + type
              + ", prix=" + prixVente + "]";
  }
  public double getNoteMoyenne() {
    if (evaluations == null || evaluations.isEmpty()) {
        return 0.0;
    }
    double somme = 0;
    for (Evaluation e : evaluations) {
        somme += e.getNote();
    }
    return somme / evaluations.size();
}
}