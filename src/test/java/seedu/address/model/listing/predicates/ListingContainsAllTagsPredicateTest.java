package seedu.address.model.listing.predicates;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.listing.Listing;
import seedu.address.model.listing.PostalCode;
import seedu.address.model.listing.UnitNumber;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

public class ListingContainsAllTagsPredicateTest {

    private final Listing sampleListing = Listing.of(
            new PostalCode("123456"),
            new UnitNumber("10-12"),
            null,
            new PriceRange(new Price("500000"), new Price("700000")),
            null,
            Set.of(
                    new Tag("Pool", List.of(), List.of()),
                    new Tag("Pet-Friendly", List.of(), List.of())
            ),
            List.of(new Person(
                    new Name("John Doe"),
                    new Phone("98765432"),
                    new Email("john@example.com"),
                    List.of(),
                    List.of()
            ))
    );

    @Test
    public void test_containsAllTags_true() {
        ListingContainsAllTagsPredicate predicate = new ListingContainsAllTagsPredicate(Set.of("pool", "pet-friendly"));
        assertTrue(predicate.test(sampleListing));
    }

    @Test
    public void test_partialTags_false() {
        ListingContainsAllTagsPredicate predicate = new ListingContainsAllTagsPredicate(Set.of("pool", "garden"));
        assertFalse(predicate.test(sampleListing));
    }

    @Test
    public void test_tagCaseInsensitive_true() {
        ListingContainsAllTagsPredicate predicate = new ListingContainsAllTagsPredicate(Set.of("POOL", "PET-friendly"));
        assertTrue(predicate.test(sampleListing));
    }

    @Test
    public void test_emptyTagSet_true() {
        ListingContainsAllTagsPredicate predicate = new ListingContainsAllTagsPredicate(Set.of());
        // Conventionally this should return true (no filter condition),
        assertTrue(predicate.test(sampleListing));
    }
}
