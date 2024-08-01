import java.text.DecimalFormat;
import java.util.Map;

public class FruitingPlant {
    //fields
    private final String productCode;
    private final String productName;
    private final String description;
    private final DreamPlant dreamPlant;
//    private final String type;
//    private final boolean dwarf;
//    private final Map<Integer,Float> potSizeToPrice;
//    private float maxPrice;
//    private float minPrice;
//    private final int potSize;

    public FruitingPlant(String productCode, String productName, String description, DreamPlant dreamPlant) {
        this.productCode = productCode;
        this.productName = productName;
        this.description = description;
        this.dreamPlant = dreamPlant;
//        this.type = type;
//        this.dwarf = dwarf;
//        this.potSize = potSize;
//        this.potSizeToPrice = potSizeToPrice;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public DreamPlant getDreamPlant() {return dreamPlant;}

    //    public String getType() {
//        return type;
//    }
//
//    public boolean isDwarf() {
//        return dwarf;
//    }
//
//    public Map<Integer,Float> getPotSizeToPriceOptions() {
//        return potSizeToPrice;
//    }
//
//    public float getMaxPrice() {
//        return maxPrice;
//    }
//
//    public float getMinPrice() {
//        return minPrice;
//    }
//
//    public int getPotSize() {
//        return potSize;
//    }
//
//    public void setMaxPrice(int maxPrice) {
//        this.maxPrice=maxPrice;
//    }
//
//    public void setMinPrice(int minPrice) {
//        this.minPrice=minPrice;
//    }

    public StringBuilder getItemInformation() {
        StringBuilder output = new StringBuilder("\n*******************************************");
        output.append("\n").append(getProductName()).append(" (").append(getProductCode()).append(")\n")
                .append(getDescription()).append("\nDwarf: ").append(this.getDreamPlant().getDreamPlantInformation());
        return output;
    }

}
