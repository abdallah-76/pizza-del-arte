package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pizzas.Evaluation;

/**
 * Classe de test pour la classe {@link pizzas.Evaluation}.
 *
 * @author Ghassan
 */
public class TestEvaluation {

  /** Évaluation basique pour les tests. */
  private Evaluation evalBasique;

  /** Évaluation complète pour les tests. */
  private Evaluation evalComplete;

  /**
   * Prépare des objets avant chaque test.
   */
  @BeforeEach
  void setUp() {
    evalBasique = new Evaluation(3);
    evalComplete = new Evaluation(5, "Très bonne pizza !");
  }

  /**
   * Vérifie que le constructeur avec une seule note fonctionne correctement.
   */
  @Test
  void testConstructeurNoteSeule() {
    assertEquals(3, evalBasique.getNote(), "La note doit être égale à 3");
    assertTrue(evalBasique.getCommentaire().isEmpty(), "Le commentaire doit être vide");
  }

  /**
   * Vérifie le constructeur complet (note + commentaire).
   */
  @Test
  void testConstructeurComplet() {
    assertEquals(5, evalComplete.getNote());
    assertEquals("Très bonne pizza !", evalComplete.getCommentaire());
  }

  /**
   * Vérifie que les notes invalides sont corrigées (inférieure à 0).
   */
  @Test
  void testNoteNegativeCorrigee() {
    Evaluation e = new Evaluation(-2, "erreur");
    assertEquals(0, e.getNote(), "Les notes négatives doivent être ramenées à 0");
  }

  /**
   * Vérifie que les notes trop hautes sont corrigées (supérieures à 5).
   */
  @Test
  void testNoteTropHauteCorrigee() {
    Evaluation e = new Evaluation(10, "trop haut");
    assertEquals(5, e.getNote(), "Les notes > 5 doivent être ramenées à 5");
  }

  /**
   * Vérifie le fonctionnement du setter de note.
   */
  @Test
  void testSetNoteValide() {
    evalBasique.setNote(4);
    assertEquals(4, evalBasique.getNote());
  }

  /**
   * Vérifie que setNote ignore les valeurs invalides.
   */
  @Test
  void testSetNoteInvalide() {
    evalBasique.setNote(7);
    assertEquals(3, evalBasique.getNote(), "La note ne doit pas changer si la valeur est invalide");
  }

  /**
   * Vérifie le setter de commentaire.
   */
  @Test
  void testSetCommentaire() {
    evalBasique.setCommentaire("Bonne pizza");
    assertEquals("Bonne pizza", evalBasique.getCommentaire());
  }

  /**
   * Vérifie la méthode estPositive().
   */
  @Test
  void testEstPositive() {
    assertTrue(evalComplete.estPositive(), "Une note de 5 doit être considérée positive");
    Evaluation e2 = new Evaluation(2);
    assertFalse(e2.estPositive(), "Une note de 2 ne doit pas être considérée positive");
  }

  /**
   * Vérifie le format de toString().
   */
  @Test
  void testToString() {
    String s = evalComplete.toString();
    assertTrue(s.contains("5"));
    assertTrue(s.contains("Très bonne pizza"));
  }
}
