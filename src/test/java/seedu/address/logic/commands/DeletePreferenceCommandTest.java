package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;
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
import seedu.address.model.tag.TagRegistry;
import seedu.address.testutil.PriceRangeBuilder;
import seedu.address.testutil.TagBuilder;

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
    }

    @Test
    public void execute_validIndices_success() {
        // Register the tag first
        Set<String> tagNames = Set.of("modern");
        model.addTags(tagNames);
        expectedModel.addTags(tagNames);

        // Get the same Tag reference for both models to ensure consistency
        TagRegistry tagRegistry = TagRegistry.of();
        Tag tag = tagRegistry.get("MODERN");

        // Create the price range
        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();

        // Add a property preference to the first person
        Person personWithPreference = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PropertyPreference preference = new PropertyPreference(
                priceRange,
                Collections.singleton(tag),
                personWithPreference);
        personWithPreference.addPropertyPreference(preference);

        DeletePreferenceCommand deleteCommand = new DeletePreferenceCommand(INDEX_FIRST_PERSON, Index.fromZeroBased(0));

        // Set up expected model
        Person expectedPerson = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PropertyPreference expectedPreference = new PropertyPreference(
                priceRange,
                Collections.singleton(tag),
                expectedPerson);
        expectedPerson.addPropertyPreference(expectedPreference);

        String expectedMessage = String.format(DeletePreferenceCommand.MESSAGE_DELETE_PREFERENCE_SUCCESS,
                Messages.format(expectedPerson, expectedPreference));

        // Execute the delete
        expectedPerson.removePropertyPreference(expectedPreference);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeletePreferenceCommand deleteCommand = new DeletePreferenceCommand(outOfBoundIndex, Index.fromOneBased(1));

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPreferenceIndex_throwsCommandException() {
        // Add a property preference to the first person to ensure at least one exists
        Person personWithPreference = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PropertyPreference preference = createTestPropertyPreference(personWithPreference);
        personWithPreference.addPropertyPreference(preference);

        // Try to delete a non-existent preference index
        Index outOfBoundIndex = Index.fromOneBased(personWithPreference.getPropertyPreferences().size() + 1);
        DeletePreferenceCommand deleteCommand = new DeletePreferenceCommand(INDEX_FIRST_PERSON, outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PREFERENCE_DISPLAYED_INDEX);
    }

    @Test
    public void execute_activeFilterTags_success() {
        // Register the tags first
        Set<String> modernTag = Set.of("modern");
        Set<String> luxuryTag = Set.of("luxury");
        model.addTags(modernTag);
        model.addTags(luxuryTag);
        expectedModel.addTags(modernTag);
        expectedModel.addTags(luxuryTag);

        // Add a property preference with certain tags
        Person personWithPreference = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PropertyPreference preference = createTestPropertyPreference(personWithPreference);
        personWithPreference.addPropertyPreference(preference);

        // Add another preference with different tags
        Tag differentTag = new TagBuilder().withTagName("luxury").build();
        Set<Tag> differentTags = Collections.singleton(differentTag);
        PropertyPreference differentPreference = new PropertyPreference(
                preference.getPriceRange(), differentTags, personWithPreference);
        personWithPreference.addPropertyPreference(differentPreference);

        // The first preference should be at index 0
        DeletePreferenceCommand deleteCommand = new DeletePreferenceCommand(INDEX_FIRST_PERSON, Index.fromZeroBased(0));

        // Set up expected model
        Person expectedPerson = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PropertyPreference expectedPreference = createTestPropertyPreference(expectedPerson);
        expectedPerson.addPropertyPreference(expectedPreference);

        // Add the different preference to the expected model too
        PropertyPreference expectedDifferentPreference = new PropertyPreference(
                expectedPreference.getPriceRange(), Collections.singleton(differentTag), expectedPerson);
        expectedPerson.addPropertyPreference(expectedDifferentPreference);

        String expectedMessage = String.format(DeletePreferenceCommand.MESSAGE_DELETE_PREFERENCE_SUCCESS,
                Messages.format(expectedPerson, expectedPreference));

        // Execute the delete
        expectedPerson.removePropertyPreference(expectedPreference);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        DeletePreferenceCommand deleteFirstCommand =
            new DeletePreferenceCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        DeletePreferenceCommand deleteSecondCommand =
            new DeletePreferenceCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON);

        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));
        // same values -> returns true
        DeletePreferenceCommand deleteFirstCommandCopy =
            new DeletePreferenceCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person index -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        // different preference index -> returns false
        DeletePreferenceCommand deleteFirstSecondIdxCommand =
            new DeletePreferenceCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertFalse(deleteFirstCommand.equals(deleteFirstSecondIdxCommand));
    }

    @Test
    public void toStringMethod() {
        Index personIndex = Index.fromOneBased(1);
        Index preferenceIndex = Index.fromOneBased(2);
        DeletePreferenceCommand deleteCommand = new DeletePreferenceCommand(personIndex, preferenceIndex);

        String expected = DeletePreferenceCommand.class.getCanonicalName() + "{targetPersonIndex=" + personIndex
                + ", targetPreferenceIndex=" + preferenceIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Creates a test property preference for testing.
     */
    private PropertyPreference createTestPropertyPreference(Person person) {
        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();

        Tag tag = new TagBuilder().withTagName("modern").build();
        return new PropertyPreference(priceRange, Collections.singleton(tag), person);
    }
}
