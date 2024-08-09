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

    // TODO add pollinators and training
    public DreamFruitingPlant(Map<Filters,Object> filters, float minPrice, float maxPrice){
        // TODO - whited out becuase in seek a geek this is used, but in pet app its not, not sure what should be in final
//        if (filters==null) {
//            this.filters = new LinkedHashMap<>();
//        } else {
//            this.filters = new HashMap<>(filters);
//        }

        this.filters = new HashMap<>(filters);
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    // TODO Figure out what its returning when I can actually run the program, same for the getter below
    /**
     * Constructor for our hashmap, this will serve as our primary constructor for our program
     * @param filters a
     */
    public DreamFruitingPlant(Map<Filters, Object> filters) {
//        if (filters==null) {
//            this.filters = new LinkedHashMap<>();
//        } else {
//            this.filters = new HashMap<>(filters);
//        }
        this.filters = new HashMap<>(filters);
    }

    // TODO fill in this explainor after when it makes more sense
    /**
     * Getter for our Map of filters
     * @return
     */
    public Map<Filters, Object> getAllFilters() {
        return new HashMap<>(filters); }

    public Object getFilter(Filters key) {return getAllFilters().get(key); }

    public float getMaxPrice() {return maxPrice;}

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
                // Assign the pot price map to a new hashmap and iterate through all values appending them to the builder
                // Suppressing the warning that is returned from assigning this to a map as java won't know until runtime
                // if it is a map that contains integers and floats
                // TODO - Find a better solution
                @SuppressWarnings (value="unchecked")
                LinkedHashMap<Integer, Float> potSizeToPrice = (LinkedHashMap<Integer, Float>) this.getFilter(Filters.POT_SIZE_PRICE);
                for (Integer potSize : potSizeToPrice.keySet()) {
                    description.append(potSize).append("inch: $").append(df.format(potSizeToPrice.get(potSize))).append(" | ");
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

    // TODO - You have to try your hardest to implement exactly what this is doing, but in a more naturally you way
    // I didn't write this, so im uneasy with it.
    public boolean compareDreamPlants(DreamFruitingPlant dreamFruitingPlant) {
        System.out.println(dreamFruitingPlant.getAllFilters());
        System.out.println(this.getAllFilters());

        for (Filters key : dreamFruitingPlant.getAllFilters().keySet()) {

//            if (this.getFilter(key).equals(dreamFruitingPlant.getFilter(key).toString()))
//                return true;

//            System.out.println(key);
            if (this.getAllFilters().containsKey(key)) {
//                System.out.println("1");

                if (this.getFilter(key) instanceof Collection<?> && dreamFruitingPlant.getFilter(key) instanceof Collection<?>) {
                    Set<Object> intersect = new HashSet<>((Collection<?>) dreamFruitingPlant.getFilter(key));
                    intersect.retainAll((Collection<?>) dreamFruitingPlant.getFilter(key));
//                    System.out.println(key);
                    if (intersect.isEmpty()) return false;
                }

                // I added this elif, might need to be deleted
                else if (this.getFilter(key) instanceof Collection<?> && !(dreamFruitingPlant.getFilter(key) instanceof Collection<?>)) {
                    if (!((Collection<?>) this.getFilter(key)).contains(dreamFruitingPlant.getFilter(key))) {
                        return false;
                    }
                }

                else if (dreamFruitingPlant.getFilter(key) instanceof Collection<?> && !(this.getFilter(key) instanceof Collection<?>)) {
//                    System.out.println(key);
                    if (!((Collection<?>) dreamFruitingPlant.getFilter(key)).contains(this.getFilter(key))) {
//                        System.out.println(dreamFruitingPlant.getFilter(key) + " " + this.getFilter(key));
//                        System.out.println("4");
                        return false;
                    }

                }
                else if (!this.getFilter(key).equals(dreamFruitingPlant.getFilter(key))) {
//                    System.out.println(this.getFilter(key).toString());
//                    System.out.println(dreamFruitingPlant.getFilter(key).toString());
                    System.out.println(key + " " + this.getFilter(key).getClass() + " " + dreamFruitingPlant.getFilter(key).getClass());
                    return false;
                }
                System.out.println(key + " Matched");
            }
        }
        return true;
    }
}
