package tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Ingredient;

/**
 * Classe de test pour la classe {@link pizzas.Ingredient}.
 *
 * @author Ghassan
 */
public class TestIngredient {

  /** Ingrédient de base pour les tests. */
  private Ingredient ingredientBase;

  @BeforeEach
  void setUp() {
    ingredientBase = new Ingredient("Tomate", 0.5);
  }

  // ******** TEST CONSTRUCTEUR ********

  @Test
  void testConstructeurValide() {
    assertEquals("Tomate", ingredientBase.getNom());
    assertEquals(0.5, ingredientBase.getPrix(), 0.001);
  }

  @Test
  void testConstructeurNomVide() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient("", 0.8);
    });
    assertTrue(e.getMessage().contains("nom"));
  }

  @Test
  void testConstructeurNomNull() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient(null, 1.0);
    });
    assertTrue(e.getMessage().toLowerCase().contains("nom"));
  }

  @Test
  void testConstructeurPrixNegatif() {
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      new Ingredient("Mozzarella", -1);
    });
    assertTrue(e.getMessage().contains("prix"));
  }

  // ******** TEST SETPRIX ********

  @Test
  void testSetPrixValide() {
    assertTrue(ingredientBase.setPrix(1.2));
    assertEquals(1.2, ingredientBase.getPrix(), 0.001);
  }

  @Test
  void testSetPrixInvalide() {
    assertFalse(ingredientBase.setPrix(0));
    assertEquals(0.5, ingredientBase.getPrix(), 0.001);
  }

  @Test
  void testSetPrixValeurElevee() {
    assertTrue(ingredientBase.setPrix(1_000_000));
    assertEquals(1_000_000, ingredientBase.getPrix(), 0.001);
  }

  // ******** TEST EQUALS / HASHCODE ********

  @Test
  void testEqualsMemeNom() {
    Ingredient autre = new Ingredient("Tomate", 1.0);
    assertEquals(ingredientBase, autre, "Deux ingrédients de même nom doivent être égaux");
  }

  @Test
  void testEqualsNomDifferent() {
    Ingredient autre = new Ingredient("Olive", 0.5);
    assertNotEquals(ingredientBase, autre);
  }

  @Test
  void testHashCode() {
    Ingredient i1 = new Ingredient("Tomate", 0.8);
    Ingredient i2 = new Ingredient("Tomate", 0.6);
    assertEquals(i1.hashCode(), i2.hashCode());
  }

  // ******** TEST TOSTRING ********

  @Test
  void testToStringFormat() {
    String texte = ingredientBase.toString();
    assertTrue(texte.contains("Tomate"));
    assertTrue(texte.contains("€"));
  }
}
