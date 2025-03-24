package seedu.address.model.price;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Price in the real estate system.
 * Guarantees: immutable; is valid as declared in {@link #isValidPrice(String)}
 */
public class Price {
    public static final String MESSAGE_CONSTRAINTS =
            "Price must be a non-negative number with up to 2 decimal places and at least 1 digit is present. "
                    + "If no value is given, the lower bound price will be unbounded.";

    /*
     * First character must be a letter
     * The rest of the characters must be letters, apostrophes, spaces, periods, or hyphens
     * Length of name should be between 2 and 50 characters
     */
    public static final String VALIDATION_REGEX = "^[0-9]+(?:\\.\\d{1,2})?$";

    public final Float price;

    /**
     * Constructs a {@code Price}.
     *
     * @param price A valid price.
     */
    public Price(String price) {
        requireNonNull(price);
        checkArgument(isValidPrice(price), MESSAGE_CONSTRAINTS);
        this.price = Float.parseFloat(price);
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidPrice(String test) {
        if (test == null) {
            return true;
        }

        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Compares the value of this price with the value of another price.
     *
     * @param otherPrice other price to compare with.
     * @return 0 if the values are equal. 1 if this price is greater. -1 if this price is smaller.
     */
    public int compare(Price otherPrice) {
        requireNonNull(otherPrice);

        return Float.compare(this.price, otherPrice.price);
    }

    @Override
    public String toString() {
        return "$" + price;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Price)) {
            return false;
        }

        Price otherPrice = (Price) other;
        return this.price.equals(otherPrice.price);
    }

    @Override
    public int hashCode() {
        return Float.hashCode(price);
    }

}
