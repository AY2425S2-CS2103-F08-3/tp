package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

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
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteListingCommand}.
 */
public class DeleteListingCommandTest {
    private Model model;
    private Listing sampleListing;
    private Person samplePerson;
    private Tag sampleTag;

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

        // Create a sample tag
        model.addTags(Set.of("Luxury"));
        sampleTag = model.getTag("Luxury");

        // Create a sample listing with the person as an owner and the tag
        sampleListing = Listing.of(
                new PostalCode("123456"),
                null, // unit number
                new HouseNumber("123"),
                new PriceRange(new Price("100000"), new Price("200000")),
                new PropertyName("Sunny Villa"),
                new HashSet<>(Set.of(sampleTag)), // with tag
                new ArrayList<>(List.of(samplePerson)), // with owner
                true
        );

        // Add person and listing to model
        model.addPerson(samplePerson);
        model.addListing(sampleListing);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);
    }

    @Test
    public void execute_deleteListing_success() {
        DeleteListingCommand command = new DeleteListingCommand(Index.fromOneBased(1));

        // Create expected model state
        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Create copies of the person and tag for the expected model
        Person expectedPerson = new Person(
                new Name("John Doe"),
                new Phone("98765432"),
                new Email("johndoe@example.com"),
                List.of(), // Empty property preferences
                List.of() // Empty listings
        );

        // Add person to expected model
        expectedModel.addPerson(expectedPerson);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        String expectedMessage = String.format(DeleteListingCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(sampleListing));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidListingIndex_throwsCommandException() {
        DeleteListingCommand command = new DeleteListingCommand(Index.fromOneBased(2));

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX,
                DeleteListingCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        DeleteListingCommand deleteListingCommand = new DeleteListingCommand(Index.fromOneBased(1));

        // same object -> returns true
        assertTrue(deleteListingCommand.equals(deleteListingCommand));

        // same values -> returns true
        DeleteListingCommand deleteListingCommandCopy = new DeleteListingCommand(Index.fromOneBased(1));
        assertTrue(deleteListingCommand.equals(deleteListingCommandCopy));

        // different types -> returns false
        assertFalse(deleteListingCommand.equals(1));

        // null -> returns false
        assertFalse(deleteListingCommand.equals(null));

        // different index -> returns false
        DeleteListingCommand differentIndexCommand = new DeleteListingCommand(Index.fromOneBased(2));
        assertFalse(deleteListingCommand.equals(differentIndexCommand));
    }

    @Test
    public void toStringMethod() {
        DeleteListingCommand deleteListingCommand = new DeleteListingCommand(Index.fromOneBased(1));
        String expected = DeleteListingCommand.class.getCanonicalName() + "{targetIndex=" + Index.fromOneBased(1) + "}";
        assertEquals(expected, deleteListingCommand.toString());
    }
}
