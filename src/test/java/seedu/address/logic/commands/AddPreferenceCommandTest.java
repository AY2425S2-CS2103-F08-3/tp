package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
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
 * {@code AddPreferenceCommand}.
 */
public class AddPreferenceCommandTest {

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
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        // Create a price range for the preference
        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();

        // Add a person with matching preferences
        Person person = model.getSortedFilteredPersonList().get(0);
        Set<String> existingTags = Set.of("modern");
        Set<String> newTags = Set.of();

        // Create the expected model state
        Person expectedPerson = expectedModel.getSortedFilteredPersonList().get(0);
        PropertyPreference expectedPreference = new PropertyPreference(
                priceRange,
                new HashSet<>(),
                expectedPerson);

        // Add tags to the preference
        Tag expectedTag = expectedModel.getTag("modern");
        expectedTag.addPropertyPreference(expectedPreference);
        expectedModel.setTag(expectedTag, expectedTag);
        expectedPreference.addTag(expectedTag);

        expectedPerson.addPropertyPreference(expectedPreference);
        expectedModel.setPerson(expectedPerson, expectedPerson);
        expectedModel.resetAllLists();

        // Set up the command
        AddPreferenceCommand addPreferenceCommand = new AddPreferenceCommand(
                INDEX_FIRST_PERSON,
                priceRange,
                existingTags,
                newTags);

        // Prepare the expected success message
        String expectedMessage = String.format(AddPreferenceCommand.MESSAGE_SUCCESS,
                Messages.format(expectedPerson, expectedPreference));

        // Execute the command and verify the result
        assertCommandSuccess(addPreferenceCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getSortedFilteredPersonList().size() + 1);
        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();
        Set<String> existingTags = Set.of("modern");
        Set<String> newTags = Set.of();
        AddPreferenceCommand addPreferenceCommand = new AddPreferenceCommand(
                outOfBoundIndex,
                priceRange,
                existingTags,
                newTags);

        assertCommandFailure(addPreferenceCommand, model,
                String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, AddPreferenceCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidExistingTags_throwsCommandException() {
        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();
        Set<String> existingTags = Set.of("nonexistent");
        Set<String> newTags = Set.of();
        AddPreferenceCommand addPreferenceCommand = new AddPreferenceCommand(
                INDEX_FIRST_PERSON,
                priceRange,
                existingTags,
                newTags);

        assertCommandFailure(addPreferenceCommand, model,
                String.format(AddPreferenceCommand.MESSAGE_INVALID_TAGS, AddPreferenceCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_duplicateNewTags_throwsCommandException() {
        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();
        Set<String> existingTags = Set.of();
        Set<String> newTags = Set.of("modern"); // Already exists in model
        AddPreferenceCommand addPreferenceCommand = new AddPreferenceCommand(
                INDEX_FIRST_PERSON,
                priceRange,
                existingTags,
                newTags);

        assertCommandFailure(addPreferenceCommand, model,
                String.format(AddPreferenceCommand.MESSAGE_DUPLICATE_TAGS, AddPreferenceCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();
        Set<String> existingTags = Set.of("modern");
        Set<String> newTags = Set.of();
        AddPreferenceCommand addPreferenceCommand = new AddPreferenceCommand(
                INDEX_FIRST_PERSON,
                priceRange,
                existingTags,
                newTags);

        // same object -> returns true
        assertTrue(addPreferenceCommand.equals(addPreferenceCommand));

        // same values -> returns true
        AddPreferenceCommand addPreferenceCommandCopy = new AddPreferenceCommand(
                INDEX_FIRST_PERSON,
                priceRange,
                existingTags,
                newTags);
        assertTrue(addPreferenceCommand.equals(addPreferenceCommandCopy));

        // different types -> returns false
        assertFalse(addPreferenceCommand.equals(1));

        // null -> returns false
        assertFalse(addPreferenceCommand.equals(null));

        // different price range -> returns false
        PriceRange differentPriceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("200000")
                .withUpperBoundPrice("300000")
                .build();
        AddPreferenceCommand addPreferenceCommandDifferentPrice = new AddPreferenceCommand(
                INDEX_FIRST_PERSON,
                differentPriceRange,
                existingTags,
                newTags);
        assertFalse(addPreferenceCommand.equals(addPreferenceCommandDifferentPrice));
    }

    @Test
    public void toStringMethod() {
        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();
        Set<String> existingTags = Set.of("modern");
        Set<String> newTags = Set.of();
        AddPreferenceCommand addPreferenceCommand = new AddPreferenceCommand(
                INDEX_FIRST_PERSON,
                priceRange,
                existingTags,
                newTags);

        String expected = AddPreferenceCommand.class.getCanonicalName() + "{priceRange=" + priceRange + "}";
        assertEquals(expected, addPreferenceCommand.toString());
    }
}
