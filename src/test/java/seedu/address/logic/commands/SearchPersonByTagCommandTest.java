package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_TAG_DOES_NOT_EXIST;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

public class SearchPersonByTagCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        ReadOnlyAddressBook addressBook = new seedu.address.model.AddressBook();
        ReadOnlyUserPrefs userPrefs = new UserPrefs();
        model = new ModelManager(addressBook, userPrefs);

        // Add tags as strings
        model.addTags(Set.of("Gym", "Pet-Friendly"));
    }

    @Test
    public void execute_nonExistentTag_throwsCommandException() {
        SearchPersonByTagCommand command = new SearchPersonByTagCommand(Set.of("nonexistenttag"));
        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(MESSAGE_TAG_DOES_NOT_EXIST, "nonexistenttag",
                SearchPersonByTagCommand.MESSAGE_USAGE), thrown.getMessage());
    }

    @Test
    public void execute_noMatches_showsNoMatches() throws CommandException {
        // Add a tag that exists in model but no person matches both tags
        model.addTags(Set.of("garden"));
        SearchPersonByTagCommand command = new SearchPersonByTagCommand(Set.of("gym", "garden"));
        CommandResult result = command.execute(model);
        assertEquals("No persons matching the tags.", result.getFeedbackToUser());
    }
}
