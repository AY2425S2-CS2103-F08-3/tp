package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PREFERENCE;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PREFERENCE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PriceRangeBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeletePreferenceCommand}.
 */
public class DeletePreferenceCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // Register tags in both models
        Set<String> tagNames = Set.of("modern", "luxury");
        model.addTags(tagNames);
        expectedModel.addTags(tagNames);

        // Add a preference to the first person in both models
        Person person = model.getSortedFilteredPersonList().get(0);
        Person expectedPerson = expectedModel.getSortedFilteredPersonList().get(0);

        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();

        PropertyPreference preference = new PropertyPreference(priceRange, new HashSet<>(), person);
        Tag tag = model.getTag("modern");
        tag.addPropertyPreference(preference);
        model.setTag(tag, tag);
        preference.addTag(tag);
        person.addPropertyPreference(preference);
        model.setPerson(person, person);

        PropertyPreference expectedPreference = new PropertyPreference(priceRange, new HashSet<>(), expectedPerson);
        Tag expectedTag = expectedModel.getTag("modern");
        expectedTag.addPropertyPreference(expectedPreference);
        expectedModel.setTag(expectedTag, expectedTag);
        expectedPreference.addTag(expectedTag);
        expectedPerson.addPropertyPreference(expectedPreference);
        expectedModel.setPerson(expectedPerson, expectedPerson);
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDeleteFrom = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PropertyPreference preferenceToDelete = personToDeleteFrom.getPropertyPreferences()
                .get(INDEX_FIRST_PREFERENCE.getZeroBased());

        DeletePreferenceCommand deletePreferenceCommand = new DeletePreferenceCommand(
                INDEX_FIRST_PERSON,
                INDEX_FIRST_PREFERENCE);

        String expectedMessage = String.format(DeletePreferenceCommand.MESSAGE_DELETE_PREFERENCE_SUCCESS,
                Messages.format(personToDeleteFrom, preferenceToDelete));

        // Create expected model state
        Person expectedPerson = expectedModel.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedPerson.removePropertyPreference(preferenceToDelete);
        expectedModel.setPerson(expectedPerson, expectedPerson);
        expectedModel.resetAllLists();

        assertCommandSuccess(deletePreferenceCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getSortedFilteredPersonList().size() + 1);
        DeletePreferenceCommand deletePreferenceCommand = new DeletePreferenceCommand(
                outOfBoundIndex,
                INDEX_FIRST_PREFERENCE);

        assertCommandFailure(deletePreferenceCommand, model,
                String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, DeletePreferenceCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidPreferenceIndexUnfilteredList_failure() {
        Person person = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(person.getPropertyPreferences().size() + 1);
        DeletePreferenceCommand deletePreferenceCommand = new DeletePreferenceCommand(
                INDEX_FIRST_PERSON,
                outOfBoundIndex);

        assertCommandFailure(deletePreferenceCommand, model,
                String.format(Messages.MESSAGE_INVALID_PREFERENCE_DISPLAYED_INDEX,
                DeletePreferenceCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        DeletePreferenceCommand deleteFirstCommand = new DeletePreferenceCommand(
                INDEX_FIRST_PERSON,
                INDEX_FIRST_PREFERENCE);
        DeletePreferenceCommand deleteSecondCommand = new DeletePreferenceCommand(
                INDEX_SECOND_PERSON,
                INDEX_SECOND_PREFERENCE);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeletePreferenceCommand deleteFirstCommandCopy = new DeletePreferenceCommand(
                INDEX_FIRST_PERSON,
                INDEX_FIRST_PREFERENCE);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        DeletePreferenceCommand deletePreferenceCommand = new DeletePreferenceCommand(
                INDEX_FIRST_PERSON,
                INDEX_FIRST_PREFERENCE);
        String expected = DeletePreferenceCommand.class.getCanonicalName() + "{targetPersonIndex="
                + INDEX_FIRST_PERSON + ", targetPreferenceIndex=" + INDEX_FIRST_PREFERENCE + "}";
        assertEquals(expected, deletePreferenceCommand.toString());
    }
}
