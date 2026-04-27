package pizzas;

/**
 * Représente une évaluation donnée par un client à une pizza.
 * Une évaluation comprend une note de 0 à 5 et un commentaire facultatif.
 *
 * @author Ghassan
 * 
 */
public class Evaluation {

  // ************************ ATTRIBUTS ************************

  /** Note donnée à la pizza (entre 0 et 5). */
  private int note;

  /** Commentaire facultatif laissé par le client. */
  private String commentaire;

  // ************************ CONSTRUCTEURS ************************

  /**
   * Crée une évaluation avec une note et sans commentaire.
   *
   * @param note note donnée (entre 0 et 5)
   */
  public Evaluation(int note) {
    this(note, "");
  }

  /**
   * Crée une évaluation complète avec une note et un commentaire.
   *
   * @param note note donnée (entre 0 et 5)
   * @param commentaire texte associé à la note
   */
  public Evaluation(int note, String commentaire) {
    if (note < 0) {
      note = 0;
    }
    if (note > 5) {
      note = 5;
    }
    this.note = note;
    this.commentaire = (commentaire != null) ? commentaire : "";
  }

  // ************************ GETTERS / SETTERS ************************

  public int getNote() { 
    return note; 
  }

  public String getCommentaire() { 
    return commentaire; 
  }
  /**
  * Modifie la note attribuée à la pizza si elle est valide.
  *
  * @param note nouvelle note (doit être comprise entre 0 et 5)
  */
  
  public void setNote(int note) {
    if (note >= 0 && note <= 5) {
      this.note = note;
    }
  }
  
  /**
  * Modifie le commentaire associé à l'évaluation.
  *
  * @param commentaire nouveau commentaire (ignoré si null)
  */
  
  public void setCommentaire(String commentaire) {
    if (commentaire != null) {
      this.commentaire = commentaire;
    }
  }

  // ************************ MÉTHODES ************************

  /**
   * Indique si l'évaluation est positive (note >= 4).
   *
   * @return true si la note est bonne, false sinon
   * 
   */
  public boolean estPositive() {
    return note >= 4;
  }

  @Override
  public String toString() {
    if (commentaire.isEmpty()) {
      return "Note : " + note + "/5";
    } else {
      return "Note : " + note + "/5 - \"" + commentaire + "\"";
    }
  }
}
