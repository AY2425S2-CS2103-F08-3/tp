package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.listing.Listing;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;

/**
 * A utility class containing a list of {@code Listing} objects to be used in tests.
 */
public class TypicalListings {

    public static final Listing LISTING_1 = new ListingBuilder()
            .withPropertyName("Modern Apartment")
            .withPriceRange(new PriceRange(new Price("100000"), new Price("200000")))
            .withTags("modern", "luxury")
            .build();

    public static final Listing LISTING_2 = new ListingBuilder()
            .withPropertyName("Cozy House")
            .withPriceRange(new PriceRange(new Price("300000"), new Price("400000")))
            .withTags("cozy", "family")
            .build();

    public static final Listing LISTING_3 = new ListingBuilder()
            .withPropertyName("Luxury Villa")
            .withPriceRange(new PriceRange(new Price("500000"), new Price("600000")))
            .withTags("luxury", "modern")
            .build();

    private TypicalListings() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical listings.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Listing listing : getTypicalListings()) {
            ab.addListing(listing);
        }
        return ab;
    }

    public static List<Listing> getTypicalListings() {
        return new ArrayList<>(Arrays.asList(LISTING_1, LISTING_2, LISTING_3));
    }
}
