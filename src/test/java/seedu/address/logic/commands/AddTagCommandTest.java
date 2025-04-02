package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code AddTagCommand}.
 */
public class AddTagCommandTest {
    private Model model = new ModelManager(new AddressBook(), new UserPrefs());

    @Test
    public void execute_addNewTags_success() {
        Set<String> newTags = Set.of("modern", "luxury");
        AddTagCommand command = new AddTagCommand(newTags);

        // Create expected model state
        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
        Set<Tag> expectedTagSet = new HashSet<>();
        for (String tagName : newTags) {
            expectedTagSet.add(new Tag(tagName, new ArrayList<>(), new ArrayList<>()));
        }
        expectedModel.addTags(newTags);

        String expectedMessage = String.format(AddTagCommand.MESSAGE_SUCCESS, Messages.format(expectedTagSet));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateTags_throwsCommandException() {
        // Add existing tags to model
        Set<String> existingTags = Set.of("modern");
        model.addTags(existingTags);

        // Try to add the same tag again
        AddTagCommand command = new AddTagCommand(existingTags);

        assertCommandFailure(command, model, String.format(AddTagCommand.MESSAGE_DUPLICATE_TAGS,
                AddTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        Set<String> tags = Set.of("modern", "luxury");
        AddTagCommand addTagCommand = new AddTagCommand(tags);

        // same object -> returns true
        assertTrue(addTagCommand.equals(addTagCommand));

        // same values -> returns true
        AddTagCommand addTagCommandCopy = new AddTagCommand(tags);
        assertTrue(addTagCommand.equals(addTagCommandCopy));

        // different types -> returns false
        assertFalse(addTagCommand.equals(1));

        // null -> returns false
        assertFalse(addTagCommand.equals(null));

        // different tags -> returns false
        AddTagCommand differentTagsCommand = new AddTagCommand(Set.of("spacious"));
        assertFalse(addTagCommand.equals(differentTagsCommand));
    }

    @Test
    public void toStringMethod() {
        Set<String> tags = Set.of("modern", "luxury");
        AddTagCommand addTagCommand = new AddTagCommand(tags);

        String expected = AddTagCommand.class.getCanonicalName() + "{toAdd=" + tags + "}";
        assertEquals(expected, addTagCommand.toString());
    }
}
