package seedu.address.model.tag;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.PropertyPreference;

/**
 * Represents a Tag in the address book.
 * Guarantees: tag name is immutable; tag is valid as declared in {@link #isValidTagName(String)};
 * all details are present and not null. Associations are mutable.
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS = "Tags must be between 2 and 30 characters long and can only "
            + "contain letters, numbers, apostrophes, spaces, periods, hyphens, underscores, plus, and ampersands. "
            + "The tag cannot be blank and must not already exist (unless for deleting).";
    public static final String VALIDATION_REGEX = "^[a-zA-Z0-9' ._+&-]{2,30}$";

    private static final ObservableMap<String, Tag> activeSearchTags = FXCollections.observableHashMap();

    // Identity fields
    public final String tagName;

    // Associations
    private final List<PropertyPreference> propertyPreferences = new ArrayList<>();
    private final List<Listing> listings = new ArrayList<>();



    /**
     * Constructs a {@code Tag}.
     * The constructor is protected to ensure that tags cannot be created without using the registry.
     *
     * @param tagName A valid tag name.
     * @param propertyPreferences A valid list of property preferences.
     * @param listings A valid list of listings.
     */
    public Tag(String tagName, List<PropertyPreference> propertyPreferences, List<Listing> listings) {
        requireAllNonNull(tagName, propertyPreferences, listings);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);

        this.tagName = tagName.toUpperCase();
        this.propertyPreferences.addAll(propertyPreferences);
        this.listings.addAll(listings);
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public String getTagName() {
        return tagName;
    }

    /**
     * Returns an immutable property preferences list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<PropertyPreference> getPropertyPreferences() {
        return Collections.unmodifiableList(propertyPreferences);
    }

    public void addPropertyPreference(PropertyPreference toAdd) {
        this.propertyPreferences.add(toAdd);
    }

    public void removePropertyPreference(PropertyPreference toDelete) {
        this.propertyPreferences.remove(toDelete);
    }

    /**
     * Returns an immutable listings list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Listing> getListings() {
        return Collections.unmodifiableList(listings);
    }

    public void addListing(Listing toAdd) {
        this.listings.add(toAdd);
    }

    public void removeListing(Listing toDelete) {
        this.listings.remove(toDelete);
    }

    /**
     * Returns the number of property preferences associated with the tag.
     */
    public int getNumPropertyPreferences() {
        return propertyPreferences.size();
    }

    /**
     * Returns the number of listings associated with the tag.
     */
    public int getNumListings() {
        return listings.size();
    }

    /**
     * Returns the number of times the tag is used in the real estate system.
     */
    public int getNumUsage() {
        return getNumPropertyPreferences() + getNumListings();
    }

    /**
     * Returns the active search tags list as an unmodifiable {@code ObservableMap}.
     *
     * @return the unmodifiable map of ActiveSearchTags.
     */
    public static ObservableMap<String, Tag> getActiveSearchTags() {
        return FXCollections.unmodifiableObservableMap(activeSearchTags);
    }

    /**
     * Replaces the contents of active search tags hashmap with {@code tags}.
     * {@code tags} must not contain duplicate tags.
     *
     * @param tags the replacement list.
     */
    public static void setActiveSearchTags(List<Tag> tags) {
        requireAllNonNull(tags);

        activeSearchTags.clear();
        for (Tag tag : tags) {
            activeSearchTags.put(tag.getTagName(), tag);
        }
    }

    /**
     * Returns true if the tag is active in search.
     */
    public boolean isActive() {
        return activeSearchTags.containsKey(tagName);
    }

    /**
     ** Checks if two tag have the same identity and data fields and associations.
     * This defines a stronger notion of equality between two tags.
     *
     * @param other Object to be compared with.
     * @return true if both tags have the same identity and data fields and associations. false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(tagName);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return new ToStringBuilder(this)
                .add("tag name", tagName)
                .toString();
    }

}
