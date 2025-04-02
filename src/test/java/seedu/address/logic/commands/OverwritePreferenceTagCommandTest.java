package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.OverwritePreferenceTagCommand.MESSAGE_INVALID_TAGS;

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
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code OverwritePreferenceTagCommand}.
 */
public class OverwritePreferenceTagCommandTest {
    private Model model;
    private Model expectedModel;
    private Person samplePerson;
    private PropertyPreference samplePreference;
    private Tag sampleTag;

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

        // Create a sample tag
        sampleTag = new Tag("Luxury", new ArrayList<>(), new ArrayList<>());

        // Create a sample preference
        samplePreference = new PropertyPreference(
                new PriceRange(new Price("100000"), new Price("200000")),
                new HashSet<>(), // Empty tags
                samplePerson // Add the person as the owner
        );

        // Add person and preference to model
        model.addPerson(samplePerson);
        samplePerson.addPropertyPreference(samplePreference);
        model.setPerson(samplePerson, samplePerson);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        // Add person and preference to expected model
        expectedModel.addPerson(samplePerson);
        expectedModel.getFilteredPersonList().get(0).addPropertyPreference(samplePreference);
        expectedModel.setPerson(expectedModel.getFilteredPersonList().get(0),
                expectedModel.getFilteredPersonList().get(0));
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Set<String> tagSet = Set.of("Luxury");
        Set<String> newTagSet = Set.of("Modern");
        OverwritePreferenceTagCommand command = new OverwritePreferenceTagCommand(
                Index.fromOneBased(2), Index.fromOneBased(1), tagSet, newTagSet);

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                OverwritePreferenceTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidTag_throwsCommandException() {
        Set<String> tagSet = Set.of("InvalidTag");
        Set<String> newTagSet = Set.of("Modern");
        OverwritePreferenceTagCommand command = new OverwritePreferenceTagCommand(
                Index.fromOneBased(1), Index.fromOneBased(1), tagSet, newTagSet);

        assertCommandFailure(command, model, String.format(MESSAGE_INVALID_TAGS,
                OverwritePreferenceTagCommand.MESSAGE_USAGE));
    }


    @Test
    public void equals() {
        Set<String> tagSet = Set.of("Luxury");
        Set<String> newTagSet = Set.of("Modern");
        OverwritePreferenceTagCommand overwritePreferenceTagCommand = new OverwritePreferenceTagCommand(
                Index.fromOneBased(1), Index.fromOneBased(1), tagSet, newTagSet);

        // same object -> returns true
        assertTrue(overwritePreferenceTagCommand.equals(overwritePreferenceTagCommand));

        // same values -> returns true
        OverwritePreferenceTagCommand overwritePreferenceTagCommandCopy = new OverwritePreferenceTagCommand(
                Index.fromOneBased(1), Index.fromOneBased(1), tagSet, newTagSet);
        assertTrue(overwritePreferenceTagCommand.equals(overwritePreferenceTagCommandCopy));

        // different types -> returns false
        assertFalse(overwritePreferenceTagCommand.equals(1));

        // null -> returns false
        assertFalse(overwritePreferenceTagCommand.equals(null));

        // different person index -> returns false
        OverwritePreferenceTagCommand differentPersonIndexCommand = new OverwritePreferenceTagCommand(
                Index.fromOneBased(2), Index.fromOneBased(1), tagSet, newTagSet);
        assertFalse(overwritePreferenceTagCommand.equals(differentPersonIndexCommand));

        // different preference index -> returns false
        OverwritePreferenceTagCommand differentPreferenceIndexCommand = new OverwritePreferenceTagCommand(
                Index.fromOneBased(1), Index.fromOneBased(2), tagSet, newTagSet);
        assertFalse(overwritePreferenceTagCommand.equals(differentPreferenceIndexCommand));

        // different tag set -> returns false
        OverwritePreferenceTagCommand differentTagSetCommand = new OverwritePreferenceTagCommand(
                Index.fromOneBased(1), Index.fromOneBased(1), Set.of("Different"), newTagSet);
        assertFalse(overwritePreferenceTagCommand.equals(differentTagSetCommand));

        // different new tag set -> returns false
        OverwritePreferenceTagCommand differentNewTagSetCommand = new OverwritePreferenceTagCommand(
                Index.fromOneBased(1), Index.fromOneBased(1), tagSet, Set.of("Different"));
        assertFalse(overwritePreferenceTagCommand.equals(differentNewTagSetCommand));
    }

    @Test
    public void toStringMethod() {
        Set<String> tagSet = Set.of("Luxury");
        Set<String> newTagSet = Set.of("Modern");
        OverwritePreferenceTagCommand overwritePreferenceTagCommand = new OverwritePreferenceTagCommand(
                Index.fromOneBased(1), Index.fromOneBased(1), tagSet, newTagSet);
        String expected = OverwritePreferenceTagCommand.class.getCanonicalName()
                + "{personIndex=" + Index.fromOneBased(1)
                + ", preferenceIndex=" + Index.fromOneBased(1)
                + ", tags=" + tagSet
                + ", newTags=" + newTagSet
                + "}";
        assertEquals(expected, overwritePreferenceTagCommand.toString());
    }
}
