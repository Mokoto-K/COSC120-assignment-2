/**
 * @author -
 * Email -
 * created for COSC120 Assignment 2
 * Add descriptor
 */

import java.text.DecimalFormat;
import java.util.*;

public class DreamFruitingPlant {

    private final float maxPrice;
    private final float minPrice;
    private final Map<Filters,Object> filters;

    // TODO add pollinators and training
    public DreamFruitingPlant(Map<Filters,Object> filters, float maxPrice, float minPrice){
        if (filters==null) {
            this.filters = new LinkedHashMap<>();
        } else {
            this.filters = new HashMap<>(filters);
        }
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    // TODO Figure out what its returning when I can actually run the program, same for the getter below
    /**
     * Constructor for our hashmap, this will serve as our primary constructor for our program
     * @param filters a
     */
    public DreamFruitingPlant(Map<Filters, Object> filters) {
        if (filters==null) {
            this.filters = new LinkedHashMap<>();
        } else {
            this.filters = new HashMap<>(filters);
        }
        // TODO will have to fix this at some point
        maxPrice =-1;
        minPrice = -1;
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

// TODO - The structure of the string will need to change once I can run the program
    public StringBuilder getDreamPlantInformation(Map<Filters, Object> filters) {
        StringBuilder sb = new StringBuilder();
        for (Filters key : filters.keySet()) {
            sb.append("\n").append(key).append(" : ").append(getFilter(key));
        }
        return sb;
//        DecimalFormat df = new DecimalFormat("0.00");
//        sb.append(isDwarf() ? "Yes" : "No")
//                .append("\nAvailable pot sizes:\n| ");
//        for (Integer potSize: potSizeToPrice.keySet()) {
//            sb.append(potSize).append("inch: $").append(df.format(potSizeToPrice.get(potSize))).append(" | ");
//        }
//        sb.append("\n\n");
//        return sb;
    }

    // TODO - You have to try your hardest to implement exactly what this is doing, but in a more naturally you way
    // I didnt write thie, so im uneasy with it.
    public boolean compareDreamPlants(DreamFruitingPlant dreamFruitingPlant) {
        System.out.println(dreamFruitingPlant.getAllFilters());
        System.out.println(this.getAllFilters());



        for (Filters key : dreamFruitingPlant.getAllFilters().keySet()) {

//            if (this.getFilter(key).equals(dreamFruitingPlant.getFilter(key).toString()))
//                return true;


            System.out.println(key);
            if (this.getAllFilters().containsKey(key)) {
                System.out.println("1");

                if (this.getFilter(key) instanceof Collection<?> && dreamFruitingPlant.getFilter(key) instanceof Collection<?>) {
                    Set<Object> intersect = new HashSet<>((Collection<?>) dreamFruitingPlant.getFilter(key));
                    intersect.retainAll((Collection<?>) dreamFruitingPlant.getFilter(key));
                    System.out.println("2");
                    if (intersect.isEmpty()) return false;
                }

                // I added this elif, might need to be deleted
                else if (this.getFilter(key) instanceof Collection<?> && !(dreamFruitingPlant.getFilter(key) instanceof Collection<?>)) {
                    return true;

                }


                else if (dreamFruitingPlant.getFilter(key) instanceof Collection<?> && !(this.getFilter(key) instanceof Collection<?>)) {
                    System.out.println("3");
                    if (!((Collection<?>) dreamFruitingPlant.getFilter(key)).contains(this.getFilter(key))) {

                        System.out.println("4");
                        return false;
                    }

                }
                else if (!this.getFilter(key).equals(dreamFruitingPlant.getFilter(key).toString())) {
                    System.out.println(this.getFilter(key).toString());
                    System.out.println(dreamFruitingPlant.getFilter(key).toString());
                    System.out.println("5");
                    return false;
                }
            }
        }
        return true;
    }

}













/*
// FOR SAFE KEEPING BEFORE I NUKE THIS CLASS, WILL DELETE WHEN THE TIME COMES

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DreamFruitingPlant {

    private final String type;
    private final boolean dwarf;
    private final int potSize;
    private final Map<Integer,Float> potSizeToPrice;
    private final String trainingSystem;
    private final List<String> pollinators;
    private final float maxPrice;
    private final float minPrice;
    private final Map<Filters,Object> filters;

    // TODO add pollinators and training
    public DreamFruitingPlant(String type, boolean dwarf, int potSize, Map<Integer,Float> potSizeToPrice, float maxPrice, float minPrice){

        this.type = type;
        this.dwarf = dwarf;
        this.potSize = potSize;
        this.potSizeToPrice = potSizeToPrice;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    // TODO fill this in once youve initiated it properly and it has a function.

    public DreamFruitingPlant(int maxPrice, int minPrice) {
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }
    // TODO Figure out what its returning when I can actually run the program, same for the getter below

    public DreamFruitingPlant(Map<Filters, Object> filters) {
        this.filters = new HashMap<>(filters);
    }

    public Map<Filters, Object> getFilters() {return filters; }

    public String getType() {return type;}

    public boolean isDwarf() {return dwarf;}

    public int getPotSize() {return potSize;}

    public Map<Integer, Float> getPotSizeToPrice() {return potSizeToPrice;}

    public float getMaxPrice() {return maxPrice;}

    public float getMinPrice() {return minPrice;}
    // TODO - Turn this into just a string, also do the same with other classes with descriptiuons
    public StringBuilder getDreamPlantInformation() {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.00");
        sb.append(isDwarf() ? "Yes" : "No")
                .append("\nAvailable pot sizes:\n| ");
        for (Integer potSize: potSizeToPrice.keySet()) {
            sb.append(potSize).append("inch: $").append(df.format(potSizeToPrice.get(potSize))).append(" | ");
        }
        sb.append("\n\n");
        return sb;
    }

    public boolean compareDreamPlants(DreamFruitingPlant dreamFruitingPlant) {
        if(!this.getType().equals(dreamFruitingPlant.getType())) return false;
        if(!this.getPotSizeToPrice().containsKey(dreamFruitingPlant.getPotSize())) return false;
        if(this.isDwarf() != dreamFruitingPlant.isDwarf()) return false;
        return true;
    }

}




 */