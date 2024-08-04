/**
 * @author -
 * Email -
 * created for COSC120 Assignment 2
 * Add descriptor
 */

public enum Filters {
    TYPE, DWARF, TRAINING_SYSTEM, POLLINATORS, POT_SIZE;

    // TODO fix this god damn description... jeeeezuz
    /**
     * @return a specific attribute to be used as keys for storage and comparison.
     */
    public String toString() {
        return switch (this) {
            case TYPE -> "Type of fruit tree";
            case DWARF -> "Dwarf or not";
            case TRAINING_SYSTEM -> "System for training vines";
            case POLLINATORS -> "Pollinators for fruiting trees";
            case POT_SIZE -> "Size of the pot";
        };
    }
}
