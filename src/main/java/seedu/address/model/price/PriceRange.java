package seedu.address.model.price;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents a Price Range in the real estate system.
 * Guarantees: immutable; is valid as Price is valid.
 * A PriceRange can be bounded or unbounded.
 * If it is bounded on both sides, it must have a lowerBoundPrice and an upperBoundPrice.
 * If it is bounded on one side, it must have one null field.
 * If it is unbounded, it must have two null fields.
 */
public class PriceRange {

    public final Price lowerBoundPrice;
    public final Price upperBoundPrice;

    /**
     * Constructs an unbounded {@code PriceRange}.
     */
    public PriceRange() {
        this.lowerBoundPrice = null;
        this.upperBoundPrice = null;
    }

    /**
     * Constructs a {@code PriceRange} with one bound.
     *
     * @param singleBoundPrice the price that is the bound.
     * @param isUpperBound     if true, the singleBoundPrice is the upper bound, otherwise it is the lower bound.
     */
    public PriceRange(Price singleBoundPrice, boolean isUpperBound) {
        requireNonNull(singleBoundPrice);

        if (isUpperBound) {
            this.lowerBoundPrice = null;
            this.upperBoundPrice = singleBoundPrice;
        } else {
            this.lowerBoundPrice = singleBoundPrice;
            this.upperBoundPrice = null;
        }
    }

    /**
     * Constructs a {@code PriceRange} with both bounds.
     *
     * @param lowerBoundPrice the lower bound price.
     * @param upperBoundPrice the upper bound price.
     */
    public PriceRange(Price lowerBoundPrice, Price upperBoundPrice) {
        requireNonNull(lowerBoundPrice);
        requireNonNull(upperBoundPrice);
        checkArgument(lowerBoundPrice.compare(upperBoundPrice) <= 0);
        this.lowerBoundPrice = lowerBoundPrice;
        this.upperBoundPrice = upperBoundPrice;
    }

    /**
     * Checks if a price is within the price range.
     *
     * @param otherPrice the price to check.
     * @return true if the price is within the range, false otherwise.
     */
    public boolean isPriceWithinRange(Price otherPrice) {
        requireNonNull(otherPrice);

        // If range is unbounded
        if (this.lowerBoundPrice == null && this.upperBoundPrice == null) {
            return true;
        }

        // If range is only upper bounded
        if (this.lowerBoundPrice == null) {
            return otherPrice.compare(this.upperBoundPrice) <= 0;
        }

        // If range is only lower bounded
        if (this.upperBoundPrice == null) {
            return otherPrice.compare(this.lowerBoundPrice) >= 0;
        }

        // If range is bounded on both sides
        return otherPrice.compare(this.lowerBoundPrice) >= 0 && otherPrice.compare(this.upperBoundPrice) <= 0;
    }

    /**
     * Checks if two price ranges overlap.
     *
     * @param otherPriceRange the other price range to check.
     * @return true if the price ranges overlap, false otherwise.
     */
    public boolean doPriceRangeOverlap(PriceRange otherPriceRange) {
        requireNonNull(otherPriceRange);

        boolean isLowerBoundWithinRange = otherPriceRange.lowerBoundPrice == null
                || this.isPriceWithinRange(otherPriceRange.lowerBoundPrice);
        boolean isUpperBoundWithinRange = otherPriceRange.upperBoundPrice == null
                || this.isPriceWithinRange(otherPriceRange.upperBoundPrice);

        return isLowerBoundWithinRange || isUpperBoundWithinRange;
    }

    @Override
    public String toString() {
        if (lowerBoundPrice == null && upperBoundPrice == null) {
            return "Any Price";
        } else if (lowerBoundPrice == null) {
            return "Up to " + upperBoundPrice;
        } else if (upperBoundPrice == null) {
            return "From " + lowerBoundPrice;
        } else {
            return lowerBoundPrice + " to " + upperBoundPrice;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PriceRange)) {
            return false;
        }

        PriceRange otherPriceRange = (PriceRange) other;
        return Objects.equals(this.lowerBoundPrice, otherPriceRange.lowerBoundPrice)
                && Objects.equals(this.upperBoundPrice, otherPriceRange.upperBoundPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBoundPrice, upperBoundPrice);
    }

}
