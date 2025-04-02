package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.tag.Tag;

public class DeleteTagCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        // Add some tags to the model for testing
        model.addTags(Set.of("Luxury", "Pool"));
    }

    @Test
    public void execute_validTags_success() throws Exception {
        Set<String> tagsToDelete = new HashSet<>();
        tagsToDelete.add("Luxury");
        tagsToDelete.add("Pool");

        DeleteTagCommand deleteCommand = new DeleteTagCommand(tagsToDelete);

        CommandResult result = deleteCommand.execute(model);

        // Create the expected message with the correct format
        Set<Tag> deletedTags = new HashSet<>();
        for (String tagName : tagsToDelete) {
            deletedTags.add(new Tag(tagName, List.of(), List.of()));
        }
        String expectedMessage = String.format(DeleteTagCommand.MESSAGE_DELETE_TAG_SUCCESS,
            Messages.format(deletedTags));
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidTags_throwsCommandException() {
        Set<String> tagsToDelete = new HashSet<>();
        tagsToDelete.add("NonExistentTag");

        DeleteTagCommand deleteCommand = new DeleteTagCommand(tagsToDelete);

        assertThrows(CommandException.class, () -> deleteCommand.execute(model));
    }

    @Test
    public void equals() {
        Set<String> firstTagSet = new HashSet<>();
        firstTagSet.add("Luxury");
        Set<String> secondTagSet = new HashSet<>();
        secondTagSet.add("Pool");

        DeleteTagCommand deleteFirstCommand = new DeleteTagCommand(firstTagSet);
        DeleteTagCommand deleteSecondCommand = new DeleteTagCommand(secondTagSet);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteTagCommand deleteFirstCommandCopy = new DeleteTagCommand(firstTagSet);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different tags -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Set<String> tagsToDelete = new HashSet<>();
        tagsToDelete.add("Luxury");
        tagsToDelete.add("Pool");
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(tagsToDelete);
        String expected = DeleteTagCommand.class.getCanonicalName() + "{tags to delete =" + tagsToDelete + "}";
        assertEquals(expected, deleteTagCommand.toString());
    }
}
