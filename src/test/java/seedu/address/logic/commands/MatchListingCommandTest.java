package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LISTING;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LISTING;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;
import seedu.address.testutil.ListingBuilder;
import seedu.address.testutil.PriceRangeBuilder;
import seedu.address.testutil.TagBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code MatchListingCommand}.
 */
public class MatchListingCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // Register tags in both models
        Set<String> tagNames = Set.of("modern", "luxury");
        model.addTags(tagNames);
        expectedModel.addTags(tagNames);
    }

    /**
     * Custom assertion for MatchListingCommand success that doesn't rely on model equality
     */
    private void assertMatchCommandSuccess(MatchListingCommand command, Model model, String expectedMessage)
            throws CommandException {
        CommandResult result = command.execute(model);
        assertEquals(new CommandResult(expectedMessage), result);
        // Verify the filtered person list is not empty (matching worked)
        assertFalse(model.getFilteredPersonList().isEmpty());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        // Get a consistent tag reference
        TagRegistry tagRegistry = TagRegistry.of();
        Tag tag = tagRegistry.get("MODERN");

        // Create a price range for the listing
        PriceRange listingPriceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("150000")
                .withUpperBoundPrice("180000")
                .build();

        // Create a listing to match with explicit tag reference
        Listing listing = new ListingBuilder()
                .withPostalCode("123456")
                .withUnitNumber("12-123")
                .withPriceRange(listingPriceRange)
                .withTags(Collections.singleton(tag))
                .build();
        model.addListing(listing);

        // Create a price range for preference
        PriceRange prefPriceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();

        // Add a person with matching preferences using same tag reference
        Person person = model.getFilteredPersonList().get(0);
        PropertyPreference preference = new PropertyPreference(
                prefPriceRange,
                Collections.singleton(tag),
                person);
        person.addPropertyPreference(preference);

        // Get the index of the listing
        Index listingIndex = INDEX_FIRST_LISTING;

        MatchListingCommand matchCommand = new MatchListingCommand(listingIndex);
        String expectedMessage = String.format(MatchListingCommand.MESSAGE_MATCH_LISTING_SUCCESS,
                Messages.format(listing));

        // Use our custom assertion that doesn't rely on model equality
        assertMatchCommandSuccess(matchCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getSortedFilteredListingList().size() + 1);
        MatchListingCommand matchCommand = new MatchListingCommand(outOfBoundIndex);

        assertCommandFailure(matchCommand, model, Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        MatchListingCommand matchFirstCommand = new MatchListingCommand(INDEX_FIRST_LISTING);
        MatchListingCommand matchSecondCommand = new MatchListingCommand(INDEX_SECOND_LISTING);

        // same object -> returns true
        assertTrue(matchFirstCommand.equals(matchFirstCommand));

        // same values -> returns true
        MatchListingCommand matchFirstCommandCopy = new MatchListingCommand(INDEX_FIRST_LISTING);
        assertTrue(matchFirstCommand.equals(matchFirstCommandCopy));

        // different types -> returns false
        assertFalse(matchFirstCommand.equals(1));

        // null -> returns false
        assertFalse(matchFirstCommand.equals(null));

        // different listing -> returns false
        assertFalse(matchFirstCommand.equals(matchSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index listingIndex = Index.fromOneBased(1);
        MatchListingCommand matchCommand = new MatchListingCommand(listingIndex);

        String expected = MatchListingCommand.class.getCanonicalName() + "{targetIndex=" + listingIndex + "}";
        assertEquals(expected, matchCommand.toString());
    }

    /**
     * Creates a test listing for testing.
     */
    private Listing createTestListing() {
        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("150000")
                .withUpperBoundPrice("180000")
                .build();

        Tag tag = new TagBuilder().withTagName("modern").build();

        return new ListingBuilder()
                .withPostalCode("123456")
                .withUnitNumber("12-123")
                .withPriceRange(priceRange)
                .withTags(Collections.singleton(tag))
                .build();
    }

    /**
     * Creates a property preference that matches the given listing.
     */
    private PropertyPreference createMatchingPreference(Person person, Listing listing) {
        Tag tag = listing.getTags().iterator().next(); // Get the first tag from the listing

        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();

        return new PropertyPreference(priceRange, Collections.singleton(tag), person);
    }
}
