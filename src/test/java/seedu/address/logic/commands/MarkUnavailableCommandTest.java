package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.MarkUnavailableCommand.MESSAGE_MARK_UNAVAILABLE_SUCCESS;

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
 * {@code MarkUnavailableCommand}.
 */
public class MarkUnavailableCommandTest {
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
                true // initially available
        );

        // Add listing to model
        model.addListing(sampleListing);
        model.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);

        // Add listing to expected model
        expectedModel.addListing(sampleListing);
        expectedModel.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);
    }

    @Test
    public void execute_markUnavailable_success() {
        // Create a copy of the listing that will be marked as unavailable
        Listing markedUnavailableListing = Listing.of(
                new PostalCode("123456"),
                null,
                new HouseNumber("123"),
                new PriceRange(new Price("100000"), new Price("200000")),
                new PropertyName("Sunny Villa"),
                new HashSet<>(),
                new ArrayList<>(),
                false // marked as unavailable
        );

        MarkUnavailableCommand command = new MarkUnavailableCommand(Index.fromOneBased(1));

        // Set up expected model state
        expectedModel.setListing(sampleListing, markedUnavailableListing);
        expectedModel.resetAllLists();

        String expectedMessage = String.format(MESSAGE_MARK_UNAVAILABLE_SUCCESS,
                Messages.format(false, markedUnavailableListing));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        MarkUnavailableCommand command = new MarkUnavailableCommand(Index.fromOneBased(2));

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX,
                MarkUnavailableCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        MarkUnavailableCommand markUnavailableCommand = new MarkUnavailableCommand(Index.fromOneBased(1));

        // same object -> returns true
        assertTrue(markUnavailableCommand.equals(markUnavailableCommand));

        // same values -> returns true
        MarkUnavailableCommand markUnavailableCommandCopy = new MarkUnavailableCommand(Index.fromOneBased(1));
        assertTrue(markUnavailableCommand.equals(markUnavailableCommandCopy));

        // different types -> returns false
        assertFalse(markUnavailableCommand.equals(1));

        // null -> returns false
        assertFalse(markUnavailableCommand.equals(null));

        // different index -> returns false
        MarkUnavailableCommand differentIndexCommand = new MarkUnavailableCommand(Index.fromOneBased(2));
        assertFalse(markUnavailableCommand.equals(differentIndexCommand));
    }

    @Test
    public void toStringMethod() {
        MarkUnavailableCommand markUnavailableCommand = new MarkUnavailableCommand(Index.fromOneBased(1));
        String expected = MarkUnavailableCommand.class.getCanonicalName()
                + "{targetIndex=" + Index.fromOneBased(1) + "}";
        assertEquals(expected, markUnavailableCommand.toString());
    }
}
