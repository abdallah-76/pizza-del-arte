package pizzas;

import java.util.Objects;

/**
 * Représente un ingrédient utilisé pour composer une pizza.
 * Chaque ingrédient possède un nom unique et un prix unitaire.
 *
 * @author Ghassan
 */
public class Ingredient {

  /** Nom unique de l'ingrédient. */
  private String nom;

  /** Prix de l'ingrédient en euros. */
  private double prix;

  // ******** CONSTRUCTEUR ********

  /**
   * Crée un nouvel ingrédient avec son nom et son prix.
   *
   * @param nom nom de l'ingrédient
   * @param prix prix de l'ingrédient (> 0)
   */
  public Ingredient(String nom, double prix) {
    if (nom == null || nom.isEmpty()) {
      throw new IllegalArgumentException("Le nom de l'ingrédient ne peut pas être vide");
    }
    if (prix <= 0) {
      throw new IllegalArgumentException("Le prix doit être supérieur à 0");
    }
    this.nom = nom;
    this.prix = prix;
  }

  // ******** GETTERS / SETTERS ********

  public String getNom() {
    return nom;
  }

  public double getPrix() {
    return prix;
  }

  /**
   * Modifie le prix de l'ingrédient.
   *
   * @param prix nouveau prix (> 0)
   * @return true si le prix a été modifié, false sinon
   */
  public boolean setPrix(double prix) {
    if (prix > 0) {
      this.prix = prix;
      return true;
    }
    return false;
  }

  // ******** MÉTHODES TECHNIQUES ********

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Ingredient)) {
      return false;
    }
    Ingredient other = (Ingredient) obj;
    return Objects.equals(nom, other.nom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nom);
  }

  @Override
  public String toString() {
    return nom + " (" + String.format("%.2f", prix) + " €)";
  }
}