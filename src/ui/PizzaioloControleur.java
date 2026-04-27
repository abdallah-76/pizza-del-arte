package ui;

import java.util.List;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import pizzas.Commande;
import pizzas.InformationPersonnelle;
import pizzas.Ingredient;
import pizzas.Pizza;
import pizzas.Pizzaiolo;
import pizzas.TypePizza;
import javafx.stage.FileChooser;
import java.io.File;


/**
 * Controleur JavaFX de la fenêtre du pizzaïolo.
 *
 * @author Eric Cariou
 */
public class PizzaioloControleur {
  
  @FXML
  private ChoiceBox<String> choiceBoxTypeIngredient;

  @FXML
  private ChoiceBox<String> choiceBoxTypePizza;

  @FXML
  private ComboBox<String> comboBoxClients;

  @FXML
  private TextField entreeBeneficeClient;

  @FXML
  private TextField entreeBeneficeCommande;

  @FXML
  private TextField entreeBeneficeTotalCommandes;

  @FXML
  private TextField entreeBeneficeTotalPizza;

  @FXML
  private TextField entreeBeneficeUnitairePizza;

  @FXML
  private TextField entreeNbCommandesPizza;

  @FXML
  private TextField entreeNbPizzasClient;

  @FXML
  private TextField entreeNomIngredient;

  @FXML
  private TextField entreeNomPizza;

  @FXML
  private TextField entreeNombreTotalCommandes;

  @FXML
  private TextField entreePhotoPizza;

  @FXML
  private TextField entreePrixIngredient;

  @FXML
  private TextField entreePrixMinimalPizza;

  @FXML
  private TextField entreePrixVentePizza;

  @FXML
  private Label labelListeCommandes;

  @FXML
  private Label labelListeIngredients;

  @FXML
  private Label labelListePizzas;

  @FXML
  private ListView<String> listeCommandes;

  @FXML
  private ListView<String> listeIngredients;

  @FXML
  private ListView<String> listePizzas;
  
  
  private Pizzaiolo pizzaiolo = new Pizzaiolo();
  



  @FXML
  void actionBoutonAfficherListeTrieePizzas(ActionEvent event) {

  }

  @FXML
  void actionBoutonAfficherTousIngredients(ActionEvent event) {
    listeIngredients.getItems().clear();

    for (Ingredient i : pizzaiolo.getIngredients()) {
      listeIngredients.getItems().add(i.getNom() + " - " + i.getPrix() + " €");
    }

    labelListeIngredients.setText("Tous les ingrédients affichés");
  }



  @FXML
  void actionBoutonAfficherToutesPizzas(ActionEvent event) {
    listePizzas.getItems().clear();

    Set<Pizza> pizzas = pizzaiolo.getPizzas();

    if (pizzas.isEmpty()) {
      labelListePizzas.setText("Aucune pizza disponible ");
      return;
    }

    for (Pizza p : pizzas) {
      String nom = p.getNom() != null ? p.getNom() : "(sans nom)";
      String type = (p.getType() != null) ? p.getType().name() : "Type inconnu";
      double prix = p.getPrixVente();

      // ✅ Format d'affichage : Nom (Type) - Prix€
      String ligne = String.format("%s (%s) - %.2f€", nom, type, prix);
      listePizzas.getItems().add(ligne);
    }

    labelListePizzas.setText("Toutes les pizzas affichées ");
  }




  @FXML
  void actionBoutonAjouterIngredientPizza(ActionEvent event) {
    try {
      String selectionPizza = listePizzas.getSelectionModel().getSelectedItem();
      if (selectionPizza == null) {
        afficherPopupErreur(" Sélectionnez une pizza !");
        return;
      }

      String ingredient = entreeNomIngredient.getText().trim();
      if (ingredient.isEmpty()) {
        afficherPopupErreur(" Sélectionnez un ingrédient ou saisissez son nom !");
        return;
      }

      String nomPizza = selectionPizza.split("\\(")[0].split("-")[0].trim().toLowerCase();

      Pizza pizzaTrouvee = pizzaiolo.getPizzas().stream()
              .filter(p -> p.getNom().trim().toLowerCase().equals(nomPizza))
              .findFirst()
              .orElse(null);

      if (pizzaTrouvee == null) {
        afficherPopupErreur("Pizza introuvable : " + nomPizza);
        return;
      }

      int res = pizzaiolo.ajouterIngredientPizza(pizzaTrouvee, ingredient);
      if (res == 0) {
        afficherPopupInformation(" Ingrédient '" + ingredient 
            + "' ajouté à la pizza '" + pizzaTrouvee.getNom() + "'.");
        actionListeSelectionPizza(null); // met à jour la liste d’ingrédients
      } else {
        switch (res) {
          case -1 -> afficherPopupErreur("Erreur : pizza non reconnue !");
          case -2 -> afficherPopupErreur("Erreur : ingrédient inconnu !");
          case -3 -> afficherPopupErreur(" Ingrédient interdit pour ce type de pizza !");
          default -> afficherPopupErreur("Erreur ajout ingrédient (code " + res + ")");
        }
      }
    } catch (Exception e) {
      afficherPopupErreur("Erreur inattendue : " + e.getMessage());
    }
  }





  @FXML
  void actionBoutonCommandesDejaTraitees(ActionEvent event) {
    listeCommandes.getItems().clear();

    List<Commande> traitees = pizzaiolo.commandesDejaTraitees();
    if (traitees.isEmpty()) {
      afficherPopupInformation("Aucune commande déjà traitée.");
      return;
    }

    for (Commande c : traitees) {
      listeCommandes.getItems().add(c.toString());
    }

    labelListeCommandes.setText(" Commandes déjà traitées affichées");
  }


  @FXML
  void actionBoutonCommandesNonTraitees(ActionEvent event) {
    listeCommandes.getItems().clear();

    List<Commande> nonTraitees = pizzaiolo.commandeNonTraitees();
    if (nonTraitees.isEmpty()) {
      afficherPopupInformation("Toutes les commandes ont déjà été traitées !");
      return;
    }

    for (Commande c : nonTraitees) {
      listeCommandes.getItems().add(c.toString());
    }

    labelListeCommandes.setText(" Commandes non traitées affichées");
  }


  @FXML
  void actionBoutonCommandesTraiteesClient(ActionEvent event) {
    String clientNom = comboBoxClients.getValue();
    if (clientNom == null || clientNom.isEmpty()) {
      afficherPopupErreur("Sélectionnez un client !");
      return;
    }

    InformationPersonnelle clientTrouve = pizzaiolo.ensembleClients().stream()
            .filter(c -> (c.getNom() + " " + c.getPrenom()).equalsIgnoreCase(clientNom))
            .findFirst()
            .orElse(null);

    if (clientTrouve == null) {
      afficherPopupErreur("Client introuvable !");
      return;
    }

    List<Commande> commandesClient = pizzaiolo.commandesTraiteesClient(clientTrouve);
    listeCommandes.getItems().clear();

    if (commandesClient.isEmpty()) {
      afficherPopupInformation("Aucune commande traitée pour ce client.");
      return;
    }

    for (Commande c : commandesClient) {
      listeCommandes.getItems().add(c.toString());
    }

    labelListeCommandes.setText(" Commandes traitées de " + clientNom);
  }



  @FXML
  void actionBoutonCreerIngredient(ActionEvent event) {
    try {
      String nom = entreeNomIngredient.getText();
      double prix = Double.parseDouble(entreePrixIngredient.getText());
      int res = pizzaiolo.creerIngredient(nom, prix);
      if (res == 0) {
        afficherPopupInformation("Ingrédient créé ");
      } else if (res == 1) {
        afficherPopupInformation("Ingrédient déjà existant : prix mis à jour ");
      } else {
        afficherPopupErreur("Impossible de créer l’ingrédient (code " + res + ")");
      }

    } catch (Exception e) {
      labelListeIngredients.setText("Erreur : " + e.getMessage());
    }
  }


  @FXML
  void actionBoutonCreerPizza(ActionEvent event) {
    try {
      String nom = entreeNomPizza.getText();
      String typeStr = choiceBoxTypePizza.getValue();

      if (nom == null || nom.isEmpty()) {
        afficherPopupErreur("Veuillez saisir un nom de pizza !");
        return;
      }

      // Si le type est "Aucun", on crée une pizza sans type
      TypePizza type = null;
      if (typeStr != null && !"Aucun".equals(typeStr)) {
        type = TypePizza.valueOf(typeStr);
      }

      Pizza p = pizzaiolo.creerPizza(nom, type);

      if (p != null) {
        double prix = Double.parseDouble(entreePrixVentePizza.getText());
        pizzaiolo.setPrixPizza(p, prix);

        // Affichage amélioré : Nom (Type ou Aucun) - Prix€
        String typeAffiche = (type != null) ? type.name() : "Aucun";
        String ligne = String.format("%s (%s) - %.2f€", p.getNom(), typeAffiche, p.getPrixVente());
        listePizzas.getItems().add(ligne);

        labelListePizzas.setText("Pizza créée avec succès ");
        afficherPopupInformation("Pizza '" + nom + "' créée avec type '" 
            + typeAffiche + "' à " + prix + "€ !");
      } else {
        afficherPopupErreur("Erreur : une pizza du même nom existe déjà !");
      }

    } catch (NumberFormatException e) {
      afficherPopupErreur("Veuillez entrer un prix valide !");
    } catch (Exception e) {
      afficherPopupErreur("Erreur inattendue : " + e.getMessage());
    }
  }


  @FXML
  void actionBoutonInterdireIngredient(ActionEvent event) {
    try {
      String nom = entreeNomIngredient.getText().trim();
      String typeStr = choiceBoxTypeIngredient.getValue();

      if (nom.isEmpty()) {
        afficherPopupErreur(" Saisis le nom d’un ingrédient !");
        return;
      }

      if (typeStr == null || typeStr.equals("Aucun")) {
        afficherPopupInformation("Aucune interdiction appliquée.");
        return;
      }

      TypePizza type = TypePizza.valueOf(typeStr);
      boolean ok = pizzaiolo.interdireIngredient(nom, type);

      if (ok) {
        afficherPopupInformation(" Ingrédient '" + nom + "' interdit pour le type " + type + ".");
      } else {
        afficherPopupErreur("Ingrédient introuvable ou type invalide !");
      }

    } catch (Exception e) {
      afficherPopupErreur("Erreur : " + e.getMessage());
    }
  }



  @FXML
  void actionBoutonModifierPrixIngredient(ActionEvent event) {
    try {
      String nom = entreeNomIngredient.getText();
      double prix = Double.parseDouble(entreePrixIngredient.getText());
      int res = pizzaiolo.changerPrixIngredient(nom, prix);
      if (res == 0) {
        labelListeIngredients.setText("Prix modifié ");
      } else {
        labelListeIngredients.setText("Erreur modification (" + res + ")");
      }
    } catch (Exception e) {
      labelListeIngredients.setText("Erreur : " + e.getMessage());
    }
  }


  @FXML
  void actionBoutonModifierPrixPizza(ActionEvent event) {
    try {
      Pizza pizza = getPizzaDepuisSelectionListe();
      if (pizza == null) {
        afficherPopupErreur("Sélectionnez une pizza !");
        return;
      }

      double prix = Double.parseDouble(entreePrixVentePizza.getText());

      if (!pizzaiolo.setPrixPizza(pizza, prix)) {
        afficherPopupErreur("Prix inférieur au prix minimal !");
        afficherStatsPizza(pizza);
        return;
      }

      // ✅ Mettre à jour l'affichage dans la liste
      int idx = listePizzas.getSelectionModel().getSelectedIndex();
      String typeAffiche = (pizza.getType() != null) ? pizza.getType().name() : "Aucun";
      String ligne = String.format("%s (%s) - %.2f€", pizza.getNom(), typeAffiche, pizza.getPrixVente());
      listePizzas.getItems().set(idx, ligne);
      listePizzas.getSelectionModel().select(idx);

      afficherPopupInformation("Prix modifié avec succès ");
      afficherStatsPizza(pizza);

    } catch (NumberFormatException e) {
      afficherPopupErreur("Entrez un prix valide !");
    } catch (Exception e) {
      afficherPopupErreur("Erreur : " + e.getMessage());
    }
  }




  @FXML
  void actionBoutonParcourirPhotoPizza(ActionEvent event) {
      try {
          // Ouvre une boîte de sélection de fichier
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Choisir une photo pour la pizza");
          fileChooser.getExtensionFilters().addAll(
              new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
          );

          // Récupère le fichier choisi
          File fichier = fileChooser.showOpenDialog(null);
          if (fichier == null) {
              afficherPopupInformation("Aucun fichier sélectionné.");
              return;
          }

          // Récupère la pizza sélectionnée dans la liste
          String selection = listePizzas.getSelectionModel().getSelectedItem();
          if (selection == null) {
              afficherPopupErreur("Sélectionnez d’abord une pizza !");
              return;
          }

          String nomPizza = selection.split("\\(")[0].split("-")[0].trim().toLowerCase();
          Pizza pizza = pizzaiolo.getPizzas().stream()
              .filter(p -> p.getNom().trim().toLowerCase().equals(nomPizza))
              .findFirst()
              .orElse(null);

          if (pizza == null) {
              afficherPopupErreur("Pizza introuvable !");
              return;
          }

          // Ajoute la photo au modèle
          if (pizzaiolo.ajouterPhoto(pizza, fichier.getAbsolutePath())) {
              entreePhotoPizza.setText(fichier.getAbsolutePath());
              afficherPopupInformation(" Photo ajoutée avec succès !");
          } else {
              afficherPopupErreur("Erreur lors de l’ajout de la photo.");
          }

      } catch (Exception e) {
          afficherPopupErreur("Erreur : " + e.getMessage());
      }
  }
  
  @FXML
  void actionBoutonSupprimerIngredientPizza(ActionEvent event) {
    try {
      String selection = listePizzas.getSelectionModel().getSelectedItem();
      String ingredient = entreeNomIngredient.getText().trim();

      if (selection == null || ingredient.isEmpty()) {
        afficherPopupErreur(" Sélectionnez une pizza et un ingrédient !");
        return;
      }

      // ✅ Extraction propre du nom (avant les parenthèses et le prix)
      String nomPizza = selection.replaceAll("\\s*\\(.*\\)\\s*-.*", "").trim();

      // ✅ Recherche insensible à la casse
      Pizza pizza = pizzaiolo.getPizzas().stream()
          .filter(p -> p.getNom().equalsIgnoreCase(nomPizza))
          .findFirst()
          .orElse(null);

      if (pizza == null) {
        afficherPopupErreur("Pizza introuvable : " + nomPizza);
        return;
      }

      int res = pizzaiolo.retirerIngredientPizza(pizza, ingredient);
      if (res == 0) {
        afficherPopupInformation(" Ingrédient '" + ingredient 
            + "' retiré de la pizza " + pizza.getNom());

        // 🔄 Vide le champ et rafraîchit la liste
        entreeNomIngredient.clear();
        actionListeSelectionPizza(null);

      } else {
        switch (res) {
          case -1 -> afficherPopupErreur("Pizza non reconnue !");
          case -2 -> afficherPopupErreur("Ingrédient introuvable !");
          case -3 -> afficherPopupErreur("Cet ingrédient n’est pas dans la pizza !");
          default -> afficherPopupErreur("Erreur suppression (code " + res + ")");
        }
      }

    } catch (Exception e) {
      afficherPopupErreur("Erreur inattendue : " + e.getMessage());
    }
  }



  @FXML
  void actionBoutonVerifierValiditeIngredientsPizza(ActionEvent event) {
    String selection = listePizzas.getSelectionModel().getSelectedItem();
    if (selection == null) {
      afficherPopupErreur("Sélectionnez une pizza avant de vérifier !");
      return;
    }

    String nomPizza = extraireNomPizzaDepuisListe(selection);

    Pizza pizzaTrouvee = pizzaiolo.getPizzas().stream()
        .filter(p -> p.getNom() != null && p.getNom().equalsIgnoreCase(nomPizza))
        .findFirst()
        .orElse(null);

    if (pizzaTrouvee == null) {
      afficherPopupErreur("Pizza introuvable : " + nomPizza);
      return;
    }

    Set<String> interdits = pizzaiolo.verifierIngredientsPizza(pizzaTrouvee);

    if (interdits.isEmpty()) {
      afficherPopupInformation("Tous les ingrédients sont valides ");
    } else {
      afficherPopupErreur("Ingrédients interdits :\n- " + String.join("\n- ", interdits));
    }
  }


  @FXML
  void actionListeSelectionCommande(MouseEvent event) {

  }

  @FXML
  void actionListeSelectionIngredient(MouseEvent event) {
    String selection = listeIngredients.getSelectionModel().getSelectedItem();
    if (selection == null) {
      return;
    }

    // 🔹 Extraire juste le nom avant le tiret (ex: "tomate - 1.0 €" -> "tomate")
    String nomIngredient = selection.split("-")[0].trim();

    // 🔹 Afficher dans le champ de saisie
    entreeNomIngredient.setText(nomIngredient);

    labelListeIngredients.setText("Ingrédient sélectionné : " + nomIngredient);
  }


  @FXML
  void actionListeSelectionPizza(MouseEvent event) {
    Pizza p = getPizzaDepuisSelectionListe();
    if (p == null) {
      afficherPopupErreur("Pizza introuvable (sélection invalide)");
      afficherStatsPizza(null);
      return;
    }

    // Affiche les ingrédients
    listeIngredients.getItems().clear();
    if (p.getIngredients().isEmpty()) {
      listeIngredients.getItems().add("(Aucun ingrédient)");
    } else {
      listeIngredients.getItems().addAll(p.getIngredients());
    }

    // ✅ Remplit prix min, benef, nb commandes, benef total
    afficherStatsPizza(p);

    labelListeIngredients.setText("Ingrédients de '" + p.getNom() + "'");
  }



  private String extraireNomPizzaDepuisListe(String selection) {
    if (selection == null) return null;
    // Exemple selection: "Margherita (Viande) - 12.00€"
    // On garde tout avant " ("
    int idx = selection.indexOf(" (");
    if (idx == -1) {
      // si jamais pas de type affiché, on coupe avant " - "
      idx = selection.indexOf(" - ");
    }
    if (idx == -1) return selection.trim();
    return selection.substring(0, idx).trim();
  }


  @FXML
  void actionMenuApropos(ActionEvent event) {

  }

  @FXML
  void actionMenuCharger(ActionEvent event) {

  }

  @FXML
  void actionMenuQuitter(ActionEvent event) {

  }

  @FXML
  void actionMenuSauvegarder(ActionEvent event) {

  }

  @FXML
  void actionSelectionClient(ActionEvent event) {

  }
  private Pizza getPizzaDepuisSelectionListe() {
    String selection = listePizzas.getSelectionModel().getSelectedItem();
    if (selection == null) {
      return null;
    }

    // selection = "reine (Viande) - 20.00€"
    String nomPizza = selection.replaceAll("\\s*\\(.*\\)\\s*-.*", "").trim();

    return pizzaiolo.getPizzas().stream()
        .filter(p -> p.getNom() != null && p.getNom().equalsIgnoreCase(nomPizza))
        .findFirst()
        .orElse(null);
  }

  private void afficherStatsPizza(Pizza p) {
    if (p == null) {
      entreePrixMinimalPizza.clear();
      entreeBeneficeUnitairePizza.clear();
      entreeNbCommandesPizza.clear();
      entreeBeneficeTotalPizza.clear();
      return;
    }

    double prixMin = pizzaiolo.calculerPrixMinimalPizza(p);
    double benefU = p.getPrixVente() - prixMin;

    int nbCmd = pizzaiolo.nombrePizzasCommandees(p);
    double benefTotal = benefU * nbCmd;

    entreePrixMinimalPizza.setText(String.format("%.2f", prixMin));
    entreeBeneficeUnitairePizza.setText(String.format("%.2f", benefU));
    entreeNbCommandesPizza.setText(String.valueOf(nbCmd));
    entreeBeneficeTotalPizza.setText(String.format("%.2f", benefTotal));
  }

  
  private void afficherPopup(String message, AlertType type) {
    Alert alert = new Alert(type);
    if (type == AlertType.ERROR) {
      alert.setTitle("Erreur");
    } else {
      alert.setTitle("Information");
    }
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

  public void setPizzaiolo(Pizzaiolo p) {
    this.pizzaiolo = p;
  }

  @FXML
  void initialize() {
    labelListePizzas.setText("Interface Pizzaiolo prête ");

    choiceBoxTypeIngredient.getItems().add("Aucun");
    choiceBoxTypePizza.getItems().add("Aucun");

    for (TypePizza t : TypePizza.values()) {
      choiceBoxTypePizza.getItems().add(t.name());
      choiceBoxTypeIngredient.getItems().add(t.name());
    }

    choiceBoxTypeIngredient.setValue("Aucun");
    choiceBoxTypePizza.setValue("Aucun");

    // ✅ Chargement des clients dans le ComboBox
    comboBoxClients.getItems().clear();
    for (InformationPersonnelle c : pizzaiolo.ensembleClients()) {
      comboBoxClients.getItems().add(c.getNom() + " " + c.getPrenom());
    }
  }
}
