/**
 * @author -
 * Email -
 * created for COSC120 Assignment 2
 * Add descriptor
 */

import java.util.*;

public class Inventory {

    private final Set<FruitingPlant> inventory = new HashSet<>();

    public void addItem(FruitingPlant fruitingPlant){
        this.inventory.add(fruitingPlant);
    }

    public List<FruitingPlant> findMatch(DreamFruitingPlant dreamFruitingPlant){
        List<FruitingPlant> matching = new ArrayList<>();
        for(FruitingPlant fruitingPlant : inventory){
            if (!fruitingPlant.dreamFruitingPlant().compareDreamPlants(dreamFruitingPlant)) continue;
//            Float correspondingPrice = fruitingPlant.getDreamPlant().getPotSizeToPrice().get(dreamFruitingPlant.getPotSize());
//            if(correspondingPrice < dreamFruitingPlant.getMinPrice() || correspondingPrice > dreamFruitingPlant.getMaxPrice()) continue;
            matching.add(fruitingPlant);
        }
        return matching;
    }

    // TODO - I think we want to query the map, not the enum for values
    // TODO - Add NA to the set
    public Set<String> getAllTypes(String category){
        Set<String> allTypes = new LinkedHashSet<>();
        for(FruitingPlant fruitingPlant : inventory){
            if (fruitingPlant.dreamFruitingPlant().getFilter(Filters.CATEGORY).equals(category)) {
                allTypes.add(fruitingPlant.dreamFruitingPlant().getFilter(Filters.TYPE).toString());
            }
        }
        allTypes.add("SKIP (any will do)");
        return allTypes;
    }

    public Set<String> getAllTrellis(String category){
        Set<String> allTrellis = new LinkedHashSet<>();
        for(FruitingPlant fruitingPlant : inventory){
            if (fruitingPlant.dreamFruitingPlant().getFilter(Filters.CATEGORY).equals(category)) {
                allTrellis.add(fruitingPlant.dreamFruitingPlant().getFilter(Filters.TRAINING_SYSTEM).toString());
            }
        }
        allTrellis.add("SKIP (any will do)");

        return allTrellis;
    }
    // TODO - Change NA to none if you like
    public Set<String> getAllPollinators(String category){
        Set<String> allPollinators = new LinkedHashSet<>();

        for(FruitingPlant fruitingPlant : inventory){
            if (fruitingPlant.dreamFruitingPlant().getFilter(Filters.CATEGORY).equals(category)) {
                String[] pollinatorsList = fruitingPlant.dreamFruitingPlant().getFilter(Filters.POLLINATORS).toString().split(",");
                for(String p : pollinatorsList) {
                    allPollinators.add(p.replace("]","").replace("[","").trim());
                }
            }
        }
        allPollinators.add("SKIP (any will do)");

        return allPollinators;
    }
}
