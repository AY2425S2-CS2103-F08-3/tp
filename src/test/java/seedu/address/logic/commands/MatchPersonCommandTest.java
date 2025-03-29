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
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ListingBuilder;
import seedu.address.testutil.PriceRangeBuilder;
import seedu.address.testutil.TagBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code MatchPersonCommand}.
 */
public class MatchPersonCommandTest {

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
        // Set up a person with a property preference
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PropertyPreference preference = createTestPropertyPreference(person);
        person.addPropertyPreference(preference);

        // Create a listing that matches the preference
        Listing listing = createMatchingListing(preference);
        model.addListing(listing);

        // Create the expected model state
        Person expectedPerson = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PropertyPreference expectedPreference = createTestPropertyPreference(expectedPerson);
        expectedPerson.addPropertyPreference(expectedPreference);
        expectedModel.addListing(listing);

        // Set up the matching command
        MatchPersonCommand matchCommand = new MatchPersonCommand(INDEX_FIRST_PERSON, Index.fromZeroBased(0));

        // Prepare the expected success message
        String expectedMessage = String.format(MatchPersonCommand.MESSAGE_MATCH_PERSON_SUCCESS,
                person.getName(),
                preference.getPriceRange(),
                Messages.format(preference.getTags()));

        // Update the expected model with the predicate and comparator
        expectedModel.updateFilteredListingList(l -> true);
        expectedModel.updateSortedFilteredListingList((l1, l2) -> 0);

        // Execute the command and verify the result
        assertCommandSuccess(matchCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        MatchPersonCommand matchCommand = new MatchPersonCommand(outOfBoundIndex, Index.fromOneBased(1));

        assertCommandFailure(matchCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPreferenceIndex_throwsCommandException() {
        // Add a preference to the first person to make sure there's at least one
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PropertyPreference preference = createTestPropertyPreference(person);
        person.addPropertyPreference(preference);

        // Create an invalid preference index
        Index outOfBoundIndex = Index.fromOneBased(person.getPropertyPreferences().size() + 1);
        MatchPersonCommand matchCommand = new MatchPersonCommand(INDEX_FIRST_PERSON, outOfBoundIndex);

        assertCommandFailure(matchCommand, model, Messages.MESSAGE_INVALID_PREFERENCE_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        MatchPersonCommand matchFirstCommand = new MatchPersonCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        MatchPersonCommand matchSecondCommand = new MatchPersonCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON);

        // same object -> returns true
        assertTrue(matchFirstCommand.equals(matchFirstCommand));

        // same values -> returns true
        MatchPersonCommand matchFirstCommandCopy = new MatchPersonCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        assertTrue(matchFirstCommand.equals(matchFirstCommandCopy));

        // different types -> returns false
        assertFalse(matchFirstCommand.equals(1));

        // null -> returns false
        assertFalse(matchFirstCommand.equals(null));

        // different person index -> returns false
        assertFalse(matchFirstCommand.equals(matchSecondCommand));

        // different preference index -> returns false
        MatchPersonCommand matchDifferentPreferenceCommand =
                new MatchPersonCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        assertFalse(matchFirstCommand.equals(matchDifferentPreferenceCommand));
    }

    @Test
    public void toStringMethod() {
        Index personIndex = Index.fromOneBased(1);
        Index preferenceIndex = Index.fromOneBased(2);
        MatchPersonCommand matchCommand = new MatchPersonCommand(personIndex, preferenceIndex);
        String expected = MatchPersonCommand.class.getCanonicalName() + "{targetPersonIndex="
            + personIndex + ", targetPreferenceIndex=" + preferenceIndex + "}";
        assertEquals(expected, matchCommand.toString());
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

    /**
     * Creates a listing that matches the given preference.
     */
    private Listing createMatchingListing(PropertyPreference preference) {
        Tag tag = preference.getTags().iterator().next(); // Get the first tag from the preference

        PriceRange priceRange = new PriceRangeBuilder()
                .withLowerBoundPrice("150000")
                .withUpperBoundPrice("180000")
                .build();

        return new ListingBuilder()
                .withPostalCode("123456")
                .withUnitNumber("12-123")
                .withPriceRange(priceRange)
                .withTags(Collections.singleton(tag))
                .build();
    }
}
