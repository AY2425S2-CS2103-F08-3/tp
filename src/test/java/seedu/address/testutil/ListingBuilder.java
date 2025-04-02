package seedu.address.testutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.model.listing.HouseNumber;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.PostalCode;
import seedu.address.model.listing.PropertyName;
import seedu.address.model.listing.UnitNumber;
import seedu.address.model.person.Person;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building Listing objects.
 */
public class ListingBuilder {

    public static final String DEFAULT_POSTAL_CODE = "123456";
    public static final String DEFAULT_UNIT_NUMBER = "12-123";
    public static final String DEFAULT_HOUSE_NUMBER = "12A";
    public static final String DEFAULT_PROPERTY_NAME = "Test Property";
    public static final boolean DEFAULT_AVAILABILITY = true;
    public static final String DEFAULT_LOWER_PRICE = "100000";
    public static final String DEFAULT_UPPER_PRICE = "200000";

    private PostalCode postalCode;
    private UnitNumber unitNumber;
    private HouseNumber houseNumber;
    private PriceRange priceRange;
    private PropertyName propertyName;
    private Set<Tag> tags;
    private List<Person> owners;
    private boolean isAvailable;

    /**
     * Creates a {@code ListingBuilder} with default details.
     * This creates a unit-based listing by default (no house number).
     */
    public ListingBuilder() {
        postalCode = new PostalCode(DEFAULT_POSTAL_CODE);
        unitNumber = new UnitNumber(DEFAULT_UNIT_NUMBER);
        houseNumber = null;
        priceRange = new PriceRange(new Price(DEFAULT_LOWER_PRICE), new Price(DEFAULT_UPPER_PRICE));
        propertyName = null;
        tags = new HashSet<>();
        owners = new ArrayList<>();
        isAvailable = DEFAULT_AVAILABILITY;
    }

    /**
     * Initializes the ListingBuilder with the data of {@code listingToCopy}.
     */
    public ListingBuilder(Listing listingToCopy) {
        postalCode = listingToCopy.getPostalCode();
        unitNumber = listingToCopy.getUnitNumber();
        houseNumber = listingToCopy.getHouseNumber();
        priceRange = listingToCopy.getPriceRange();
        propertyName = listingToCopy.getPropertyName();
        tags = new HashSet<>(listingToCopy.getTags());
        owners = new ArrayList<>(listingToCopy.getOwners());
        isAvailable = listingToCopy.getAvailability();
    }

    /**
     * Sets the {@code PostalCode} of the {@code Listing} that we are building.
     */
    public ListingBuilder withPostalCode(String postalCode) {
        this.postalCode = new PostalCode(postalCode);
        return this;
    }

    /**
     * Sets the {@code UnitNumber} of the {@code Listing} that we are building.
     * Note: Setting this will set houseNumber to null as a Listing can't have both.
     */
    public ListingBuilder withUnitNumber(String unitNumber) {
        this.unitNumber = new UnitNumber(unitNumber);
        this.houseNumber = null; // A listing can't have both unit number and house number
        return this;
    }

    /**
     * Sets the {@code HouseNumber} of the {@code Listing} that we are building.
     * Note: Setting this will set unitNumber to null as a Listing can't have both.
     */
    public ListingBuilder withHouseNumber(String houseNumber) {
        this.houseNumber = new HouseNumber(houseNumber);
        this.unitNumber = null; // A listing can't have both unit number and house number
        return this;
    }

    /**
     * Sets the {@code PriceRange} of the {@code Listing} that we are building.
     */
    public ListingBuilder withPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
        return this;
    }

    /**
     * Sets the {@code PropertyName} of the {@code Listing} that we are building.
     */
    public ListingBuilder withPropertyName(String propertyName) {
        this.propertyName = new PropertyName(propertyName);
        return this;
    }

    /**
     * Sets the availability of the {@code Listing} that we are building.
     */
    public ListingBuilder withAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
        return this;
    }

    /**
     * Sets the {@code tags} of the {@code Listing} that we are building.
     */
    public ListingBuilder withTags(Set<Tag> tags) {
        this.tags = new HashSet<>(tags);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Listing} that we are building.
     */
    public ListingBuilder withTags(String... tagNames) {
        this.tags = new HashSet<>();
        for (String tagName : tagNames) {
            this.tags.add(new Tag(tagName, new ArrayList<>(), new ArrayList<>()));
        }
        return this;
    }

    /**
     * Sets the {@code owners} of the {@code Listing} that we are building.
     */
    public ListingBuilder withOwners(List<Person> owners) {
        this.owners = new ArrayList<>(owners);
        return this;
    }

    /**
     * Creates a convenience method to build a listing with just the address details.
     * Uses default values for other fields.
     */
    public ListingBuilder withAddress(String address) {
        // Parse the address into postal code and unit/house number
        // For testing purposes, we'll create a unit-based listing
        this.postalCode = new PostalCode(DEFAULT_POSTAL_CODE);
        this.unitNumber = new UnitNumber(DEFAULT_UNIT_NUMBER);
        this.houseNumber = null;
        return this;
    }


    /**
     * Builds a {@code Listing} with the current attributes.
     */
    public Listing build() {
        // Use the Listing.of static method to create the appropriate Listing object
        return Listing.of(postalCode, unitNumber, houseNumber, priceRange, propertyName,
                tags, owners, isAvailable);
    }
}
