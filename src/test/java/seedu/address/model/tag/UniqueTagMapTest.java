package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.listing.HouseNumber;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.PostalCode;
import seedu.address.model.listing.PropertyName;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.exceptions.DuplicateTagException;
import seedu.address.model.tag.exceptions.TagNotFoundException;

public class UniqueTagMapTest {

    private UniqueTagMap uniqueTagMap;
    private Tag tag1;
    private Tag tag2;

    @BeforeEach
    public void setUp() {
        uniqueTagMap = new UniqueTagMap();
        tag1 = new Tag("HDB", new ArrayList<>(), new ArrayList<>());
        tag2 = new Tag("CONDO", new ArrayList<>(), new ArrayList<>());

        if (!uniqueTagMap.contains(tag1)) {
            uniqueTagMap.add(tag1);
        }
        if (!uniqueTagMap.contains(tag2)) {
            uniqueTagMap.add(tag2);
        }

        // Reset TagRegistry for consistent test results
        uniqueTagMap.setTags(new ArrayList<>());
    }

    @Test
    public void addTag_success() {
        uniqueTagMap.add(tag1);
        assertTrue(uniqueTagMap.contains(tag1));
    }

    @Test
    public void addTag_duplicateTag_throwsDuplicateTagException() {
        uniqueTagMap.add(tag1);
        assertThrows(DuplicateTagException.class, () -> uniqueTagMap.add(tag1));
    }

    @Test
    public void getTag_existingTag_success() {
        uniqueTagMap.add(tag1);
        assertEquals(tag1, uniqueTagMap.get("HDB"));
    }

    @Test
    public void getTag_nonExistentTag_throwsTagNotFoundException() {
        assertThrows(TagNotFoundException.class, () -> uniqueTagMap.get("LANDED"));
    }

    @Test
    public void removeTag_existingTag_success() {
        uniqueTagMap.add(tag1);
        uniqueTagMap.remove(tag1);
        assertFalse(uniqueTagMap.contains(tag1));
    }

    @Test
    public void removeTag_nonExistentTag_throwsTagNotFoundException() {
        assertThrows(TagNotFoundException.class, () -> uniqueTagMap.remove(tag1));
    }

    @Test
    public void setTag_replaceTag_success() {
        uniqueTagMap.add(tag1);
        Tag editedTag = new Tag("HDB", new ArrayList<>(), new ArrayList<>());
        uniqueTagMap.setTag(tag1, editedTag);
        assertTrue(uniqueTagMap.contains(editedTag));
    }

    @Test
    public void setTag_replaceNonExistentTag_throwsTagNotFoundException() {
        Tag editedTag = new Tag("HDB", new ArrayList<>(), new ArrayList<>());
        assertThrows(TagNotFoundException.class, () -> uniqueTagMap.setTag(tag1, editedTag));
    }

    @Test
    public void setTag_duplicateReplacement_throwsDuplicateTagException() {
        uniqueTagMap.add(tag1);
        uniqueTagMap.add(tag2);
        assertThrows(DuplicateTagException.class, () -> uniqueTagMap.setTag(tag1, tag2));
    }

    @Test
    public void addPropertyPreferenceToTag_success() {
        uniqueTagMap.add(tag1);

        Price lowPrice = new Price("500000");
        Price highPrice = new Price("800000");
        Person person = new Person(new Name("John Doe"), new Phone("98765432"), new Email("johndoe@example.com"),
                new ArrayList<>(), new ArrayList<>());
        PropertyPreference preference = new PropertyPreference(new PriceRange(lowPrice, highPrice), new HashSet<>(),
                person);

        tag1.addPropertyPreference(preference);
        assertTrue(uniqueTagMap.get("HDB").getPropertyPreferences().contains(preference));
    }

    @Test
    public void addListingToTag_success() {
        uniqueTagMap.add(tag1);

        // Correct Listing constructor based on expected parameters
        PostalCode postalCode = new PostalCode("678900");
        HouseNumber houseNumber = new HouseNumber("123");
        PriceRange priceRange = new PriceRange(new Price("500000"), new Price("800000"));
        PropertyName propertyName = new PropertyName("HDB at Bukit Timah");
        Set<Tag> tags = new HashSet<>();
        List<Person> interestedPersons = new ArrayList<>();

        Listing listing = new Listing(postalCode, houseNumber, priceRange, propertyName, tags, interestedPersons, true);

        tag1.addListing(listing);
        assertTrue(uniqueTagMap.get("HDB").getListings().contains(listing));
    }
}
