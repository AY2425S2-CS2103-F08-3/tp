package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.MatchListingCommand.MESSAGE_MATCH_LISTING_SUCCESS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
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
import seedu.address.model.search.SearchType;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code MatchListingCommand}.
 */
public class MatchListingCommandTest {
    private Model model;
    private Model expectedModel;
    private Person samplePerson;
    private Listing sampleListing;
    private PropertyPreference samplePreference;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Create a sample person
        samplePerson = new Person(
                new Name("John Doe"),
                new Phone("98765432"),
                new Email("johndoe@example.com"),
                List.of(), // Empty property preferences
                List.of() // Empty listings
        );

        // Create a sample listing
        sampleListing = Listing.of(
                new PostalCode("123456"),
                null, // unit number
                new HouseNumber("123"),
                new PriceRange(new Price("100000"), new Price("200000")),
                new PropertyName("Sunny Villa"),
                new HashSet<>(), // empty tags
                new ArrayList<>(), // empty owners
                true
        );

        // Create a sample property preference
        samplePreference = new PropertyPreference(
                new PriceRange(new Price("150000"), new Price("250000")),
                new HashSet<>(), // empty tags
                samplePerson
        );

        // Add person and listing to model
        model.addPerson(samplePerson);
        model.addListing(sampleListing);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);

        // Add person and listing to expected model
        expectedModel.addPerson(samplePerson);
        expectedModel.addListing(sampleListing);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);
    }

    @Test
    public void execute_matchListing_success() {
        // Add property preference to person
        samplePerson.addPropertyPreference(samplePreference);
        model.setPerson(samplePerson, samplePerson);

        MatchListingCommand command = new MatchListingCommand(Index.fromOneBased(1));

        // Set up expected model state
        expectedModel.resetAllLists();
        expectedModel.setSearch(new HashSet<>(sampleListing.getTags()),
                sampleListing.getPriceRange(),
                SearchType.PERSON,
                Model.PREDICATE_SHOW_ALL_PROPERTY_PREFERENCES);

        String expectedMessage = String.format(MESSAGE_MATCH_LISTING_SUCCESS,
                Messages.format(sampleListing));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidListingIndex_throwsCommandException() {
        MatchListingCommand command = new MatchListingCommand(Index.fromOneBased(2));

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX,
                MatchListingCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_matchListingWithTags_success() {
        // Create tags
        Set<String> tagNames = new HashSet<>();
        tagNames.add("pet-friendly");
        tagNames.add("pool");
        model.addTags(tagNames);

        Tag tag1 = model.getTag("pet-friendly");
        Tag tag2 = model.getTag("pool");

        // Add tags to listing
        sampleListing.addTag(tag1);
        sampleListing.addTag(tag2);
        model.setListing(sampleListing, sampleListing);

        // Add matching tags to preference
        samplePreference.addTag(tag1);
        samplePerson.addPropertyPreference(samplePreference);
        model.setPerson(samplePerson, samplePerson);

        MatchListingCommand command = new MatchListingCommand(Index.fromOneBased(1));

        // Set up expected model state
        expectedModel.resetAllLists();
        expectedModel.setSearch(new HashSet<>(sampleListing.getTags()),
                sampleListing.getPriceRange(),
                SearchType.PERSON,
                Model.PREDICATE_SHOW_ALL_PROPERTY_PREFERENCES);

        String expectedMessage = String.format(MESSAGE_MATCH_LISTING_SUCCESS,
                Messages.format(sampleListing));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        MatchListingCommand matchListingCommand = new MatchListingCommand(Index.fromOneBased(1));

        // same object -> returns true
        assertTrue(matchListingCommand.equals(matchListingCommand));

        // same values -> returns true
        MatchListingCommand matchListingCommandCopy = new MatchListingCommand(Index.fromOneBased(1));
        assertTrue(matchListingCommand.equals(matchListingCommandCopy));

        // different types -> returns false
        assertFalse(matchListingCommand.equals(1));

        // null -> returns false
        assertFalse(matchListingCommand.equals(null));

        // different listing index -> returns false
        MatchListingCommand differentListingIndexCommand = new MatchListingCommand(Index.fromOneBased(2));
        assertFalse(matchListingCommand.equals(differentListingIndexCommand));
    }

    @Test
    public void toStringMethod() {
        MatchListingCommand matchListingCommand = new MatchListingCommand(Index.fromOneBased(1));
        String expected = MatchListingCommand.class.getCanonicalName()
                + "{targetIndex=" + Index.fromOneBased(1) + "}";
        assertEquals(expected, matchListingCommand.toString());
    }
}
