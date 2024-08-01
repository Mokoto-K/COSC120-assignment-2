import java.util.*;

public class Inventory {

    private final Set<FruitingPlant> inventory = new HashSet<>();

    public void addItem(FruitingPlant fruitingPlant){
        this.inventory.add(fruitingPlant);
    }

    public List<FruitingPlant> findMatch(DreamPlant dreamFruitingPlant){
        List<FruitingPlant> matching = new ArrayList<>();
        for(FruitingPlant fruitingPlant : inventory){
//            if(!fruitingPlant.getDreamPlant().getType().equals(dreamFruitingPlant.getType())) continue;
//            if(!fruitingPlant.getDreamPlant().getPotSizeToPrice().containsKey(dreamFruitingPlant.getPotSize())) continue;
//            if(fruitingPlant.getDreamPlant().isDwarf() != dreamFruitingPlant.isDwarf()) continue;
            fruitingPlant.getDreamPlant().compareDreamPlants(dreamFruitingPlant);
            Float correspondingPrice = fruitingPlant.getDreamPlant().getPotSizeToPrice().get(dreamFruitingPlant.getPotSize());
            if(correspondingPrice < dreamFruitingPlant.getMinPrice() || correspondingPrice > dreamFruitingPlant.getMaxPrice()) continue;
            matching.add(fruitingPlant);
        }
        return matching;
    }

    public Set<String> getAllTypes(){
        Set<String> allTypes = new LinkedHashSet<>();
        for(FruitingPlant fruitingPlant : inventory){
            allTypes.add(fruitingPlant.getDreamPlant().getType());
        }
        return allTypes;
    }
}
