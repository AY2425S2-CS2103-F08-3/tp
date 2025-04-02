package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;

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
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PriceRangeBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeletePreferenceTagCommand}.
 */
public class DeletePreferenceTagCommandTest {

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
        model.addTags(Set.of("Luxury"));
        expectedModel.addTags(Set.of("Luxury"));
        sampleTag = model.getTag("Luxury");

        // Create a sample preference with the tag
        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();
        samplePreference = new PropertyPreference(priceRange, new HashSet<>(Set.of(sampleTag)), samplePerson);
        sampleTag.addPropertyPreference(samplePreference);
        model.setTag(sampleTag, sampleTag);
        samplePerson.addPropertyPreference(samplePreference);

        // Add person to both models
        model.addPerson(samplePerson);
        expectedModel.addPerson(samplePerson);

        // Set up expected model
        Person expectedPerson = expectedModel.getSortedFilteredPersonList().get(0);
        PropertyPreference expectedPreference = new PropertyPreference(priceRange, new HashSet<>(), expectedPerson);
        Tag expectedTag = expectedModel.getTag("Luxury");
        expectedTag.addPropertyPreference(expectedPreference);
        expectedModel.setTag(expectedTag, expectedTag);
        expectedPreference.addTag(expectedTag);
        expectedPerson.addPropertyPreference(expectedPreference);
        expectedModel.setPerson(expectedPerson, expectedPerson);

        // Update filtered lists
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        DeletePreferenceTagCommand command = new DeletePreferenceTagCommand(
                Index.fromOneBased(2),
                Index.fromOneBased(1),
                Set.of("Luxury")
        );

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                DeletePreferenceTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidTag_throwsCommandException() {
        DeletePreferenceTagCommand command = new DeletePreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                Set.of("NonExistentTag")
        );

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_TAG_NOT_FOUND_IN_PREFERENCE,
                "NonExistentTag", DeletePreferenceTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_tagNotInPreference_throwsCommandException() {
        // Add a new tag that's not in the preference
        model.addTags(Set.of("NewTag"));
        expectedModel.addTags(Set.of("NewTag"));

        DeletePreferenceTagCommand command = new DeletePreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                Set.of("NewTag")
        );

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_TAG_NOT_FOUND_IN_PREFERENCE,
                "NewTag", DeletePreferenceTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        DeletePreferenceTagCommand deletePreferenceTagCommand = new DeletePreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                Set.of("Luxury")
        );

        // same object -> returns true
        assertTrue(deletePreferenceTagCommand.equals(deletePreferenceTagCommand));

        // same values -> returns true
        DeletePreferenceTagCommand deletePreferenceTagCommandCopy = new DeletePreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                Set.of("Luxury")
        );
        assertTrue(deletePreferenceTagCommand.equals(deletePreferenceTagCommandCopy));

        // different types -> returns false
        assertFalse(deletePreferenceTagCommand.equals(1));

        // null -> returns false
        assertFalse(deletePreferenceTagCommand.equals(null));

        // different person index -> returns false
        DeletePreferenceTagCommand differentPersonIndexCommand = new DeletePreferenceTagCommand(
                Index.fromOneBased(2),
                Index.fromOneBased(1),
                Set.of("Luxury")
        );
        assertFalse(deletePreferenceTagCommand.equals(differentPersonIndexCommand));

        // different preference index -> returns false
        DeletePreferenceTagCommand differentPreferenceIndexCommand = new DeletePreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(2),
                Set.of("Luxury")
        );
        assertFalse(deletePreferenceTagCommand.equals(differentPreferenceIndexCommand));

        // different tags -> returns false
        DeletePreferenceTagCommand differentTagsCommand = new DeletePreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                Set.of("DifferentTag")
        );
        assertFalse(deletePreferenceTagCommand.equals(differentTagsCommand));
    }

    @Test
    public void toStringMethod() {
        DeletePreferenceTagCommand deletePreferenceTagCommand = new DeletePreferenceTagCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(1),
                Set.of("Luxury")
        );
        String expected = DeletePreferenceTagCommand.class.getCanonicalName() + "{personIndex=" + Index.fromOneBased(1)
                + ", preferenceIndex=" + Index.fromOneBased(1) + ", tagsToDelete=[Luxury]}";
        assertEquals(expected, deletePreferenceTagCommand.toString());
    }
}
