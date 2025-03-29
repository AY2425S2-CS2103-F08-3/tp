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
import seedu.address.model.tag.TagRegistry;
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

        // Ensure there are some registered tags for testing
        model.addTags(Set.of("modern", "cozy"));
        expectedModel.addTags(Set.of("modern", "cozy"));
    }

    @Test
    public void execute_validIndexAndPreference_success() {
        Person personToAddPreference = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();

        // Use only existing tags for this test to avoid registration issues
        Set<String> existingTags = Set.of("modern");
        Set<String> newTags = Set.of(); // Empty set for simplicity

        AddPreferenceCommand addCommand = new AddPreferenceCommand(
                INDEX_FIRST_PERSON, priceRange, existingTags, newTags);

        // Update expected model to reflect changes after execution
        Person expectedPerson = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> expectedTagSet = new HashSet<>();
        TagRegistry tagRegistry = TagRegistry.of();

        // Add tags to the preference
        for (String tagName : existingTags) {
            expectedTagSet.add(tagRegistry.get(tagName.toUpperCase()));
        }

        PropertyPreference expectedPreference = new PropertyPreference(priceRange, expectedTagSet, expectedPerson);
        expectedPerson.addPropertyPreference(expectedPreference);

        String expectedMessage = String.format(AddPreferenceCommand.MESSAGE_SUCCESS,
                Messages.format(expectedPerson, expectedPreference));

        assertCommandSuccess(addCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        PriceRange priceRange = new PriceRangeBuilder().build();

        AddPreferenceCommand addCommand = new AddPreferenceCommand(
                outOfBoundIndex, priceRange, Set.of(), Set.of());

        assertCommandFailure(addCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidExistingTags_throwsCommandException() {
        PriceRange priceRange = new PriceRangeBuilder().build();
        Set<String> nonExistentTags = Set.of("nonexistent");

        AddPreferenceCommand addCommand = new AddPreferenceCommand(
                INDEX_FIRST_PERSON, priceRange, nonExistentTags, Set.of());

        assertCommandFailure(addCommand, model, AddPreferenceCommand.MESSAGE_INVALID_TAGS);
    }

    @Test
    public void execute_duplicateNewTags_throwsCommandException() {
        PriceRange priceRange = new PriceRangeBuilder().build();
        Set<String> duplicateTags = Set.of("modern"); // Already added in setUp

        AddPreferenceCommand addCommand = new AddPreferenceCommand(
                INDEX_FIRST_PERSON, priceRange, Set.of(), duplicateTags);

        assertCommandFailure(addCommand, model, AddPreferenceCommand.MESSAGE_DUPLICATE_TAGS);
    }

    @Test
    public void equals() {
        PriceRange priceRange1 = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();

        PriceRange priceRange2 = new PriceRangeBuilder()
                .withLowerBoundPrice("300000")
                .withUpperBoundPrice("400000")
                .build();

        AddPreferenceCommand addCommand1 = new AddPreferenceCommand(
                INDEX_FIRST_PERSON, priceRange1, Set.of(), Set.of());
        AddPreferenceCommand addCommand2 = new AddPreferenceCommand(
                INDEX_FIRST_PERSON, priceRange2, Set.of(), Set.of());

        // same object -> returns true
        assertTrue(addCommand1.equals(addCommand1));

        // same values -> returns true
        AddPreferenceCommand addCommand1Copy = new AddPreferenceCommand(
                INDEX_FIRST_PERSON, priceRange1, Set.of(), Set.of());
        assertTrue(addCommand1.equals(addCommand1Copy));

        // different types -> returns false
        assertFalse(addCommand1.equals(1));

        // null -> returns false
        assertFalse(addCommand1.equals(null));

        // different price range -> returns false
        assertFalse(addCommand1.equals(addCommand2));
    }

    @Test
    public void toStringMethod() {
        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("100000")
                .withUpperBoundPrice("200000")
                .build();

        AddPreferenceCommand addCommand = new AddPreferenceCommand(
                INDEX_FIRST_PERSON, priceRange, Set.of("modern"), Set.of("premium"));
        String expected = AddPreferenceCommand.class.getCanonicalName() + "{priceRange=" + priceRange + "}";
        assertEquals(expected, addCommand.toString());
    }
}
