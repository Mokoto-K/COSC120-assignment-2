/**
 * @author -
 * Email -
 * created for COSC120 Assignment 2
 * Add descriptor
 */

// An enum class representing the choice for the dwarf option of a plant
public enum Dwarf {
    YES, NO, NA;

    /**
     *
     * @return a prettified version of the case that is selected by the user.
     */
    public String toString() {
        return switch (this) {
            case YES -> "Yes";
            case NO -> "No";
            case NA -> "I don't mind";
        };
    }
}
