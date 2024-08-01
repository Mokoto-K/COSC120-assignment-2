import java.text.DecimalFormat;
import java.util.Map;

public class DreamPlant {

    private final String type;
    private final boolean dwarf;
    private final int potSize;
    private final Map<Integer,Float> potSizeToPrice;
    private float maxPrice;
    private float minPrice;

    public DreamPlant(String type, boolean dwarf, int potSize, Map<Integer,Float> potSizeToPrice, float maxPrice, float minPrice){

        this.type = type;
        this.dwarf = dwarf;
        this.potSize = potSize;
        this.potSizeToPrice = potSizeToPrice;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    public String getType() {return type;}

    public boolean isDwarf() {return dwarf;}

    public int getPotSize() {return potSize;}

    public Map<Integer, Float> getPotSizeToPrice() {return potSizeToPrice;}

    public float getMaxPrice() {return maxPrice;}

    public float getMinPrice() {return minPrice;}

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

    public boolean compareDreamPlants(DreamPlant dreamFruitingPlant) {
        if(!this.getType().equals(dreamFruitingPlant.getType())) return false;
        if(!this.getPotSizeToPrice().containsKey(dreamFruitingPlant.getPotSize())) return false;
        if(this.isDwarf() != dreamFruitingPlant.isDwarf()) return false;
        return true;
    }

}
