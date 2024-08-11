/**
 * @author -
 * Email -
 * created for COSC120 Assignment 2
 * Add descriptor
 */

import java.text.DecimalFormat;
import java.util.*;

public class DreamFruitingPlant {

    private float maxPrice;
    private float minPrice;
    private final Map<Filters,Object> filters;

    /**
     * A constructor serving primarily for user input
     * @param filters - a map of the users dream fruit
     * @param minPrice the min price a user is willing to pay
     * @param maxPrice the max price a user is willing to pay
     */
    public DreamFruitingPlant(Map<Filters,Object> filters, float minPrice, float maxPrice){
        this.filters = new HashMap<>(filters);
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    /**
     * Constructor to specificly handle our hashmap, this will serve as our primary constructor for our database
     * @param filters a map containing all plants from the database and their attributes
     */
    public DreamFruitingPlant(Map<Filters, Object> filters) {
        this.filters = new HashMap<>(filters);
    }

    /**
     * Getter for our Map of filters
     * @return a hashmap of all plants and there attributes
     */
    public Map<Filters, Object> getAllFilters() {
        return new HashMap<>(filters); }

    /**
     *
     * @param key representing a value from the filters enum
     * @return the value from the fruitingPlant map returned when passing the key in
     */
    public Object getFilter(Filters key) {return getAllFilters().get(key); }

    /**
     *
     * @return gets the price of the users max price input
     */
    public float getMaxPrice() {return maxPrice;}

    /**
     *
     * @return gets the price of the users min price input
     */
    public float getMinPrice() {return minPrice;}

    /**
     * Takes a map and iterates through all keys and their values to a string builder that is returned as the description
     * for any given tree.
     * @param filters a map that contains all the keys from the users dream fruit to be used to iterate through and display
     *                all stored fruit trees that matched user criteria
     * @return String - String builders "to string" output of concatenated description data.
     */
    public String getDreamPlantInformation(Map<Filters, Object> filters) {
        // Create a new string builder to hold all of our description
        StringBuilder description = new StringBuilder();
        for (Filters key : filters.keySet()) {
            // Check if the key is pot size, if it is we must get the map holding the pot size's and prices and append
            // all the values to the builder.
            if (key.equals(Filters.POT_SIZE)) {
                description.append("\n").append(Filters.POT_SIZE_PRICE).append(" : ");
                DecimalFormat df = new DecimalFormat("0.00");

                // Initialise a LinkedHashMap, to retain order, this will be used to map the pot sizes to price from the db
                Map<Integer, Float> potSizeToPriceMap = new LinkedHashMap<>();

                // Get a hold of the Pot-size-to-price-map from our main plant map, turn it into a string, clean off the unwanted
                // characters and create an array of strings, splitting on the comma.
                String[] potSizePriceList = this.getFilter(Filters.POT_SIZE_PRICE).toString()
                        .replace("{", "").replace("}","").split(",");

                // Next, for every string in the potSizePriceList, create another String array, splitting on the equals sign
                // and put both the first and second element into the map, parsing them as int and float. You now have control
                // of your pot size to price map again
                for(String potPrices : potSizePriceList) {
                    String[] temp = potPrices.trim().split("=");
                    potSizeToPriceMap.put(Integer.parseInt(temp[0]), Float.parseFloat(temp[1]));
                }
                // For each key in the map, append the key and the value to our string builder
                for (Integer potSize : potSizeToPriceMap.keySet()) {
                    description.append(potSize).append("inch: $").append(df.format(potSizeToPriceMap.get(potSize))).append(" | ");
                }
            }
            // Check if the key set is a collection, if so, this means it's a list and there are multiple values to be
            // displayed, therefore iterate through all the values and append them to the string builder differently
            else if (this.getFilter(key) instanceof Collection<?>) {
                description.append("\n").append(key).append(" : ");
                // For every value in the collection, append with a '|' between
                for (Object obj : ((Collection<?>) this.getFilter(key)).toArray()) {
                    description.append(obj).append(" | ");
                }
            }
            else {
                // Append the key and the values to the string builder
                description.append("\n").append(key).append(" : ").append(getFilter(key));
            }
        }
        return description.toString();
    }

    /**
     * The comparison method for all plants. Compares the users dream plant against every plant in the database.
     * @param dreamFruitingPlant the users dream plant in the form of a map
     * @return true or false depending on if the plants match or not
     */
    public boolean compareDreamPlants(DreamFruitingPlant dreamFruitingPlant) {
        // Iterate through all the keys in the users dream fruiting plant map, this ensures we only iterate through
        // values (keys) stored in the users map and check for comparison of only those keys
        for (Filters key : dreamFruitingPlant.getAllFilters().keySet()) {
            // if a plant from the database contains a key that is also in the users dream plant map
            if (this.getAllFilters().containsKey(key)) {

                // If both the database plant and the users dream plant are collection, compare them here
                if (this.getFilter(key) instanceof Collection<?> && dreamFruitingPlant.getFilter(key) instanceof Collection<?>) {
                    // Creating a set of objects of the database and dream users plants collection
                    Set<Object> intersect = new HashSet<>((Collection<?>) dreamFruitingPlant.getFilter(key));
                    // if the set is empty after running retain all, then there were no matching features so we return false
                    intersect.retainAll((Collection<?>) dreamFruitingPlant.getFilter(key));
                    if (intersect.isEmpty()) return false;
                }
                // Comparing if the database plant is a collection and the users dream plants value is not
                else if (this.getFilter(key) instanceof Collection<?> && !(dreamFruitingPlant.getFilter(key) instanceof Collection<?>)) {
                    // return false if the users dream plant attribute isnt in the database plants collection
                    if (!((Collection<?>) this.getFilter(key)).contains(dreamFruitingPlant.getFilter(key))) {
                        return false;
                    }
                }
                // Comparing if the users dream plants value is a collection but the database plant is not
                else if (dreamFruitingPlant.getFilter(key) instanceof Collection<?> && !(this.getFilter(key) instanceof Collection<?>)) {
                    // return false if the database plants attribute is not in the user's plants collection
                    if (!((Collection<?>) dreamFruitingPlant.getFilter(key)).contains(this.getFilter(key))) {
                        return false;
                    }
                }
                // Comparing if the value from the database plant and the users dream plant are single values, not collections
                else if (!this.getFilter(key).equals(dreamFruitingPlant.getFilter(key))) {
                    // returning false if they don't match
                    return false;
                }
                // This else statement is a protective measure against there being an error in the database, since we are
                // comparing if the key from a users plant is a key shared by a plant in the database, we should never
                // end up here, but if there is a mistake in the database, and it doesn't contain a key (plant value) that
                // we expect it to have, the top "if" statement will be false and instantly return true without any
                // comparison.
            } else {
                return false;
            }
        }
        // if a plant survives that gauntlet... we add it
        return true;
    }
}
