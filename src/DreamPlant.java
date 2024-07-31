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



}
