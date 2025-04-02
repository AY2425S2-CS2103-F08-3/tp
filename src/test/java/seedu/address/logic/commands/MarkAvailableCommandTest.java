package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.MarkAvailableCommand.MESSAGE_MARK_AVAILABLE_SUCCESS;

import java.util.ArrayList;
import java.util.HashSet;

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
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code MarkAvailableCommand}.
 */
public class MarkAvailableCommandTest {
    private Model model;
    private Model expectedModel;
    private Listing sampleListing;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Create a sample listing
        sampleListing = Listing.of(
                new PostalCode("123456"),
                null, // unit number
                new HouseNumber("123"),
                new PriceRange(new Price("100000"), new Price("200000")),
                new PropertyName("Sunny Villa"),
                new HashSet<>(), // empty tags
                new ArrayList<>(), // empty owners
                false // initially unavailable
        );

        // Add listing to model
        model.addListing(sampleListing);
        model.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);

        // Add listing to expected model
        expectedModel.addListing(sampleListing);
        expectedModel.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);
    }

    @Test
    public void execute_markAvailable_success() {
        // Create a copy of the listing that will be marked as available
        Listing markedAvailableListing = Listing.of(
                new PostalCode("123456"),
                null,
                new HouseNumber("123"),
                new PriceRange(new Price("100000"), new Price("200000")),
                new PropertyName("Sunny Villa"),
                new HashSet<>(),
                new ArrayList<>(),
                true // marked as available
        );

        MarkAvailableCommand command = new MarkAvailableCommand(Index.fromOneBased(1));

        // Set up expected model state
        expectedModel.setListing(sampleListing, markedAvailableListing);
        expectedModel.resetAllLists();

        String expectedMessage = String.format(MESSAGE_MARK_AVAILABLE_SUCCESS,
                Messages.format(true, markedAvailableListing));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        MarkAvailableCommand command = new MarkAvailableCommand(Index.fromOneBased(2));

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX,
                MarkAvailableCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        MarkAvailableCommand markAvailableCommand = new MarkAvailableCommand(Index.fromOneBased(1));

        // same object -> returns true
        assertTrue(markAvailableCommand.equals(markAvailableCommand));

        // same values -> returns true
        MarkAvailableCommand markAvailableCommandCopy = new MarkAvailableCommand(Index.fromOneBased(1));
        assertTrue(markAvailableCommand.equals(markAvailableCommandCopy));

        // different types -> returns false
        assertFalse(markAvailableCommand.equals(1));

        // null -> returns false
        assertFalse(markAvailableCommand.equals(null));

        // different index -> returns false
        MarkAvailableCommand differentIndexCommand = new MarkAvailableCommand(Index.fromOneBased(2));
        assertFalse(markAvailableCommand.equals(differentIndexCommand));
    }

    @Test
    public void toStringMethod() {
        MarkAvailableCommand markAvailableCommand = new MarkAvailableCommand(Index.fromOneBased(1));
        String expected = MarkAvailableCommand.class.getCanonicalName()
                + "{targetIndex=" + Index.fromOneBased(1) + "}";
        assertEquals(expected, markAvailableCommand.toString());
    }
}
