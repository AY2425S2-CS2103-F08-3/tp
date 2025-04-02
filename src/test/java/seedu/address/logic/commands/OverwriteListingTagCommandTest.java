package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.OverwriteListingTagCommand.MESSAGE_INVALID_TAGS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.listing.HouseNumber;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.PostalCode;
import seedu.address.model.listing.PropertyName;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code OverwriteListingTagCommand}.
 */
public class OverwriteListingTagCommandTest {
    private Model model;
    private Model expectedModel;
    private Listing sampleListing;
    private Tag sampleTag;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Create a sample listing
        sampleListing = Listing.of(
                new PostalCode("123456"),
                null, // unit number
                new HouseNumber("123"),
                new PriceRange(new Price("100000"), new Price("200000")),
                new PropertyName("Sunny Villa"),
                new HashSet<>(), // empty tags
                new ArrayList<>(), // empty owners
                true
        );

        // Create a sample tag
        sampleTag = new Tag("Luxury", new ArrayList<>(), new ArrayList<>());

        // Add listing and tag to model
        model.addListing(sampleListing);
        sampleListing.addTag(sampleTag);
        model.setListing(sampleListing, sampleListing);
        model.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);

        // Add listing and tag to expected model
        expectedModel.addListing(sampleListing);
        expectedModel.getFilteredListingList().get(0).addTag(sampleTag);
        expectedModel.setListing(expectedModel.getFilteredListingList().get(0),
                expectedModel.getFilteredListingList().get(0));
        expectedModel.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);
    }


    @Test
    public void execute_invalidListingIndex_throwsCommandException() {
        Set<String> tagSet = Set.of("Luxury");
        Set<String> newTagSet = Set.of("Modern");
        OverwriteListingTagCommand command = new OverwriteListingTagCommand(Index.fromOneBased(2), tagSet, newTagSet);

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX,
                OverwriteListingTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidTag_throwsCommandException() {
        Set<String> tagSet = Set.of("InvalidTag");
        Set<String> newTagSet = Set.of("Modern");
        OverwriteListingTagCommand command = new OverwriteListingTagCommand(Index.fromOneBased(1), tagSet, newTagSet);

        assertCommandFailure(command, model, String.format(MESSAGE_INVALID_TAGS,
                OverwriteListingTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        Set<String> tagSet = Set.of("Luxury");
        Set<String> newTagSet = Set.of("Modern");
        OverwriteListingTagCommand overwriteListingTagCommand = new OverwriteListingTagCommand(
                Index.fromOneBased(1), tagSet, newTagSet);

        // same object -> returns true
        assertTrue(overwriteListingTagCommand.equals(overwriteListingTagCommand));

        // same values -> returns true
        OverwriteListingTagCommand overwriteListingTagCommandCopy = new OverwriteListingTagCommand(
                Index.fromOneBased(1), tagSet, newTagSet);
        assertTrue(overwriteListingTagCommand.equals(overwriteListingTagCommandCopy));

        // different types -> returns false
        assertFalse(overwriteListingTagCommand.equals(1));

        // null -> returns false
        assertFalse(overwriteListingTagCommand.equals(null));

        // different listing index -> returns false
        OverwriteListingTagCommand differentListingIndexCommand = new OverwriteListingTagCommand(
                Index.fromOneBased(2), tagSet, newTagSet);
        assertFalse(overwriteListingTagCommand.equals(differentListingIndexCommand));

        // different tag set -> returns false
        OverwriteListingTagCommand differentTagSetCommand = new OverwriteListingTagCommand(
                Index.fromOneBased(1), Set.of("Different"), newTagSet);
        assertFalse(overwriteListingTagCommand.equals(differentTagSetCommand));

        // different new tag set -> returns false
        OverwriteListingTagCommand differentNewTagSetCommand = new OverwriteListingTagCommand(
                Index.fromOneBased(1), tagSet, Set.of("Different"));
        assertFalse(overwriteListingTagCommand.equals(differentNewTagSetCommand));
    }

    @Test
    public void toStringMethod() {
        Set<String> tagSet = Set.of("Luxury");
        Set<String> newTagSet = Set.of("Modern");
        OverwriteListingTagCommand overwriteListingTagCommand = new OverwriteListingTagCommand(
                Index.fromOneBased(1), tagSet, newTagSet);
        String expected = OverwriteListingTagCommand.class.getCanonicalName()
                + "{propertyIndex=" + Index.fromOneBased(1)
                + ", tags=" + tagSet + ", newTags=" + newTagSet + "}";
        assertEquals(expected, overwriteListingTagCommand.toString());
    }
}
