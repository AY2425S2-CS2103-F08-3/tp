package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person's property preference in the real estate system.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class PropertyPreference {

    // Data fields
    private final PriceRange priceRange;

    // Associations
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Constructs a {@code PropertyPreference}.
     * Every field must be present and not null.
     *
     * @param priceRange A valid price range.
     * @param tags A valid set of tags.
     */
    public PropertyPreference(PriceRange priceRange, Set<Tag> tags) {
        requireAllNonNull(priceRange, tags);
        this.priceRange = priceRange;
        this.tags.addAll(tags);
    }

    /**
     * Constructs a {@code PropertyPreference}.
     * Every field must be present and not null.
     *
     * @param priceRange A valid price range.
     */
    public PropertyPreference(PriceRange priceRange) {
        requireAllNonNull(priceRange);
        this.priceRange = priceRange;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PropertyPreference)) {
            return false;
        }

        PropertyPreference otherPropertyPreference = (PropertyPreference) other;
        return this.priceRange.equals(otherPropertyPreference.priceRange)
                && tags.equals(otherPropertyPreference.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(priceRange, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("price range", priceRange)
                .add("tags", tags)
                .toString();
    }
}
