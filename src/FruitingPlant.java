import java.util.Map;
/**
 * @author -
 * Email -
 * created for COSC120 Assignment 2
 * Add descriptor
 */
// A record class representing Fruiting plants in the database
public record FruitingPlant(String productName, String productCode, String description, DreamFruitingPlant dreamFruitingPlant) {

    /**
     * A to String method for the fruiting plant class, this returns a concatinated string of all the information of
     * a tree if it matches with a users dream tree.
     * @param filters a map representing the users dream fruiting plant
     * @return String - a concatination of all details owned by a given tree.
     */
    public StringBuilder toString(Map<Filters, Object> filters) {
        // Initate the string builder
        StringBuilder output = new StringBuilder("\n*******************************************");
        // Add all fields to the string, calling the dream fruiting plants description method passing in the map to get
        // the generic details.
        output.append("\n").append(this.productName()).append(" (").append(this.productCode()).append(")\n")
                .append(this.description()).append(this.dreamFruitingPlant().getDreamPlantInformation(filters));
        return output;
    }

}
