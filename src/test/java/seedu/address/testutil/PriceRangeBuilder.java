package seedu.address.testutil;

import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;

/**
 * A utility class to help with building PriceRange objects.
 */
public class PriceRangeBuilder {

    private Price lowerBoundPrice;
    private Price upperBoundPrice;

    /**
     * Creates a {@code PriceRangeBuilder} with default values (unbounded).
     */
    public PriceRangeBuilder() {
        this.lowerBoundPrice = null;
        this.upperBoundPrice = null;
    }

    /**
     * Initializes the PriceRangeBuilder with the data of {@code priceRangeToCopy}.
     */
    public PriceRangeBuilder(PriceRange priceRangeToCopy) {
        this.lowerBoundPrice = priceRangeToCopy.lowerBoundPrice;
        this.upperBoundPrice = priceRangeToCopy.upperBoundPrice;
    }

    /**
     * Sets the {@code lowerBoundPrice} of the {@code PriceRange} that we are building.
     */
    public PriceRangeBuilder withLowerBoundPrice(String lowerBoundPrice) {
        this.lowerBoundPrice = new Price(lowerBoundPrice);
        return this;
    }

    /**
     * Sets the {@code upperBoundPrice} of the {@code PriceRange} that we are building.
     */
    public PriceRangeBuilder withUpperBoundPrice(String upperBoundPrice) {
        this.upperBoundPrice = new Price(upperBoundPrice);
        return this;
    }

    /**
     * Creates a bounded {@code PriceRange} with both lower and upper bounds.
     */
    public PriceRange build() {
        if (lowerBoundPrice == null && upperBoundPrice == null) {
            return new PriceRange();
        } else if (lowerBoundPrice == null) {
            return new PriceRange(upperBoundPrice, true);
        } else if (upperBoundPrice == null) {
            return new PriceRange(lowerBoundPrice, false);
        } else {
            return new PriceRange(lowerBoundPrice, upperBoundPrice);
        }
    }

    /**
     * Creates a {@code PriceRange} from a string in the format "lower-upper".
     * If the format is just "upper", it will create an upper-bounded range.
     * If the format is just "lower-", it will create a lower-bounded range.
     */
    public static PriceRange createFromString(String rangeString) {
        if (rangeString == null || rangeString.isEmpty()) {
            return new PriceRange();
        }

        if (rangeString.contains("-")) {
            String[] parts = rangeString.split("-");
            if (parts.length == 2) {
                if (parts[0].isEmpty()) {
                    // Format: "-upper"
                    return new PriceRange(new Price(parts[1]), true);
                } else if (parts[1].isEmpty()) {
                    // Format: "lower-"
                    return new PriceRange(new Price(parts[0]), false);
                } else {
                    // Format: "lower-upper"
                    return new PriceRange(new Price(parts[0]), new Price(parts[1]));
                }
            }
        }

        // Just a single price - assuming it's an upper bound
        return new PriceRange(new Price(rangeString), true);
    }
}
