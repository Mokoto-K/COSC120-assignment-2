/**
 * @author -
 * Email -
 * created for COSC120 Assignment 2
 * Add descriptor
 */

// an enum class representing all the generic data values in the database, this is used in combination with a map
// to perform data abstraction in our program by accessing the keys for the data where ever necessary
public enum Filters {
    CATEGORY, TYPE, DWARF, TRAINING_SYSTEM, POLLINATORS, POT_SIZE, POT_SIZE_PRICE;

    /**
     * @return a case that represents a specific key in a map that holds all of our
     * databases attribute values, it is accessed all over our program.
     */
    public String toString() {
        return switch (this) {
            case CATEGORY -> "Type of fruit tree";
            case TYPE -> "Type of fruit";
            case DWARF -> "Dwarf or not";
            case TRAINING_SYSTEM -> "System for training vines";
            case POLLINATORS -> "Pollinators for fruiting trees";
            case POT_SIZE -> "Size of the pot";
            case POT_SIZE_PRICE -> "Prices for pot sizes";
        };
    }
}
