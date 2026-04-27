package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pizzas.Pizza;
import pizzas.TypePizza;
import pizzas.Evaluation;

/**
 * Tests unitaires de la classe Pizza.
 */
public class PizzaTest {

    private Pizza pizzaReine;
    private Pizza pizzaVeggie;

    @BeforeEach
    public void setUp() {
        pizzaReine = new Pizza("Reine", TypePizza.Viande);
        pizzaVeggie = new Pizza("Veggie", TypePizza.Vegetarienne);
    }

    // ─────────────────────────────────────────────────────────────
    // 1. Tests du constructeur et des getters
    // ─────────────────────────────────────────────────────────────
    @Test
    public void testConstructeur() {
        assertEquals("Reine", pizzaReine.getNom());
        assertEquals(TypePizza.Viande, pizzaReine.getType());
        assertTrue(pizzaReine.getIngredients().isEmpty());
        assertTrue(pizzaReine.getEvaluations().isEmpty());
        assertEquals(0.0, pizzaReine.getPrixVente());
        assertNull(pizzaReine.getCheminPhoto());
    }

    // ─────────────────────────────────────────────────────────────
    // 2. Tests d’ajout d’ingrédients
    // ─────────────────────────────────────────────────────────────
    @Test
    public void testAddIngredient() {
        pizzaVeggie.addIngredient("tomate");
        pizzaVeggie.addIngredient("fromage");

        assertEquals(2, pizzaVeggie.getIngredients().size());
        assertTrue(pizzaVeggie.getIngredients().contains("tomate"));
        assertTrue(pizzaVeggie.getIngredients().contains("fromage"));
    }

    @Test
    public void testAddIngredientDuplicate() {
        pizzaVeggie.addIngredient("tomate");
        pizzaVeggie.addIngredient("tomate");

        assertEquals(2, pizzaVeggie.getIngredients().size());
    }

    // ─────────────────────────────────────────────────────────────
    // 3. Tests du prix de vente
    // ─────────────────────────────────────────────────────────────
    @Test
    public void testSetPrixVente() {
        pizzaReine.setPrixVente(12.5);
        assertEquals(12.5, pizzaReine.getPrixVente());
    }

    @Test
    public void testSetPrixZero() {
        pizzaReine.setPrixVente(0.0);
        assertEquals(0.0, pizzaReine.getPrixVente());
    }

    // ─────────────────────────────────────────────────────────────
    // 4. Tests du chemin de photo
    // ─────────────────────────────────────────────────────────────
    @Test
    public void testSetCheminPhoto() {
        pizzaReine.setCheminPhoto("image.png");
        assertEquals("image.png", pizzaReine.getCheminPhoto());
    }

    @Test
    public void testSetCheminPhotoNull() {
        pizzaReine.setCheminPhoto(null);
        assertNull(pizzaReine.getCheminPhoto());
    }

    // ─────────────────────────────────────────────────────────────
    // 5. Tests des évaluations
    // ─────────────────────────────────────────────────────────────
    @Test
    public void testAddEvaluation() {
        Evaluation eval = new Evaluation(5, "Super bonne");
        pizzaReine.addEvaluation(eval);

        assertEquals(1, pizzaReine.getEvaluations().size());
        assertEquals(eval, pizzaReine.getEvaluations().get(0));
    }

    @Test
    public void testAddDeuxEvaluations() {
        Evaluation e1 = new Evaluation(4, "Bien");
        Evaluation e2 = new Evaluation(2, "Bof");

        pizzaReine.addEvaluation(e1);
        pizzaReine.addEvaluation(e2);

        assertEquals(2, pizzaReine.getEvaluations().size());
    }

    // ─────────────────────────────────────────────────────────────
    // 6. Tests de equals() et hashCode()
    // ─────────────────────────────────────────────────────────────
    @Test
    public void testEqualsTrue() {
        Pizza p1 = new Pizza("Reine", TypePizza.Viande);
        Pizza p2 = new Pizza("Reine", TypePizza.Vegetarienne);

        assertEquals(p1, p2);
    }

    @Test
    public void testEqualsFalse() {
        Pizza p1 = new Pizza("Reine", TypePizza.Viande);
        Pizza p2 = new Pizza("Veggie", TypePizza.Viande);

        assertNotEquals(p1, p2);
    }

    @Test
    public void testHashCode() {
        Pizza p1 = new Pizza("Reine", TypePizza.Viande);
        Pizza p2 = new Pizza("Reine", TypePizza.Viande);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    // ─────────────────────────────────────────────────────────────
    // 7. Test du toString()
    // ─────────────────────────────────────────────────────────────
    @Test
    public void testToString() {
        String str = pizzaReine.toString();
        assertTrue(str.contains("Reine"));
        assertTrue(str.contains("Viande"));
    }
}