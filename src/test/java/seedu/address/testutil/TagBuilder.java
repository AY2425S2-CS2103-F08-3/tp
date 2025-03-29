package seedu.address.testutil;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.listing.Listing;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;

/**
 * A utility class to help with building Tag objects.
 */
public class TagBuilder {

    private String tagName;
    private List<PropertyPreference> propertyPreferences;
    private List<Listing> listings;

    /**
     * Creates a {@code TagBuilder} with default values.
     */
    public TagBuilder() {
        tagName = "DEFAULT";
        propertyPreferences = new ArrayList<>();
        listings = new ArrayList<>();
    }

    /**
     * Initializes the TagBuilder with the data of {@code tagToCopy}.
     */
    public TagBuilder(Tag tagToCopy) {
        tagName = tagToCopy.getTagName();
        propertyPreferences = new ArrayList<>(tagToCopy.getPropertyPreferences());
        listings = new ArrayList<>(tagToCopy.getListings());
    }

    /**
     * Sets the {@code tagName} of the {@code Tag} that we are building.
     */
    public TagBuilder withTagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    /**
     * Sets the {@code propertyPreferences} of the {@code Tag} that we are building.
     */
    public TagBuilder withPropertyPreferences(List<PropertyPreference> propertyPreferences) {
        this.propertyPreferences = new ArrayList<>(propertyPreferences);
        return this;
    }

    /**
     * Sets the {@code listings} of the {@code Tag} that we are building.
     */
    public TagBuilder withListings(List<Listing> listings) {
        this.listings = new ArrayList<>(listings);
        return this;
    }

    /**
     * Builds a {@code Tag} with the current attributes and ensures it's registered.
     * This will either get an existing tag with the same name or create and register a new one.
     */
    public Tag build() {
        TagRegistry tagRegistry = TagRegistry.of();

        // Convert to uppercase for consistency with Tag class
        String upperTagName = tagName.toUpperCase();

        // First check if tag already exists in registry
        try {
            return tagRegistry.get(upperTagName);
        } catch (Exception e) {
            // Tag doesn't exist, create and register it
            Tag newTag = new Tag(tagName, propertyPreferences, listings);
            tagRegistry.add(newTag);
            return newTag;
        }
    }
}
