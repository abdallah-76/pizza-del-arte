package ui;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pizzas.Pizzaiolo;

public final class MainInterface extends Application {

  // 🔴 INSTANCE UNIQUE PARTAGÉE
  private static final Pizzaiolo PIZZAIOLO = new Pizzaiolo();

  public static Pizzaiolo getPizzaiolo() {
    return PIZZAIOLO;
  }

  public void startFenetreClient() {
    try {
      URL url = getClass().getResource("client.fxml");
      FXMLLoader loader = new FXMLLoader(url);
      VBox root = loader.load();

      ClientControleur ctrl = loader.getController();
      ctrl.setPizzaiolo(PIZZAIOLO);

      Stage stage = new Stage();
      stage.setTitle("Commandes de pizzas");
      stage.setScene(new Scene(root, 1210, 620));
      stage.show();

    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  public void startFenetrePizzaiolo(Stage stage) {
    try {
      URL url = getClass().getResource("pizzaiolo.fxml");
      FXMLLoader loader = new FXMLLoader(url);
      VBox root = loader.load();

      PizzaioloControleur ctrl = loader.getController();
      ctrl.setPizzaiolo(PIZZAIOLO);

      stage.setTitle("Gestion des pizzas");
      stage.setScene(new Scene(root, 985, 630));
      stage.show();

    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  @Override
  public void start(Stage primaryStage) {

    // ✅ Données déjà prêtes au lancement (aucun bouton)
    JeuDeDonnees.Donnees data = JeuDeDonnees.creer();

    // ===== Fenêtre Pizzaiolo =====
    try {
      URL url = getClass().getResource("pizzaiolo.fxml");
      FXMLLoader fxmlLoader = new FXMLLoader(url);
      VBox root = (VBox) fxmlLoader.load();

      PizzaioloControleur ctrl = fxmlLoader.getController();
      ctrl.setPizzaiolo(data.pizzaiolo);

      Scene scene = new Scene(root, 985, 630);

      primaryStage.setScene(scene);
      primaryStage.setResizable(true);
      primaryStage.setTitle("Gestion des pizzas");
      primaryStage.show();

    } catch (IOException e) {
      System.err.println("Erreur au chargement de la fenêtre du pizzaïolo : " + e);
    }

    // ===== Fenêtre Client =====
    try {
      URL url = getClass().getResource("client.fxml");
      FXMLLoader fxmlLoader = new FXMLLoader(url);
      VBox root = (VBox) fxmlLoader.load();

      ClientControleur ctrl = fxmlLoader.getController();
      ctrl.setPizzaiolo(data.pizzaiolo);
      ctrl.setClient(data.client1); // client déjà prêt

      Scene scene = new Scene(root, 1210, 620);

      Stage stage = new Stage();
      stage.setResizable(true);
      stage.setTitle("Commandes de pizzas");
      stage.setScene(scene);
      stage.show();

    } catch (IOException e) {
      System.err.println("Erreur au chargement de la fenêtre du client : " + e);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
