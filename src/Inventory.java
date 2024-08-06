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
            if (!fruitingPlant.getDreamPlant().compareDreamPlants(dreamFruitingPlant)) continue;
            Float correspondingPrice = fruitingPlant.getDreamPlant().getPotSizeToPrice().get(dreamFruitingPlant.getPotSize());
            if(correspondingPrice < dreamFruitingPlant.getMinPrice() || correspondingPrice > dreamFruitingPlant.getMaxPrice()) continue;
            matching.add(fruitingPlant);
        }
        return matching;
    }

    // TODO - Add NA to the set
    public Set<String> getAllTypes(){
        Set<String> allTypes = new LinkedHashSet<>();
        for(FruitingPlant fruitingPlant : inventory){
            allTypes.add(fruitingPlant.getDreamPlant().getType());
        }
        return allTypes;
    }

    public Set<String> getAllDwarfs(){
        Set<String> allDwarfs = new LinkedHashSet<>();
        return allDwarfs;
    }

    public Set<String> getAllTrellis(){
        Set<String> allDwarfs = new LinkedHashSet<>();
        return allDwarfs;
    }

    public Set<String> getAllPollinators(){
        Set<String> allDwarfs = new LinkedHashSet<>();
        return allDwarfs;
    }
    // TODO - Build a "getAllDwarfs" set creator
    // TODO - Build a "getAllTrellis" set creator
}
