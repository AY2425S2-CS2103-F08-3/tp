package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ListListingCommandTest {

    @Test
    public void execute_listListings_success() {
        Model model = new ModelManager(new seedu.address.model.AddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(new seedu.address.model.AddressBook(), new UserPrefs());
        ListListingCommand listListingCommand = new ListListingCommand();

        // Execute command
        CommandResult commandResult = listListingCommand.execute(model);

        // Verify result
        assertEquals(ListListingCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
        assertEquals(expectedModel.getFilteredListingList(), model.getFilteredListingList());
    }

    @Test
    public void equals() {
        ListListingCommand listListingCommand = new ListListingCommand();

        // same object -> returns true
        assertTrue(listListingCommand.equals(listListingCommand));

        // same type -> returns true
        assertTrue(listListingCommand.equals(new ListListingCommand()));

        // different types -> returns false
        assertFalse(listListingCommand.equals(1));

        // null -> returns false
        assertFalse(listListingCommand.equals(null));
    }

    @Test
    public void toStringMethod() {
        ListListingCommand listListingCommand = new ListListingCommand();
        String expected = ListListingCommand.class.getCanonicalName() + "{}";
        assertEquals(expected, listListingCommand.toString());
    }
}
