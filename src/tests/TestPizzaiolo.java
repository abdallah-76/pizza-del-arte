package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pizzas.Commande;
import pizzas.Client;
import pizzas.InformationPersonnelle;
import pizzas.Pizza;
import pizzas.Pizzaiolo;
import pizzas.TypePizza;

/**
 * Tests unitaires de la classe Pizzaiolo.
 */
public class TestPizzaiolo {

  private Pizzaiolo pizzaiolo;
  private Pizza pizzaViande;
  private Pizza pizzaVeggie;

  @BeforeEach
  public void setUp() {
    pizzaiolo = new Pizzaiolo();
    pizzaViande = pizzaiolo.creerPizza("Reine", TypePizza.Viande);
    pizzaVeggie = pizzaiolo.creerPizza("Veggie", TypePizza.Vegetarienne);
  }

  // ─────────────────────────────────────────
  // 1. Tests création et modification d'ingrédients
  // ─────────────────────────────────────────

  @Test
  public void testCreerIngredientOk() {
    int res = pizzaiolo.creerIngredient("tomate", 1.0);
    assertEquals(0, res);
  }

  @Test
  public void testCreerIngredientNomNull() {
    int res = pizzaiolo.creerIngredient(null, 1.0);
    assertEquals(-1, res);
  }

  @Test
  public void testCreerIngredientPrixNegatif() {
    int res = pizzaiolo.creerIngredient("tomate", -2.0);
    assertEquals(-3, res);
  }

  @Test
  public void testCreerIngredientDoublon() {
    assertEquals(0, pizzaiolo.creerIngredient("tomate", 1.0));
    int res = pizzaiolo.creerIngredient("tomate", 2.0);
    assertEquals(-2, res);
  }

  @Test
  public void testChangerPrixIngredientOk() {
    pizzaiolo.creerIngredient("tomate", 1.0);
    int res = pizzaiolo.changerPrixIngredient("tomate", 2.5);
    assertEquals(0, res);
  }

  @Test
  public void testChangerPrixIngredientNomInvalide() {
    pizzaiolo.creerIngredient("tomate", 1.0);
    int res = pizzaiolo.changerPrixIngredient("", 2.0);
    assertEquals(-1, res);
  }

  @Test
  public void testChangerPrixIngredientPrixNegatif() {
    pizzaiolo.creerIngredient("tomate", 1.0);
    int res = pizzaiolo.changerPrixIngredient("tomate", -5.0);
    assertEquals(-2, res);
  }

  @Test
  public void testChangerPrixIngredientInexistant() {
    int res = pizzaiolo.changerPrixIngredient("fromage", 2.0);
    assertEquals(-3, res);
  }

  // ─────────────────────────────────────────
  // 2. Tests interdiction d'ingrédients
  // ─────────────────────────────────────────

  @Test
  public void testInterdireIngredientOk() {
    pizzaiolo.creerIngredient("jambon", 2.0);
    boolean res = pizzaiolo.interdireIngredient("jambon", TypePizza.Vegetarienne);
    assertTrue(res);
  }

  @Test
  public void testInterdireIngredientInexistant() {
    boolean res = pizzaiolo.interdireIngredient("salami", TypePizza.Viande);
    assertFalse(res);
  }

  // ─────────────────────────────────────────
  // 3. Tests création de pizzas
  // ─────────────────────────────────────────

  @Test
  public void testCreerPizzaOk() {
    Pizza p = pizzaiolo.creerPizza("Regina", TypePizza.Viande);
    assertNotNull(p);
    assertEquals("Regina", p.getNom());
  }

  @Test
  public void testCreerPizzaNomVide() {
    Pizza p = pizzaiolo.creerPizza("", TypePizza.Viande);
    assertNull(p);
  }

  @Test
  public void testCreerPizzaDoublon() {
    Pizza p1 = pizzaiolo.creerPizza("QuatreFromages", TypePizza.Vegetarienne);
    assertNotNull(p1);
    Pizza p2 = pizzaiolo.creerPizza("QuatreFromages", TypePizza.Viande);
    assertNull(p2);
  }

  // ─────────────────────────────────────────
  // 4. Tests ajout / retrait d'ingrédients dans une pizza
  // ─────────────────────────────────────────

  @Test
  public void testAjouterIngredientPizzaOk() {
    pizzaiolo.creerIngredient("tomate", 1.0);
    int res = pizzaiolo.ajouterIngredientPizza(pizzaViande, "tomate");
    assertEquals(0, res);
    assertTrue(pizzaViande.getIngredients().contains("tomate"));
  }

  @Test
  public void testAjouterIngredientPizzaPizzaInconnue() {
    pizzaiolo.creerIngredient("tomate", 1.0);
    Pizza autre = new Pizza("Test", TypePizza.Viande); // pas dans la map du pizzaiolo
    int res = pizzaiolo.ajouterIngredientPizza(autre, "tomate");
    assertEquals(-1, res);
  }

  @Test
  public void testAjouterIngredientPizzaIngredientInconnu() {
    int res = pizzaiolo.ajouterIngredientPizza(pizzaViande, "trucInexistant");
    assertEquals(-2, res);
  }

  @Test
  public void testAjouterIngredientPizzaIngredientInterdit() {
    pizzaiolo.creerIngredient("jambon", 2.0);
    pizzaiolo.interdireIngredient("jambon", TypePizza.Vegetarienne);
    int res = pizzaiolo.ajouterIngredientPizza(pizzaVeggie, "jambon");
    assertEquals(-3, res);
  }

  @Test
  public void testRetirerIngredientPizzaOk() {
    pizzaiolo.creerIngredient("tomate", 1.0);
    pizzaiolo.ajouterIngredientPizza(pizzaViande, "tomate");
    int res = pizzaiolo.retirerIngredientPizza(pizzaViande, "tomate");
    assertEquals(0, res);
    assertFalse(pizzaViande.getIngredients().contains("tomate"));
  }

  @Test
  public void testRetirerIngredientPizzaIngredientNonDansPizza() {
    pizzaiolo.creerIngredient("tomate", 1.0);
    int res = pizzaiolo.retirerIngredientPizza(pizzaViande, "tomate");
    assertEquals(-3, res);
  }

  // ─────────────────────────────────────────
  // 5. Tests vérification des ingrédients interdits
  // ─────────────────────────────────────────

  @Test
  public void testVerifierIngredientsPizzaOk() {
    pizzaiolo.creerIngredient("jambon", 2.0);
    pizzaiolo.creerIngredient("fromage", 1.0);
    pizzaiolo.interdireIngredient("jambon", TypePizza.Vegetarienne);

    pizzaiolo.ajouterIngredientPizza(pizzaVeggie, "jambon");
    pizzaiolo.ajouterIngredientPizza(pizzaVeggie, "fromage");

    Set<String> interdits = pizzaiolo.verifierIngredientsPizza(pizzaVeggie);
    assertTrue(interdits.contains("jambon"));
    assertFalse(interdits.contains("fromage"));
  }

  @Test
  public void testVerifierIngredientsPizzaNull() {
    Set<String> res = pizzaiolo.verifierIngredientsPizza(null);
    assertNull(res);
  }

  // ─────────────────────────────────────────
  // 6. Tests ajout de photo
  // ─────────────────────────────────────────

  @Test
  public void testAjouterPhotoOk() throws Exception {
    boolean res = pizzaiolo.ajouterPhoto(pizzaViande, "photo.png");
    assertTrue(res);
    assertEquals("photo.png", pizzaViande.getCheminPhoto());
  }

  @Test
  public void testAjouterPhotoPizzaInconnue() throws Exception {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    boolean res = pizzaiolo.ajouterPhoto(p, "image.png");
    assertFalse(res);
  }

  // ─────────────────────────────────────────
  // 7. Tests des prix : getPrixPizza, calculerPrixMinimalPizza, setPrixPizza
  // ─────────────────────────────────────────

  @Test
  public void testGetPrixPizzaOk() {
    pizzaViande.setPrixVente(15.0);
    assertEquals(15.0, pizzaiolo.getPrixPizza(pizzaViande));
  }

  @Test
  public void testGetPrixPizzaInconnue() {
    Pizza p = new Pizza("Test", TypePizza.Viande);
    assertEquals(-1.0, pizzaiolo.getPrixPizza(p));
  }

  @Test
  public void testCalculerPrixMinimalPizza() {
    pizzaiolo.creerIngredient("ing1", 3.0);
    pizzaiolo.creerIngredient("ing2", 2.0);
    pizzaiolo.ajouterIngredientPizza(pizzaViande, "ing1");
    pizzaiolo.ajouterIngredientPizza(pizzaViande, "ing2");

    // somme = 3 + 2 = 5 ; *1.4 = 7 ; ceil(7/10)=1 => 1*10 = 10
    double min = pizzaiolo.calculerPrixMinimalPizza(pizzaViande);
    assertEquals(10.0, min);
  }

  @Test
  public void testSetPrixPizzaTropBas() {
    pizzaiolo.creerIngredient("ing1", 3.0);
    pizzaiolo.ajouterIngredientPizza(pizzaViande, "ing1");

    double min = pizzaiolo.calculerPrixMinimalPizza(pizzaViande);
    boolean ok = pizzaiolo.setPrixPizza(pizzaViande, min - 1);
    assertFalse(ok);
  }

  @Test
  public void testSetPrixPizzaOk() {
    pizzaiolo.creerIngredient("ing1", 3.0);
    pizzaiolo.ajouterIngredientPizza(pizzaViande, "ing1");

    double min = pizzaiolo.calculerPrixMinimalPizza(pizzaViande);
    boolean ok = pizzaiolo.setPrixPizza(pizzaViande, min + 2);
    assertTrue(ok);
    assertEquals(min + 2, pizzaViande.getPrixVente());
  }

  // ─────────────────────────────────────────
  // 8. Tests sur les bénéfices
  // ─────────────────────────────────────────

  @Test
  public void testBeneficeParPizza() {
    pizzaiolo.creerIngredient("ing1", 3.0);
    pizzaiolo.ajouterIngredientPizza(pizzaViande, "ing1");

    double min = pizzaiolo.calculerPrixMinimalPizza(pizzaViande);
    pizzaiolo.setPrixPizza(pizzaViande, min + 5);

    Map<Pizza, Double> benefs = pizzaiolo.beneficeParPizza();
    assertTrue(benefs.containsKey(pizzaViande));
    assertEquals(5.0, benefs.get(pizzaViande));
  }

  @Test
  public void testBeneficeCommandes() {
    pizzaiolo.creerIngredient("ing1", 3.0);
    pizzaiolo.ajouterIngredientPizza(pizzaViande, "ing1");
    double min = pizzaiolo.calculerPrixMinimalPizza(pizzaViande);
    pizzaiolo.setPrixPizza(pizzaViande, min + 2); // benef 2 par pizza

    InformationPersonnelle infos = new InformationPersonnelle("Nom", "Prenom");
    Client client = new Client(infos, "mail@test.com", "mdp");
    Commande commande = new Commande(client);
    commande.ajouterPizza(pizzaViande, 3); // 3 pizzas

    double benef = pizzaiolo.beneficeCommandes(commande);
    assertEquals(6.0, benef);
  }

  // ─────────────────────────────────────────
  // 9. Tests sur getPizzas
  // ─────────────────────────────────────────

  @Test
  public void testGetPizzas() {
    Set<Pizza> all = pizzaiolo.getPizzas();
    assertTrue(all.contains(pizzaViande));
    assertTrue(all.contains(pizzaVeggie));
  }
}
