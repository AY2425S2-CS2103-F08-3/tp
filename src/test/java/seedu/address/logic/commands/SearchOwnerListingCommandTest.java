package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.SearchOwnerListingCommand.MESSAGE_SEARCH_SELLER_PROPERTY_SUCCESS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code SearchOwnerListingCommand}.
 */
public class SearchOwnerListingCommandTest {
    private Model model;
    private Model expectedModel;
    private Person samplePerson;
    private Listing sampleListing;

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
    public void execute_searchOwnerListing_success() {
        // First add the person as an owner
        sampleListing.addOwner(samplePerson);
        samplePerson.addListing(sampleListing);
        model.setListing(sampleListing, sampleListing);
        model.setPerson(samplePerson, samplePerson);

        SearchOwnerListingCommand command = new SearchOwnerListingCommand(Index.fromOneBased(1));

        // Set up expected model state
        expectedModel.resetAllLists();
        expectedModel.updateFilteredListingList(listing -> listing.getOwners().contains(samplePerson));

        String expectedMessage = String.format(MESSAGE_SEARCH_SELLER_PROPERTY_SUCCESS,
                Messages.format(samplePerson));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        SearchOwnerListingCommand command = new SearchOwnerListingCommand(Index.fromOneBased(2));

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                SearchOwnerListingCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_noListingsFound_success() {
        SearchOwnerListingCommand command = new SearchOwnerListingCommand(Index.fromOneBased(1));

        // Set up expected model state
        expectedModel.resetAllLists();
        expectedModel.updateFilteredListingList(listing -> listing.getOwners().contains(samplePerson));

        String expectedMessage = String.format(MESSAGE_SEARCH_SELLER_PROPERTY_SUCCESS,
                Messages.format(samplePerson));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        SearchOwnerListingCommand searchOwnerListingCommand = new SearchOwnerListingCommand(Index.fromOneBased(1));

        // same object -> returns true
        assertTrue(searchOwnerListingCommand.equals(searchOwnerListingCommand));

        // same values -> returns true
        SearchOwnerListingCommand searchOwnerListingCommandCopy =
            new SearchOwnerListingCommand(Index.fromOneBased(1));
        assertTrue(searchOwnerListingCommand.equals(searchOwnerListingCommandCopy));

        // different types -> returns false
        assertFalse(searchOwnerListingCommand.equals(1));

        // null -> returns false
        assertFalse(searchOwnerListingCommand.equals(null));

        // different person index -> returns false
        SearchOwnerListingCommand differentPersonIndexCommand =
            new SearchOwnerListingCommand(Index.fromOneBased(2));
        assertFalse(searchOwnerListingCommand.equals(differentPersonIndexCommand));
    }

    @Test
    public void toStringMethod() {
        SearchOwnerListingCommand searchOwnerListingCommand = new SearchOwnerListingCommand(Index.fromOneBased(1));
        String expected = SearchOwnerListingCommand.class.getCanonicalName()
                + "{targetIndex=" + Index.fromOneBased(1) + "}";
        assertEquals(expected, searchOwnerListingCommand.toString());
    }
}
