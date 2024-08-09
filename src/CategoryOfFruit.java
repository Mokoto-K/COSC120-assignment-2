/**
 * @author -
 * Email -
 * created for COSC120 Assignment 2
 * Add descriptor
 */

// an enum class that represents all the different categories of fruiting plants from the database
public enum CategoryOfFruit {
    POME, VINE, CITRUS, STONE_FRUIT;

    // TODO perhaps change the strings to be more.... glamorous
    /**
     * @return a cute rendition of the corresponding enum class of fruit
     */
    public String toString() {
        return switch (this) {
            case POME -> "Pome";
            case VINE -> "Vine";
            case CITRUS -> "Citrus";
            case STONE_FRUIT -> "Stone Fruit";
        };
    }
}
