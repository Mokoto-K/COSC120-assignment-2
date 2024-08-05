/**
 * @author -
 * Email -
 * created for COSC120 Assignment 2
 * Add descriptor
 */

public enum Filters {
    CATEGORY, TYPE, DWARF, TRAINING_SYSTEM, POLLINATORS, POT_SIZE, POT_SIZE_PRICE;

    // TODO fix this god damn description... jeeeezuz
    /**
     * @return a specific attribute to be used as keys for storage and comparison.
     */
    public String toString() {
        return switch (this) {
            case CATEGORY -> "Type of fruit tree";
            case TYPE -> "Type of fruit";
            case DWARF -> "Dwarf or not";
            case TRAINING_SYSTEM -> "System for training vines";
            case POLLINATORS -> "Pollinators for fruiting trees";
            // TODO might not need potsiZe
            case POT_SIZE -> "Size of the pot";
            case POT_SIZE_PRICE -> "Prices for pot sizes";
        };
    }
}
