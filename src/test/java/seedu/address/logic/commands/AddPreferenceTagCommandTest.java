package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

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
 * {@code AddPreferenceTagCommand}.
 */
public class AddPreferenceTagCommandTest {
    private Model model;
    private Person samplePerson;
    private PropertyPreference samplePreference;

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

        // Create a sample property preference without tags
        samplePreference = new PropertyPreference(
                new PriceRange(new Price("100000"), new Price("200000")),
                new HashSet<>(), // empty tags
                samplePerson
        );

        // Add person and preference to model
        samplePerson.addPropertyPreference(samplePreference);
        model.addPerson(samplePerson);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
    }

    @Test
    public void execute_addExistingTags_success() {
        // Add existing tags to model
        Set<String> existingTags = Set.of("modern", "luxury");
        model.addTags(existingTags);

        AddPreferenceTagCommand command = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                existingTags,
                new HashSet<>() // no new tags
        );

        // Create expected model state
        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Create copies of the person and preference for the expected model
        Person expectedPerson = new Person(
                new Name("John Doe"),
                new Phone("98765432"),
                new Email("johndoe@example.com"),
                List.of(), // Empty property preferences
                List.of() // Empty listings
        );

        PropertyPreference expectedPreference = new PropertyPreference(
                new PriceRange(new Price("100000"), new Price("200000")),
                new HashSet<>(), // empty tags
                expectedPerson
        );

        // Add person and preference to expected model
        expectedPerson.addPropertyPreference(expectedPreference);
        expectedModel.addPerson(expectedPerson);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        // Add existing tags to model
        expectedModel.addTags(existingTags);

        // Add tags to preference in expected model
        for (String tagName : existingTags) {
            Tag tag = expectedModel.getTag(tagName);
            tag.addPropertyPreference(expectedPreference);
            expectedModel.setTag(tag, tag);
            expectedPreference.addTag(tag);
        }

        expectedModel.setPerson(expectedPerson, expectedPerson);
        expectedModel.resetAllLists();

        String expectedMessage = String.format(AddPreferenceTagCommand.MESSAGE_SUCCESS,
                Messages.format(expectedPerson, expectedPreference),
                Messages.formatTagsOnly(expectedPreference.getTags()));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addNewTags_success() {
        // Add existing tags to model
        Set<String> existingTags = Set.of("modern", "luxury");
        model.addTags(existingTags);

        Set<String> newTags = Set.of("spacious", "quiet");
        AddPreferenceTagCommand command = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                new HashSet<>(), // no existing tags
                newTags
        );

        // Create expected model state
        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Create copies of the person and preference for the expected model
        Person expectedPerson = new Person(
                new Name("John Doe"),
                new Phone("98765432"),
                new Email("johndoe@example.com"),
                List.of(), // Empty property preferences
                List.of() // Empty listings
        );

        PropertyPreference expectedPreference = new PropertyPreference(
                new PriceRange(new Price("100000"), new Price("200000")),
                new HashSet<>(), // empty tags
                expectedPerson
        );

        // Add person and preference to expected model
        expectedPerson.addPropertyPreference(expectedPreference);
        expectedModel.addPerson(expectedPerson);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        // Add existing tags to model
        expectedModel.addTags(existingTags);

        // Add new tags to model and preference
        expectedModel.addTags(newTags);
        for (String tagName : newTags) {
            Tag tag = expectedModel.getTag(tagName);
            tag.addPropertyPreference(expectedPreference);
            expectedModel.setTag(tag, tag);
            expectedPreference.addTag(tag);
        }

        expectedModel.setPerson(expectedPerson, expectedPerson);
        expectedModel.resetAllLists();

        String expectedMessage = String.format(AddPreferenceTagCommand.MESSAGE_SUCCESS,
                Messages.format(expectedPerson, expectedPreference),
                Messages.formatTagsOnly(expectedPreference.getTags()));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addMixedTags_success() {
        // Add existing tags to model
        Set<String> existingTags = Set.of("modern", "luxury");
        model.addTags(existingTags);

        Set<String> newTags = Set.of("spacious", "quiet");
        AddPreferenceTagCommand command = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                existingTags,
                newTags
        );

        // Create expected model state
        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Create copies of the person and preference for the expected model
        Person expectedPerson = new Person(
                new Name("John Doe"),
                new Phone("98765432"),
                new Email("johndoe@example.com"),
                List.of(), // Empty property preferences
                List.of() // Empty listings
        );

        PropertyPreference expectedPreference = new PropertyPreference(
                new PriceRange(new Price("100000"), new Price("200000")),
                new HashSet<>(), // empty tags
                expectedPerson
        );

        // Add person and preference to expected model
        expectedPerson.addPropertyPreference(expectedPreference);
        expectedModel.addPerson(expectedPerson);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        // Add existing tags to model
        expectedModel.addTags(existingTags);

        // Add new tags to model and preference
        expectedModel.addTags(newTags);
        Set<String> allTags = new HashSet<>(existingTags);
        allTags.addAll(newTags);
        for (String tagName : allTags) {
            Tag tag = expectedModel.getTag(tagName);
            tag.addPropertyPreference(expectedPreference);
            expectedModel.setTag(tag, tag);
            expectedPreference.addTag(tag);
        }

        expectedModel.setPerson(expectedPerson, expectedPerson);
        expectedModel.resetAllLists();

        String expectedMessage = String.format(AddPreferenceTagCommand.MESSAGE_SUCCESS,
                Messages.format(expectedPerson, expectedPreference),
                Messages.formatTagsOnly(expectedPreference.getTags()));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Set<String> existingTags = Set.of("modern");
        model.addTags(existingTags);

        AddPreferenceTagCommand command = new AddPreferenceTagCommand(
                Index.fromOneBased(2),
                Index.fromOneBased(1),
                existingTags,
                new HashSet<>()
        );

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                AddPreferenceTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidPreferenceIndex_throwsCommandException() {
        Set<String> existingTags = Set.of("modern");
        model.addTags(existingTags);

        AddPreferenceTagCommand command = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(2),
                existingTags,
                new HashSet<>()
        );

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_PREFERENCE_DISPLAYED_INDEX,
                AddPreferenceTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidTag_throwsCommandException() {
        Set<String> existingTags = Set.of("nonexistent");
        AddPreferenceTagCommand command = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                existingTags,
                new HashSet<>()
        );

        assertCommandFailure(command, model, String.format(AddPreferenceTagCommand.MESSAGE_INVALID_TAGS,
                AddPreferenceTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_duplicateNewTag_throwsCommandException() {
        Set<String> existingTags = Set.of("modern");
        model.addTags(existingTags);

        Set<String> newTags = Set.of("modern"); // Already exists
        AddPreferenceTagCommand command = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                new HashSet<>(),
                newTags
        );

        assertCommandFailure(command, model, String.format(AddPreferenceTagCommand.MESSAGE_DUPLICATE_TAGS,
                AddPreferenceTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_duplicateTagInPreference_throwsCommandException() {
        // Add existing tags to model and preference
        Set<String> existingTags = Set.of("modern");
        model.addTags(existingTags);
        for (String tagName : existingTags) {
            Tag tag = model.getTag(tagName);
            tag.addPropertyPreference(samplePreference);
            model.setTag(tag, tag);
            samplePreference.addTag(tag);
        }
        model.setPerson(samplePerson, samplePerson);

        // Try to add the same tag again
        AddPreferenceTagCommand command = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                existingTags,
                new HashSet<>()
        );

        assertCommandFailure(command, model, String.format(AddPreferenceTagCommand.MESSAGE_DUPLICATE_TAGS_IN_LISTING,
                AddPreferenceTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        Set<String> existingTags = Set.of("modern");
        Set<String> newTags = Set.of("spacious");
        AddPreferenceTagCommand addPreferenceTagCommand = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                existingTags,
                newTags
        );

        // same object -> returns true
        assertTrue(addPreferenceTagCommand.equals(addPreferenceTagCommand));

        // same values -> returns true
        AddPreferenceTagCommand addPreferenceTagCommandCopy = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                existingTags,
                newTags
        );
        assertTrue(addPreferenceTagCommand.equals(addPreferenceTagCommandCopy));

        // different types -> returns false
        assertFalse(addPreferenceTagCommand.equals(1));

        // null -> returns false
        assertFalse(addPreferenceTagCommand.equals(null));

        // different person index -> returns false
        AddPreferenceTagCommand differentPersonIndexCommand = new AddPreferenceTagCommand(
                Index.fromOneBased(2),
                Index.fromOneBased(1),
                existingTags,
                newTags
        );
        assertFalse(addPreferenceTagCommand.equals(differentPersonIndexCommand));

        // different preference index -> returns false
        AddPreferenceTagCommand differentPreferenceIndexCommand = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(2),
                existingTags,
                newTags
        );
        assertFalse(addPreferenceTagCommand.equals(differentPreferenceIndexCommand));

        // different existing tags -> returns false
        AddPreferenceTagCommand differentExistingTagsCommand = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                Set.of("luxury"),
                newTags
        );
        assertFalse(addPreferenceTagCommand.equals(differentExistingTagsCommand));

        // different new tags -> returns false
        AddPreferenceTagCommand differentNewTagsCommand = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                existingTags,
                Set.of("quiet")
        );
        assertFalse(addPreferenceTagCommand.equals(differentNewTagsCommand));
    }

    @Test
    public void toStringMethod() {
        Set<String> existingTags = Set.of("modern");
        Set<String> newTags = Set.of("spacious");
        AddPreferenceTagCommand addPreferenceTagCommand = new AddPreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                existingTags,
                newTags
        );

        String expected = AddPreferenceTagCommand.class.getCanonicalName() + "{personIndex=" + Index.fromOneBased(1)
                + ", preferenceIndex=" + Index.fromOneBased(1)
                + ", tags=" + existingTags
                + ", newTags=" + newTags + "}";
        assertEquals(expected, addPreferenceTagCommand.toString());
    }
}
