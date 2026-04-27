package pizzas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Représente le pizzaïolo : il gère les ingrédients, les pizzas,
 * et peut calculer les bénéfices et statistiques de la pizzeria.
 *
 * @author abdallah
 */
public class Pizzaiolo implements InterPizzaiolo {

  // ******** ATTRIBUTS ********

  /** Ingrédients disponibles (nom → ingrédient). */
  private Map<String, Ingredient> ingredients;

  /** Pizzas disponibles à la vente (nom → pizza). */
  private Map<String, Pizza> pizzas;

  /** Ingrédients interdits par type de pizza. */
  private Map<TypePizza, Set<String>> ingredientsInterdits;
  
  /*Permet de connaître les types interdits pour chaque ingrédient*/
  private Map<String, Set<TypePizza>> restrictions = new HashMap<>();


  /** Liste des commandes traitées et non traitées. */
  private List<Commande> commandesTraitees;
  private List<Commande> commandesNonTraitees;

  /** Ensemble des clients. */
  private Set<InformationPersonnelle> clients;

  // ******** CONSTRUCTEUR ********
  /**
   * Constructeur par défaut du pizzaïolo.
   * Initialise les listes et ensembles : ingrédients, pizzas, commandes et clients.
   * Les structures de données sont vides au démarrage.
   */
  public Pizzaiolo() {
    this.ingredients = new HashMap<>();
    this.pizzas = new HashMap<>();
    this.ingredientsInterdits = new EnumMap<>(TypePizza.class);
    for (TypePizza type : TypePizza.values()) {
      ingredientsInterdits.put(type, new HashSet<>());
    }
    this.commandesTraitees = new ArrayList<>();
    this.commandesNonTraitees = new ArrayList<>();
    this.clients = new HashSet<>();
  }

  // ******** INGREDIENTS ********

  @Override
  public int creerIngredient(String nom, double prix) {
    if (nom == null || nom.trim().isEmpty()) return -1;
    if (prix <= 0) return -3;

    String key = nom.trim().toLowerCase();

    Ingredient existant = ingredients.get(key);

    // Si l’ingrédient existe déjà
    if (existant != null) {
      // même prix -> refuse
      if (Double.compare(existant.getPrix(), prix) == 0) {
        return -2;
      }
      // prix différent -> on met à jour et c'est OK
      existant.setPrix(prix);
      return 1; // code spécial : "prix mis à jour"
    }

    // sinon création normale
    ingredients.put(key, new Ingredient(nom.trim(), prix));
    return 0;
  }


  @Override
  public int changerPrixIngredient(String nom, double prix) {
    if (nom == null || nom.isEmpty()) {
      return -1;
    }
    if (prix <= 0) {
      return -2;
    }
    Ingredient ingr = ingredients.get(nom);
    if (ingr == null) {
      return -3;
    }
    ingr.setPrix(prix);
    return 0;
  }

  @Override
  public boolean interdireIngredient(String nomIngredient, TypePizza type) {
    if (!ingredients.containsKey(nomIngredient) 
        &&  ingredients.keySet().stream().noneMatch(i -> i.equalsIgnoreCase(nomIngredient))) {
      return false;
    }
    String nomNettoye = nomIngredient.trim().toLowerCase();

    restrictions.putIfAbsent(nomNettoye, new HashSet<>());
    restrictions.get(nomNettoye).add(type);

    System.out.println("⚠️ Ingrédient interdit ajouté : " + nomNettoye + " pour type " + type);
    System.out.println("👉 Restrictions actuelles : " + restrictions);

    return true;
  }


  // ******** PIZZAS ********

  @Override
  public Pizza creerPizza(String nom, TypePizza type) {
    if (nom == null || nom.isEmpty() || pizzas.containsKey(nom)) {
      return null;
    }
    Pizza p = new Pizza(nom, type);
    pizzas.put(nom, p);
    return p;
  }

  @Override
  public int ajouterIngredientPizza(Pizza pizza, String nomIngredient) {
    if (pizza == null || !pizzas.containsValue(pizza)) {
      return -1;
    }
    if (nomIngredient == null || nomIngredient.isEmpty() 
        || !ingredients.containsKey(nomIngredient)) {
      return -2;
    }

    // ✅ Si la pizza n’a pas de type, on autorise tout
    if (pizza.getType() == null) {
      pizza.addIngredient(nomIngredient);
      return 0;
    }

    // 🔹 Vérifie si l’ingrédient est interdit pour ce type
    if (ingredientsInterdits.get(pizza.getType()).contains(nomIngredient)) {
      return -3;
    }

    pizza.addIngredient(nomIngredient);
    return 0;
  }

  @Override
  public int retirerIngredientPizza(Pizza pizza, String nomIngredient) {
    if (pizza == null || !pizzas.containsValue(pizza)) {
      return -1;
    }
    if (nomIngredient == null || nomIngredient.isEmpty() 
        || !ingredients.containsKey(nomIngredient)) {
      return -2;
    }
    if (!pizza.getIngredients().contains(nomIngredient)) {
      return -3;
    }
    pizza.getIngredients().remove(nomIngredient);
    return 0;
  }

  @Override
  public Set<String> verifierIngredientsPizza(Pizza pizza) {
    Set<String> interdits = new HashSet<>();
    if (pizza == null) {
      return interdits;
    }

    TypePizza type = pizza.getType();
    if (type == null) {
      return interdits; // si la pizza n’a pas de type, rien à interdire
    }

    for (String ingr : pizza.getIngredients()) {
      String ingrNettoye = ingr.trim().toLowerCase();
      Set<TypePizza> types = restrictions.get(ingrNettoye);
      if (types != null && types.contains(type)) {
        interdits.add(ingr);
      }
    }
    return interdits;
  }


  @Override
  public boolean ajouterPhoto(Pizza pizza, String file) throws IOException {
    if (pizza == null || !pizzas.containsValue(pizza)) {
      return false;
    }
    pizza.setCheminPhoto(file);
    return true;
  }

  // ******** PRIX ********

  @Override
  public double getPrixPizza(Pizza pizza) {
    if (pizza == null || !pizzas.containsValue(pizza)) {
      return -1;
    }
    return pizza.getPrixVente();
  }

  @Override
  public boolean setPrixPizza(Pizza pizza, double prix) {
    if (pizza == null || !pizzas.containsValue(pizza)) {
      return false;
    }
    double min = calculerPrixMinimalPizza(pizza);
    if (prix < min) {
      return false;
    }
    pizza.setPrixVente(prix);
    return true;
  }

  @Override
  public double calculerPrixMinimalPizza(Pizza pizza) {
    if (pizza == null || !pizzas.containsValue(pizza)) {
      return -1;
    }
    double somme = 0;
    for (String ingrNom : pizza.getIngredients()) {
      Ingredient ingr = ingredients.get(ingrNom);
      if (ingr != null) {
        somme += ingr.getPrix();
      }
    }
    somme = somme * 1.4; // +40%
    return Math.ceil(somme / 10) * 10; // arrondi à la dizaine supérieure
  }

  // ******** COMMANDES ********

  @Override
  public Set<Pizza> getPizzas() {
    return new HashSet<>(pizzas.values());
  }

  @Override
  public Set<InformationPersonnelle> ensembleClients() {
    return clients;
  }

  @Override
  public List<Commande> commandesDejaTraitees() {
    return commandesTraitees;
  }

  @Override
  public List<Commande> commandeNonTraitees() {
    List<Commande> aTraiter = new ArrayList<>(commandesNonTraitees);

    // On considère qu'on "traite" tout ce qui est dans nonTraitees
    commandesTraitees.addAll(aTraiter);
    commandesNonTraitees.clear();

    for (Commande c : aTraiter) {
      c.setEtat(Commande.EtatCommande.TRAITEE);
    }

    return aTraiter;
  }




  @Override
  public List<Commande> commandesTraiteesClient(InformationPersonnelle client) {
    if (client == null) {
      return null;
    }
    List<Commande> result = new ArrayList<>();
    for (Commande c : commandesTraitees) {
      if (c.getClient().getInfos().equals(client)) {
        result.add(c);
      }
    }
    return result;
  }
  
  /**
   * Ajoute une commande au suivi du pizzaïolo si elle n'est pas déjà enregistrée.
   * La commande est placée dans la liste des commandes non traitées.
   *
   * @param c la commande à ajouter
   */
  public void ajouterCommande(Commande c) {
    if (c != null && !commandesNonTraitees.contains(c) && !commandesTraitees.contains(c)) {
      commandesNonTraitees.add(c);
      System.out.println(" Commande ajoutée au suivi du pizzaiolo : " + c);
    }
  }

  
  // ******** BENEFICES ********

  @Override
  public Map<Pizza, Double> beneficeParPizza() {
    Map<Pizza, Double> map = new HashMap<>();
    for (Pizza p : pizzas.values()) {
      double min = calculerPrixMinimalPizza(p);
      map.put(p, p.getPrixVente() - min);
    }
    return map;
  }

  @Override
  public double beneficeCommandes(Commande commande) {
    if (commande == null) {
      return -1;
    }
    double benef = 0;
    List<Pizza> listPizzas = commande.getPizzas();
    List<Integer> listQuantites = commande.getQuantites();
    for (int i = 0; i < listPizzas.size(); i++) {
      Pizza p = listPizzas.get(i);
      double min = calculerPrixMinimalPizza(p);
      benef += (p.getPrixVente() - min) * listQuantites.get(i);
    }
    return benef;
  }

  @Override
  public double beneficeToutesCommandes() {
    double total = 0;
    for (Commande c : commandesTraitees) {
      total += beneficeCommandes(c);
    }
    return total;
  }

  @Override
  public Map<InformationPersonnelle, Integer> nombrePizzasCommandeesParClient() {
    Map<InformationPersonnelle, Integer> map = new HashMap<>();
    for (Commande c : commandesTraitees) {
      InformationPersonnelle info = c.getClient().getInfos();
      map.put(info, map.getOrDefault(info, 0) + c.getPizzas().size());
    }
    return map;
  }

  @Override
  public Map<InformationPersonnelle, Double> beneficeParClient() {
    Map<InformationPersonnelle, Double> map = new HashMap<>();
    for (Commande c : commandesTraitees) {
      InformationPersonnelle info = c.getClient().getInfos();
      map.put(info, map.getOrDefault(info, 0.0) + beneficeCommandes(c));
    }
    return map;
  }

  @Override
  public int nombrePizzasCommandees(Pizza pizza) {
    if (pizza == null || !pizzas.containsValue(pizza)) {
      return -1;
    }
    int total = 0;
    for (Commande c : commandesTraitees) {
      for (Pizza p : c.getPizzas()) {
        if (p.equals(pizza)) {
          total++;
        }
      }
    }
    return total;
  }

  @Override
  public List<Pizza> classementPizzasParNombreCommandes() {
    Map<Pizza, Integer> nb = new HashMap<>();
    for (Pizza p : pizzas.values()) {
      nb.put(p, nombrePizzasCommandees(p));
    }
    List<Pizza> liste = new ArrayList<>(pizzas.values());
    liste.sort((p1, p2) -> Integer.compare(nb.get(p2), nb.get(p1)));
    return liste;
  }
  /**
   * Retourne l'ensemble des ingrédients disponibles.
   *
   * @return les ingrédients
   */
  
  public Collection<Ingredient> getIngredients() {
    return ingredients.values();
  }
  /**
   * Ajoute une commande dans la liste des commandes NON TRAITÉES.
   * Utile pour initialiser des données au démarrage.
   */
  public void ajouterCommandeNonTraitee(Commande c) {
    if (c == null) {
      return;
    }
    if (!commandesNonTraitees.contains(c) && !commandesTraitees.contains(c)) {
      commandesNonTraitees.add(c);
      // On enregistre le client
      if (c.getClient() != null && c.getClient().getInfos() != null) {
        clients.add(c.getClient().getInfos());
      }
    }
  }

  /**
   * Ajoute une commande directement comme TRAITÉE.
   * Utile pour initialiser des statistiques (nb commandes, bénéfice total, etc.).
   */
  public void ajouterCommandeTraitee(Commande c) {
    if (c == null) {
      return;
    }
    if (!commandesTraitees.contains(c)) {
      commandesTraitees.add(c);
      commandesNonTraitees.remove(c);
      c.setEtat(Commande.EtatCommande.TRAITEE);
      // On enregistre le client
      if (c.getClient() != null && c.getClient().getInfos() != null) {
        clients.add(c.getClient().getInfos());
      }
    }
  }

}