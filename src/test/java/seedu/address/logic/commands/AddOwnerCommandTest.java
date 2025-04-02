package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

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
 * {@code AddOwnerCommand}.
 */
public class AddOwnerCommandTest {
    private Model model;
    private Listing sampleListing;
    private Person samplePerson;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());

        // Create a sample person
        samplePerson = new Person(
                new Name("John Doe"),
                new Phone("98765432"),
                new Email("johndoe@example.com"),
                List.of(), // Empty property preferences
                List.of() // Empty listings
        );

        // Create a sample listing without owners
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
    }

    @Test
    public void execute_addOwner_success() {
        AddOwnerCommand command = new AddOwnerCommand(Index.fromOneBased(1), Index.fromOneBased(1));

        // Create expected model state
        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Create copies of the person and listing for the expected model
        Person expectedPerson = new Person(
                new Name("John Doe"),
                new Phone("98765432"),
                new Email("johndoe@example.com"),
                List.of(), // Empty property preferences
                List.of() // Empty listings
        );

        Listing expectedListing = Listing.of(
                new PostalCode("123456"),
                null,
                new HouseNumber("123"),
                new PriceRange(new Price("100000"), new Price("200000")),
                new PropertyName("Sunny Villa"),
                new HashSet<>(),
                new ArrayList<>(),
                true
        );

        // Add person and listing to expected model
        expectedModel.addPerson(expectedPerson);
        expectedModel.addListing(expectedListing);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);

        // Add owner to listing in expected model
        expectedListing.addOwner(expectedPerson);
        expectedPerson.addListing(expectedListing);
        expectedModel.setListing(expectedListing, expectedListing);
        expectedModel.setPerson(expectedPerson, expectedPerson);
        expectedModel.resetAllLists();

        String expectedMessage = String.format(AddOwnerCommand.MESSAGE_SUCCESS,
                Messages.format(expectedPerson, expectedListing));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        AddOwnerCommand command = new AddOwnerCommand(Index.fromOneBased(2), Index.fromOneBased(1));

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                AddOwnerCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidListingIndex_throwsCommandException() {
        AddOwnerCommand command = new AddOwnerCommand(Index.fromOneBased(1), Index.fromOneBased(2));

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX,
                AddOwnerCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_duplicateOwner_throwsCommandException() {
        // First add the person as an owner
        sampleListing.addOwner(samplePerson);
        samplePerson.addListing(sampleListing);
        model.setListing(sampleListing, sampleListing);
        model.setPerson(samplePerson, samplePerson);

        // Try to add the same person as owner again
        AddOwnerCommand command = new AddOwnerCommand(Index.fromOneBased(1), Index.fromOneBased(1));

        assertCommandFailure(command, model, String.format(AddOwnerCommand.MESSAGE_OWNER_ALREADY_IN_LISTING,
                AddOwnerCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        AddOwnerCommand addOwnerCommand = new AddOwnerCommand(Index.fromOneBased(1), Index.fromOneBased(1));

        // same object -> returns true
        assertTrue(addOwnerCommand.equals(addOwnerCommand));

        // same values -> returns true
        AddOwnerCommand addOwnerCommandCopy = new AddOwnerCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        assertTrue(addOwnerCommand.equals(addOwnerCommandCopy));

        // different types -> returns false
        assertFalse(addOwnerCommand.equals(1));

        // null -> returns false
        assertFalse(addOwnerCommand.equals(null));

        // different person index -> returns false
        AddOwnerCommand differentPersonIndexCommand = new AddOwnerCommand(Index.fromOneBased(2), Index.fromOneBased(1));
        assertFalse(addOwnerCommand.equals(differentPersonIndexCommand));

        // different listing index -> returns false
        AddOwnerCommand differentListingIndexCommand =
            new AddOwnerCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        assertFalse(addOwnerCommand.equals(differentListingIndexCommand));
    }

    @Test
    public void toStringMethod() {
        AddOwnerCommand addOwnerCommand = new AddOwnerCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        String expected = AddOwnerCommand.class.getCanonicalName() + "{}";
        assertEquals(expected, addOwnerCommand.toString());
    }
}
