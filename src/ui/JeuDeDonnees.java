package ui;

import pizzas.*;

public final class JeuDeDonnees {

  private JeuDeDonnees() {}

  public static Donnees creer() {

    Pizzaiolo pizzaiolo = new Pizzaiolo();

    // ===== Ingrédients =====
    pizzaiolo.creerIngredient("tomate", 1.0);
    pizzaiolo.creerIngredient("fromage", 2.0);
    pizzaiolo.creerIngredient("jambon", 3.0);
    pizzaiolo.creerIngredient("champignon", 2.5);
    pizzaiolo.creerIngredient("olive", 1.5);
    pizzaiolo.creerIngredient("pepperoni", 3.5);

    // ===== Pizzas =====
    Pizza reine = pizzaiolo.creerPizza("reine", TypePizza.Viande);
    pizzaiolo.ajouterIngredientPizza(reine, "tomate");
    pizzaiolo.ajouterIngredientPizza(reine, "fromage");
    pizzaiolo.ajouterIngredientPizza(reine, "jambon");
    pizzaiolo.ajouterIngredientPizza(reine, "champignon");
    pizzaiolo.setPrixPizza(reine, 20);

    Pizza pep = pizzaiolo.creerPizza("pepperoni", TypePizza.Viande);
    pizzaiolo.ajouterIngredientPizza(pep, "tomate");
    pizzaiolo.ajouterIngredientPizza(pep, "fromage");
    pizzaiolo.ajouterIngredientPizza(pep, "pepperoni");
    pizzaiolo.setPrixPizza(pep, 20);

    Pizza veg = pizzaiolo.creerPizza("veggie", TypePizza.Vegetarienne);
    pizzaiolo.ajouterIngredientPizza(veg, "tomate");
    pizzaiolo.ajouterIngredientPizza(veg, "fromage");
    pizzaiolo.ajouterIngredientPizza(veg, "olive");
    pizzaiolo.ajouterIngredientPizza(veg, "champignon");
    pizzaiolo.setPrixPizza(veg, 20);

    // ===== Clients =====
    InformationPersonnelle ip1 =
        new InformationPersonnelle("Abdallah", "Mamar", "7 rue ...", 23);
    Client c1 = new Client(ip1, "mamar@dj.com", "123456789");

    InformationPersonnelle ip2 =
        new InformationPersonnelle("Ghassan", "James", "1 avenue ...", 22);
    Client c2 = new Client(ip2, "ghassan@dj.com", "password");

    // ===== Commandes + évaluations =====
    c1.connexion("mamar@dj.com", "123456789");
    try {
      Commande cmd1 = c1.debuterCommande();
      c1.ajouterPizza(reine, 1, cmd1);
      c1.ajouterPizza(pep, 2, cmd1);
      c1.validerCommande(cmd1);

      // ✅ visible dans "commandes non traitées"
      pizzaiolo.ajouterCommandeNonTraitee(cmd1);

      // ✅ commande déjà traitée pour stats
      Commande cmd1b = c1.debuterCommande();
      c1.ajouterPizza(reine, 2, cmd1b);
      c1.validerCommande(cmd1b);
      pizzaiolo.ajouterCommandeTraitee(cmd1b);

      c1.ajouterEvaluation(reine, 5, "Très bonne !");
      c1.ajouterEvaluation(pep, 4, "Un peu épicée mais top");
    } catch (Exception e) {
      // ignore
    }


    c2.connexion("ghassan@dj.com", "password");
    try {
      Commande cmd2 = c2.debuterCommande();
      c2.ajouterPizza(veg, 1, cmd2);
      c2.validerCommande(cmd2);
      pizzaiolo.ajouterCommandeTraitee(cmd2);


      c2.ajouterEvaluation(veg, 5, "Parfaite");
    } catch (Exception e) {
      // ignore
    }

    return new Donnees(pizzaiolo, c1, c2);
  }

  public static final class Donnees {
    public final Pizzaiolo pizzaiolo;
    public final Client client1;
    public final Client client2;

    public Donnees(Pizzaiolo pizzaiolo, Client client1, Client client2) {
      this.pizzaiolo = pizzaiolo;
      this.client1 = client1;
      this.client2 = client2;
    }
  }
}
