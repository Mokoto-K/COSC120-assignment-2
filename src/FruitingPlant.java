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

    public FruitingPlant(String productCode, String productName, String description, String type, boolean dwarf, int potSize, Map<Integer,Float> potSizeToPrice) {
        this.productCode = productCode;
        this.productName = productName;
        this.description = description;
        this.dreamPlant = new DreamPlant(type, dwarf, potSize, potSizeToPrice,0, 0);
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

    // This will get changed and move, dont worry for now
    public StringBuilder getItemInformation() {
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuilder output = new StringBuilder("\n*******************************************");
        output.append("\n").append(getProductName()).append(" (").append(getProductCode()).append(")\n")
                .append(getDescription()).append("\nDwarf: ").append(isDwarf() ? "Yes" : "No")
                .append("\nAvailable pot sizes:\n| ");
        for (Integer potSize: potSizeToPrice.keySet()) {
            output.append(potSize).append("inch: $").append(df.format(potSizeToPrice.get(potSize))).append(" | ");
        }
        return output;
    }

}
