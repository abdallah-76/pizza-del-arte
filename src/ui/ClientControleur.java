package ui;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import pizzas.Client;
import pizzas.Commande;
import pizzas.Evaluation;
import pizzas.InformationPersonnelle;
import pizzas.NonConnecteException;
import pizzas.Pizza;
import pizzas.Pizzaiolo;
import pizzas.TypePizza;

/**
 * Controleur JavaFX de la fenêtre du client.
 *
 * @author Eric Cariou
 */

public class ClientControleur {
  
  @FXML
  private ChoiceBox<String> choiceBoxFiltreType;
  
  @FXML
  private TextField entreeAdresseClient;
  
  @FXML
  private TextField entreeAgeClient;
  
  @FXML
  private TextField entreeAuteurEvaluation;
  
  @FXML
  private TextField entreeEmailClient;
  
  @FXML
  private TextField entreeEvaluationMoyenneEvaluations;
  
  @FXML
  private TextField entreeFiltreContientIngredient;
  
  @FXML
  private TextField entreeFiltrePrixMax;
  
  @FXML
  private TextField entreeMotDePasseClient;
  
  @FXML
  private TextField entreeNomClient;
  
  @FXML
  private TextField entreeNomPizza;
  
  @FXML
  private TextField entreeNomPizzaEvaluee;
  
  @FXML
  private TextField entreeNoteMoyennePizza;
  
  @FXML
  private TextField entreePrenomClient;
  
  @FXML
  private TextField entreePrixPizza;
  
  @FXML
  private TextField entreeTypePizza;
  
  @FXML
  private Label labelListeCommandes;
  
  @FXML
  private Label labelListePizzas;
  
  @FXML
  private ListView<String> listeCommandes;
  
  @FXML
  private ListView<String> listeEvaluations;
  
  @FXML
  private ListView<String> listeIngredients;
  
  @FXML
  private ChoiceBox<Integer> choiceBoxNoteEvaluation;
  
  @FXML
  private ListView<String> listePizzas;
  
  @FXML
  private StackPane panePhotoPizza;
  
  @FXML
  private TextArea texteCommentaireEvaluation;
  
  // ================= ATTRIBUTS LOGIQUES =================//
  
  private Client client;  // modèle associé au client connecté
  private Pizzaiolo pizzaiolo; // accès à la liste des pizzas disponibles
  
  @FXML
  void actionBoutonAfficherCommandesEnCours(ActionEvent event) {
    if (client == null || !client.estConnecte()) {
      afficherPopupErreur("Connectez-vous d'abord !");
      return;
    }

    listeCommandes.getItems().clear();
    for (Commande c : client.getCommandes()) {
      if (c.getEtat() == Commande.EtatCommande.CREEE) {
        listeCommandes.getItems().add(c.toString());
      }
    }
  
    if (listeCommandes.getItems().isEmpty()) {
      afficherPopupInformation("Aucune commande en cours.");
    } else {
      afficherPopupInformation("Commandes en cours affichées.");
    }
  }
  
  @FXML
  void actionBoutonAfficherCommandesTtraitees(ActionEvent event) {
    if (client == null || !client.estConnecte()) {
      afficherPopupErreur("Connectez-vous d'abord !");
      return;
    }

    listeCommandes.getItems().clear();
    for (Commande c : client.getCommandes()) {
      if (c.getEtat() == Commande.EtatCommande.TRAITEE) {
        listeCommandes.getItems().add(c.toString());
      }
    }

    if (listeCommandes.getItems().isEmpty()) {
      afficherPopupInformation("Aucune commande traitée.");
    } else {
      afficherPopupInformation("Commandes traitées affichées.");
    }
  }
  
  @FXML
  void actionBoutonAfficherEvaluationPizzas(ActionEvent event) {
    String selection = listePizzas.getSelectionModel().getSelectedItem();
    if (selection == null) {
      afficherPopupErreur("Sélectionnez une pizza !");
      return;
    }

    String nomPizza = selection.split(" - ")[0];
    Pizza pizzaChoisie = pizzaiolo.getPizzas().stream()
            .filter(p -> p.getNom().equals(nomPizza))
            .findFirst().orElse(null);

    if (pizzaChoisie == null) {
      afficherPopupErreur("Pizza introuvable !");
      return;
    }

    listeEvaluations.getItems().clear();
    for (Evaluation e : pizzaChoisie.getEvaluations()) {
      listeEvaluations.getItems().add(e.toString());
    }

    entreeNomPizzaEvaluee.setText(pizzaChoisie.getNom());
    entreeEvaluationMoyenneEvaluations.setText(String.format("%.2f",
        pizzaChoisie.getNoteMoyenne()));
    afficherPopupInformation("Évaluations affichées pour " + pizzaChoisie.getNom());
  }
  
  @FXML
  void actionBoutonAfficherToutesPizzas(ActionEvent event) {
    if (pizzaiolo == null) {
      pizzaiolo = new Pizzaiolo();
    }

    listePizzas.getItems().clear();
    for (Pizza p : pizzaiolo.getPizzas()) {
      listePizzas.getItems().add(p.getNom() + " - " + p.getPrixVente() + "€");
    }
    labelListePizzas.setText("Toutes les pizzas affichées ");
  }
  
  @FXML
  void actionBoutonAjouterMonEvaluation(ActionEvent event) {
    try {
      if (client == null || !client.estConnecte()) {
        labelListePizzas.setText(" Connectez-vous d'abord !");
        return;
      }

      String selection = listePizzas.getSelectionModel().getSelectedItem();
      if (selection == null) {
        labelListePizzas.setText(" Sélectionnez une pizza à évaluer !");
        return;
      }

      String nomPizza = selection.split(" - ")[0];
      Pizza pizzaChoisie = pizzaiolo.getPizzas().stream()
              .filter(p -> p.getNom().equals(nomPizza))
              .findFirst().orElse(null);

      if (pizzaChoisie == null) {
        labelListePizzas.setText(" Pizza introuvable !");
        return;
      }

      int note = choiceBoxNoteEvaluation.getValue();
      String commentaire = texteCommentaireEvaluation.getText();

      boolean ok = client.ajouterEvaluation(pizzaChoisie, note, commentaire);
      if (ok) {
        labelListePizzas.setText("Évaluation ajoutée ");
      } else {
        labelListePizzas.setText(" Impossible d’ajouter l’évaluation");
      }
    } catch (Exception e) {
      labelListePizzas.setText("Erreur évaluation : " + e.getMessage());
    }
  }
  
  @FXML
  void actionBoutonAjouterPizzaSelectionneeCommande(ActionEvent event) {
    try {
      if (client == null || !client.estConnecte()) {
        afficherPopupErreur(" Connectez-vous d'abord !");
        return;
      }

      String selectionPizza = listePizzas.getSelectionModel().getSelectedItem();
      if (selectionPizza == null) {
        afficherPopupErreur(" Sélectionnez une pizza !");
        return;
      }

      String nomPizza = selectionPizza.split(" - ")[0].trim();
      Pizza pizzaChoisie = pizzaiolo.getPizzas().stream()
              .filter(p -> p.getNom().equalsIgnoreCase(nomPizza))
              .findFirst()
              .orElse(null);

      if (pizzaChoisie == null) {
        afficherPopupErreur(" Pizza introuvable !");
        return;
      }

      // ✅ Vérifie si une commande est sélectionnée dans la liste
      String selectionCommande = listeCommandes.getSelectionModel().getSelectedItem();
      Commande commandeCible = null;

      if (selectionCommande != null) {
        // On essaie de retrouver la commande correspondante 
        // (par id ou par égalité d’objet)
        commandeCible = client.getCommandes().stream()
                .filter(c -> selectionCommande.equals(c.toString()))
                .findFirst()
                .orElse(null);
      }

      // ✅ Si aucune commande sélectionnée, prend la dernière
      if (commandeCible == null) {
        List<Commande> commandes = client.getCommandes();
        if (commandes.isEmpty()) {
          afficherPopupErreur(" Aucune commande en cours !");
          return;
        }
        commandeCible = commandes.get(commandes.size() - 1);
      }

      // Ajout de la pizza
      commandeCible.ajouterPizza(pizzaChoisie, 1);

      // 🔄 Rafraîchit la liste des commandes (pour afficher la mise à jour)
      listeCommandes.getItems().clear();
      for (Commande c : client.getCommandes()) {
        listeCommandes.getItems().add(c.toString());
      }

      afficherPopupInformation(" " + pizzaChoisie.getNom() 
          + " ajoutée à la commande sélectionnée !");
    } catch (Exception e) {
      afficherPopupErreur("Erreur lors de l’ajout : " + e.getMessage());
    }
  }

  
  @FXML
  void actionBoutonAppliquerFiltreContientngredient(ActionEvent event) {
    String ingr = entreeFiltreContientIngredient.getText().toLowerCase();
    listePizzas.getItems().clear();
    for (Pizza p : pizzaiolo.getPizzas()) {
      boolean contient = p.getIngredients().stream()
              .anyMatch(i -> i.toLowerCase().contains(ingr)); // i est déjà une String
      if (contient) {
        listePizzas.getItems().add(p.getNom() + " - " + p.getPrixVente() + "€");
      }
    }
    afficherPopupInformation("Filtre appliqué sur l’ingrédient : " + ingr);
  }
  
  @FXML
  void actionBoutonAppliquerFiltrePrixMax(ActionEvent event) {
    try {
      double max = Double.parseDouble(entreeFiltrePrixMax.getText());
      listePizzas.getItems().clear();
      for (Pizza p : pizzaiolo.getPizzas()) {
        if (p.getPrixVente() <= max) {
          listePizzas.getItems().add(p.getNom() + " - " + p.getPrixVente() + "€");
        }
      }
      afficherPopupInformation("Filtre prix max appliqué : " + max + "€");
    } catch (NumberFormatException e) {
      afficherPopupErreur("Veuillez entrer un prix valide.");
    }
  }
  
  @FXML
  void actionBoutonAppliquerFiltreType(ActionEvent event) {
    String typeChoisi = choiceBoxFiltreType.getValue();
    listePizzas.getItems().clear();

    if (typeChoisi == null || typeChoisi.isEmpty()) {
      afficherPopupErreur("Sélectionnez un type de pizza !");
      return;
    }

    for (Pizza p : pizzaiolo.getPizzas()) {
      // Si la pizza n’a pas de type (null)
      if (p.getType() == null) {
        if (typeChoisi.equalsIgnoreCase("Aucun")) {
          listePizzas.getItems().add(p.getNom() + " - " + p.getPrixVente() + "€");
        }
        continue; // passe à la suivante
      }

      // Comparaison du type sélectionné
      if (p.getType().toString().equalsIgnoreCase(typeChoisi)) {
        listePizzas.getItems().add(p.getNom() + " - " + p.getPrixVente() + "€");
      }
    }

    if (listePizzas.getItems().isEmpty()) {
      afficherPopupInformation("Aucune pizza du type '" + typeChoisi + "'.");
    } else {
      afficherPopupInformation("Filtre appliqué : " + typeChoisi);
    }
  }

  
  @FXML
  void actionBoutonConnexion(ActionEvent event) {
    try {
      String email = entreeEmailClient.getText();
      String mdp = entreeMotDePasseClient.getText();

      if (client == null) {
        InformationPersonnelle info = new InformationPersonnelle(
                entreeNomClient.getText(), entreePrenomClient.getText(),
                entreeAdresseClient.getText(), Integer.parseInt(entreeAgeClient.getText()));
        client = new Client(info, email, mdp);
      }

      if (client.connexion(email, mdp)) {
        afficherPopupInformation("Connexion réussie !");
        entreeAuteurEvaluation.setText(client.getInfos().getNom() 
            + " " + client.getInfos().getPrenom());
      } else {
        afficherPopupErreur("Échec de la connexion.");
      }
    } catch (Exception e) {
      afficherPopupErreur("Erreur : " + e.getMessage());
    }
  }
  
  @FXML
  void actionBoutonCreerNouvelleCommande(ActionEvent event) {
    try {
      if (client != null && client.estConnecte()) {
        Commande cmd = client.debuterCommande();
        pizzaiolo.ajouterCommande(cmd);
        listeCommandes.getItems().add(cmd.toString());
        afficherPopupInformation("Nouvelle commande créée !");
      } else {
        afficherPopupErreur("Connectez-vous d'abord !");
      }
    } catch (Exception e) {
      afficherPopupErreur("Erreur commande : " + e.getMessage());
    }
  }
  
  @FXML
  void actionBoutonDeconnexion(ActionEvent event) {
    try {
      if (client != null) {
        client.deconnexion();
        afficherPopupInformation("Déconnexion réussie !");
      } else {
        afficherPopupErreur("Aucun client connecté !");
      }
    } catch (NonConnecteException e) {
      afficherPopupErreur("Aucun client connecté !");
    }
  }
  
  @FXML
  void actionBoutonInscription(ActionEvent event) {
    try {
      InformationPersonnelle info = new InformationPersonnelle(
              entreeNomClient.getText(), entreePrenomClient.getText(),
              entreeAdresseClient.getText(), Integer.parseInt(entreeAgeClient.getText()));
      client = new Client(info, entreeEmailClient.getText(), entreeMotDePasseClient.getText());
      afficherPopupInformation("Compte client créé !");
    } catch (Exception e) {
      afficherPopupErreur("Erreur inscription : " + e.getMessage());
    }
  }
  
  @FXML
  void actionBoutonReinitialiserFiltre(ActionEvent event) {
    entreeFiltreContientIngredient.clear();
    entreeFiltrePrixMax.clear();
    choiceBoxFiltreType.setValue(null);
    actionBoutonAfficherToutesPizzas(event);
    afficherPopupInformation("Filtres réinitialisés.");
  }
  
  @FXML
  void actionBoutonValiderCommandeEnCours(ActionEvent event) {
    try {
      if (client == null || !client.estConnecte()) {
        afficherPopupErreur("Connectez-vous d'abord !");
        return;
      }

      String selection = listeCommandes.getSelectionModel().getSelectedItem();
      if (selection == null) {
        afficherPopupErreur("Sélectionnez une commande à valider !");
        return;
      }

      // 🔹 Retrouver la commande correspondante dans la liste du client
      Commande commandeSelectionnee = client.getCommandes().stream()
              .filter(c -> selection.equals(c.toString()))
              .findFirst()
              .orElse(null);

      if (commandeSelectionnee == null) {
        afficherPopupErreur("Commande introuvable !");
        return;
      }

      // 🔹 Valider la commande sélectionnée
      commandeSelectionnee.setEtat(Commande.EtatCommande.VALIDEE);
      afficherPopupInformation(" Commande validée avec succès !");

      // Rafraîchir l'affichage
      listeCommandes.refresh();

    } catch (Exception e) {
      afficherPopupErreur("Erreur validation : " + e.getMessage());
    }
  }

  
  @FXML
  void actionSelectionCommnade(MouseEvent event) {
    String selection = listeCommandes.getSelectionModel().getSelectedItem();
    if (selection != null) {
      labelListeCommandes.setText("Commande sélectionnée : " + selection);
    }
  }

  
  @FXML
  void actionSelectionEvaluation(MouseEvent event) {
    String selection = listeEvaluations.getSelectionModel().getSelectedItem();
    if (selection == null) {
      return;
    }

    // 🔹 Exemple d’affichage simplifié (selon ton format d’Evaluation.toString())
    labelListePizzas.setText("Évaluation sélectionnée : " + selection);

    // Si tu veux aller plus loin :
    String auteur = selection.split("-")[0].trim(); // si ton toString a "Auteur - Note ..."
    entreeAuteurEvaluation.setText(auteur);
  }

  
  @FXML
  void actionSelectionPizza(MouseEvent event) {
    String selection = listePizzas.getSelectionModel().getSelectedItem();
    if (selection == null) {
      return;
    }

    // 🔹 Extraire le nom avant le tiret
    String nomPizza = selection.split(" - ")[0].trim();

    // 🔹 Retrouver la pizza dans le modèle
    Pizza pizzaChoisie = pizzaiolo.getPizzas().stream()
            .filter(p -> p.getNom().equalsIgnoreCase(nomPizza))
            .findFirst()
            .orElse(null);

    if (pizzaChoisie == null) {
      afficherPopupErreur("Pizza introuvable !");
      return;
    }

    // 🔹 Afficher les infos de base
    entreeNomPizza.setText(pizzaChoisie.getNom());
    entreePrixPizza.setText(String.format("%.2f", pizzaChoisie.getPrixVente()));
    entreeTypePizza.setText(
        (pizzaChoisie.getType() != null) ? pizzaChoisie.getType().toString() : "Aucun"
    );
    entreeNoteMoyennePizza.setText(String.format("%.2f", pizzaChoisie.getNoteMoyenne()));

    // ✅ NOUVEAU : afficher les ingrédients
    listeIngredients.getItems().clear();
    if (pizzaChoisie.getIngredients().isEmpty()) {
      listeIngredients.getItems().add("(Aucun ingrédient)");
    } else {
      for (String ingr : pizzaChoisie.getIngredients()) {
        listeIngredients.getItems().add(ingr);
      }
    }

    labelListePizzas.setText("Pizza sélectionnée : " + pizzaChoisie.getNom());
  }

  
  public void setPizzaiolo(Pizzaiolo p) {
    this.pizzaiolo = p;
  }
  
  @FXML
  void initialize() {
    // Message d'accueil
    labelListePizzas.setText("Bienvenue dans PizzaDelArte ");

    // Menu déroulant des types de pizzas
    choiceBoxFiltreType.getItems().clear();
    choiceBoxFiltreType.getItems().add("Aucun"); // ✅ pour pizzas sans type
    for (TypePizza t : TypePizza.values()) {
      choiceBoxFiltreType.getItems().add(t.name());
    }

    // Notes possibles pour les évaluations
    choiceBoxNoteEvaluation.getItems().addAll(1, 2, 3, 4, 5);

    // Le champ auteur d’évaluation n’est pas modifiable par le client
    entreeAuteurEvaluation.setEditable(false);
  }

  
  // ================= POPUPS =================
  private void afficherPopup(String message, AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(type == AlertType.ERROR ? "Erreur" : "Information");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.setResizable(true);
    alert.showAndWait();
  }

  private void afficherPopupErreur(String message) {
    afficherPopup(message, AlertType.ERROR);
  }

  private void afficherPopupInformation(String message) {
    afficherPopup(message, AlertType.INFORMATION);
  }

  public void setClient(Client c) {
    this.client = c;
  }

  
}
