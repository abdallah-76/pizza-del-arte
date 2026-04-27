package pizzas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implémentation d'un client pour l'application PizzaDelArte.
 * Un client peut s'inscrire, se connecter, créer et gérer des commandes,
 * filtrer les pizzas et évaluer celles qu'il a déjà commandées.
 *
 * @author Youssef
 */
public class Client implements InterClient {

  // ************************ ATTRIBUTS ************************

  private InformationPersonnelle infos;
  private String email;
  private String motDePasse;
  private boolean connecte;

  /** Commandes du client (validées ou en cours). */
  private List<Commande> commandes;

  /** Ensemble global des pizzas disponibles (simulé ici). */
  private static Set<Pizza> cataloguePizzas = new HashSet<>();

  /** Filtres appliqués. */
  private TypePizza filtreType;
  private List<String> filtreIngredients;
  private double filtrePrixMax;

  // ************************ CONSTRUCTEUR ************************

  public Client(InformationPersonnelle infos, String email, String motDePasse) {
    this.infos = infos;
    this.email = email;
    this.motDePasse = motDePasse;
    this.connecte = false;
    this.commandes = new ArrayList<>();
    this.filtreIngredients = new ArrayList<>();
    this.filtrePrixMax = -1; // pas de filtre actif
  }

  // ************************ INSCRIPTION & CONNEXION ************************

  @Override
  public int inscription(String email, String mdp, InformationPersonnelle info) {
    if (email == null || mdp == null || email.isEmpty() || mdp.isEmpty()) {
      return -2; // email ou mot de passe vide
    }
    if (info == null) {
      return -3; // informations personnelles invalides
    }
    if (!email.contains("@") || !email.contains(".")) {
      return -4; // email mal formé
    }
    if (this.email.equals(email)) {
      return -1; // email déjà utilisé
    }
    this.email = email;
    this.motDePasse = mdp;
    this.infos = info;
    return 0;
  }

  @Override
  public boolean connexion(String email, String mdp) {
    if (this.email.equals(email) && this.motDePasse.equals(mdp)) {
      this.connecte = true;
      return true;
    }
    return false;
  }

  @Override
  public void deconnexion() throws NonConnecteException {
    if (!connecte) throw new NonConnecteException();
    this.connecte = false;
  }

  // ************************ COMMANDES ************************

  @Override
  public Commande debuterCommande() throws NonConnecteException {
    if (!connecte) throw new NonConnecteException();
    Commande cmd = new Commande(this);
    commandes.add(cmd);
    return cmd;
  }

  @Override
  public void ajouterPizza(Pizza pizza, int nombre, Commande cmd)
      throws NonConnecteException, CommandeException {
    if (!connecte) throw new NonConnecteException();
    if (cmd == null || !commandes.contains(cmd))
      throw new CommandeException("Commande inexistante ou non créée par ce client");
    if (cmd.getEtat() != Commande.EtatCommande.CREEE)
      throw new CommandeException("La commande n'est pas modifiable");
    if (pizza == null) throw new CommandeException("Pizza invalide");
    cmd.ajouterPizza(pizza, nombre);
  }

  @Override
  public void validerCommande(Commande cmd)
      throws NonConnecteException, CommandeException {
    if (!connecte) throw new NonConnecteException();
    if (cmd == null || !commandes.contains(cmd))
      throw new CommandeException("Commande inconnue ou non créée par ce client");
    if (cmd.getEtat() != Commande.EtatCommande.CREEE)
      throw new CommandeException("Commande déjà validée ou traitée");
    cmd.setEtat(Commande.EtatCommande.VALIDEE);
  }

  @Override
  public void annulerCommande(Commande cmd)
      throws NonConnecteException, CommandeException {
    if (!connecte) throw new NonConnecteException();
    if (cmd == null || !commandes.contains(cmd))
      throw new CommandeException("Commande inexistante");
    if (cmd.getEtat() != Commande.EtatCommande.CREEE)
      throw new CommandeException("Impossible d'annuler une commande validée");
    commandes.remove(cmd);
  }

  @Override
  public List<Commande> getCommandesEncours() throws NonConnecteException {
    if (!connecte) throw new NonConnecteException();
    List<Commande> encours = new ArrayList<>();
    for (Commande c : commandes)
      if (c.getEtat() == Commande.EtatCommande.CREEE) encours.add(c);
    return encours;
  }

  @Override
  public List<Commande> getCommandePassees() throws NonConnecteException {
    if (!connecte) throw new NonConnecteException();
    List<Commande> passees = new ArrayList<>();
    for (Commande c : commandes)
      if (c.getEtat() != Commande.EtatCommande.CREEE) passees.add(c);
    return passees;
  }

  // ************************ FILTRES ************************

  @Override
  public Set<Pizza> getPizzas() {
    return cataloguePizzas;
  }

  @Override
  public void ajouterFiltre(TypePizza type) {
    this.filtreType = type;
  }

  @Override
  public void ajouterFiltre(String... ingredients) {
    for (String ingr : ingredients) {
      if (ingr != null && !ingr.isEmpty()) {
        filtreIngredients.add(ingr);
      }
    }
  }

  @Override
  public void ajouterFiltre(double prixMaximum) {
    if (prixMaximum > 0) this.filtrePrixMax = prixMaximum;
  }

  @Override
  public Set<Pizza> selectionPizzaFiltres() {
    Set<Pizza> resultat = new HashSet<>();
    for (Pizza p : cataloguePizzas) {
      boolean ok = true;
      if (filtreType != null && p.getType() != filtreType) ok = false;
      for (String ingr : filtreIngredients)
        if (!p.getIngredients().contains(ingr)) ok = false;
      if (filtrePrixMax > 0 && p.getPrixVente() > filtrePrixMax) ok = false;
      if (ok) resultat.add(p);
    }
    return resultat;
  }

  @Override
  public void supprimerFiltres() {
    filtreType = null;
    filtreIngredients.clear();
    filtrePrixMax = -1;
  }

  // ************************ ÉVALUATIONS ************************

  @Override
  public Set<Evaluation> getEvaluationsPizza(Pizza pizza) {
    if (pizza == null) return null;
    return new HashSet<>(pizza.getEvaluations());
  }

  @Override
  public double getNoteMoyenne(Pizza pizza) {
    if (pizza == null) return -2;
    List<Evaluation> evals = pizza.getEvaluations();
    if (evals.isEmpty()) return -1;
    double somme = 0;
    for (Evaluation e : evals) somme += e.getNote();
    return somme / evals.size();
  }

  @Override
  public boolean ajouterEvaluation(Pizza pizza, int note, String commentaire)
      throws NonConnecteException, CommandeException {
    if (!connecte) throw new NonConnecteException();
    if (pizza == null) return false;
    // Vérifie si le client a déjà commandé cette pizza
    boolean dejaCommandee = false;
    for (Commande c : commandes)
      if (c.getPizzas().contains(pizza)
          && c.getEtat() == Commande.EtatCommande.VALIDEE)
        dejaCommandee = true;
    if (!dejaCommandee)
      throw new CommandeException("Le client n'a jamais commandé cette pizza");

    // Vérifie si déjà évaluée
    for (Evaluation e : pizza.getEvaluations())
      if (e.getCommentaire().equals(commentaire)) return false;

    pizza.addEvaluation(new Evaluation(note, commentaire));
    return true;
  }

  // ************************ GETTERS UTILES ************************

  public String getEmail() {
    return email;
  }

  public boolean estConnecte() {
    return connecte;
  }

  public InformationPersonnelle getInfos() {
    return infos;
  }

  public List<Commande> getCommandes() {
    return commandes;
  }

  @Override
  public String toString() {
    return "Client[" + email + ", " + commandes.size() + " commande(s)]";
  }
}