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
import seedu.address.model.listing.HouseNumber;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.PostalCode;
import seedu.address.model.listing.PropertyName;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code AddListingTagCommand}.
 */
public class AddListingTagCommandTest {
    private Model model;
    private Listing sampleListing;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());

        // Create a sample owner
        Person sampleOwner = new Person(
                new Name("John Doe"),
                new Phone("98765432"),
                new Email("johndoe@example.com"),
                List.of(), // Empty property preferences
                List.of() // Empty listings
        );

        // Add some existing tags to the model
        model.addTags(Set.of("Luxury", "Pool"));

        // Create listing without tags
        sampleListing = Listing.of(
                new PostalCode("123456"),
                null, // unit number
                new HouseNumber("123"),
                new PriceRange(new Price("100000"), new Price("200000")),
                new PropertyName("Sunny Villa"),
                new HashSet<>(), // empty tags
                List.of(sampleOwner),
                true
        );

        // Add listing to model
        model.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);
        model.addListing(sampleListing);
    }

    @Test
    public void execute_addExistingTags_success() {
        // Create sets for existing tags
        Set<String> tagSet = new HashSet<>();
        tagSet.add("Luxury");
        tagSet.add("Pool");
        Set<String> newTagSet = new HashSet<>();

        AddListingTagCommand command = new AddListingTagCommand(Index.fromOneBased(1), tagSet, newTagSet);

        // Create expected model state
        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Set up the tag registry first
        expectedModel.addTags(Set.of("Luxury", "Pool"));

        // Create a copy of the listing for the expected model
        Listing expectedListing = Listing.of(
                new PostalCode("123456"),
                null,
                new HouseNumber("123"),
                new PriceRange(new Price("100000"), new Price("200000")),
                new PropertyName("Sunny Villa"),
                new HashSet<>(),
                List.of(new Person(new Name("John Doe"), new Phone("98765432"),
                        new Email("johndoe@example.com"), List.of(), List.of())),
                true
        );

        // Add the listing to the expected model
        expectedModel.addListing(expectedListing);
        expectedModel.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);
        expectedModel.resetAllLists();

        // Add tags to the listing in expected model
        for (String tagName : tagSet) {
            Tag tag = expectedModel.getTag(tagName);
            tag.addListing(expectedListing);
            expectedModel.setTag(tag, tag);
            expectedListing.addTag(tag);
        }
        expectedModel.setListing(expectedListing, expectedListing);

        String expectedMessage = String.format(AddListingTagCommand.MESSAGE_SUCCESS,
                Messages.format(expectedListing.getTags(), expectedListing));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addNewTags_success() {
        // Create sets for new tags
        Set<String> tagSet = new HashSet<>();
        Set<String> newTagSet = new HashSet<>();
        newTagSet.add("Modern");
        newTagSet.add("Spacious");

        AddListingTagCommand command = new AddListingTagCommand(Index.fromOneBased(1), tagSet, newTagSet);

        // Create expected model state
        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Set up the tag registry first
        expectedModel.addTags(Set.of("Luxury", "Pool"));
        expectedModel.addTags(newTagSet);

        // Create a copy of the listing for the expected model
        Listing expectedListing = Listing.of(
                new PostalCode("123456"),
                null,
                new HouseNumber("123"),
                new PriceRange(new Price("100000"), new Price("200000")),
                new PropertyName("Sunny Villa"),
                new HashSet<>(),
                List.of(new Person(new Name("John Doe"), new Phone("98765432"),
                        new Email("johndoe@example.com"), List.of(), List.of())),
                true
        );

        // Add the listing to the expected model
        expectedModel.addListing(expectedListing);
        expectedModel.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);
        expectedModel.resetAllLists();

        // Add tags to the listing in expected model
        for (String tagName : newTagSet) {
            Tag tag = expectedModel.getTag(tagName);
            tag.addListing(expectedListing);
            expectedModel.setTag(tag, tag);
            expectedListing.addTag(tag);
        }
        expectedModel.setListing(expectedListing, expectedListing);

        String expectedMessage = String.format(AddListingTagCommand.MESSAGE_SUCCESS,
                Messages.format(expectedListing.getTags(), expectedListing));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addMixedTags_success() {
        // Create sets for both existing and new tags
        Set<String> tagSet = new HashSet<>();
        tagSet.add("Luxury");
        Set<String> newTagSet = new HashSet<>();
        newTagSet.add("Modern");

        AddListingTagCommand command = new AddListingTagCommand(Index.fromOneBased(1), tagSet, newTagSet);

        // Create expected model state
        Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

        // Set up the tag registry first
        expectedModel.addTags(Set.of("Luxury", "Pool"));
        expectedModel.addTags(newTagSet);

        // Create a copy of the listing for the expected model
        Listing expectedListing = Listing.of(
                new PostalCode("123456"),
                null,
                new HouseNumber("123"),
                new PriceRange(new Price("100000"), new Price("200000")),
                new PropertyName("Sunny Villa"),
                new HashSet<>(),
                    List.of(new Person(new Name("John Doe"), new Phone("98765432"),
                        new Email("johndoe@example.com"), List.of(), List.of())),
                true
        );

        // Add the listing to the expected model
        expectedModel.addListing(expectedListing);
        expectedModel.updateFilteredListingList(Model.PREDICATE_SHOW_ALL_LISTINGS);
        expectedModel.resetAllLists();

        // Add tags to the listing in expected model
        for (String tagName : tagSet) {
            Tag tag = expectedModel.getTag(tagName);
            tag.addListing(expectedListing);
            expectedModel.setTag(tag, tag);
            expectedListing.addTag(tag);
        }
        for (String tagName : newTagSet) {
            Tag tag = expectedModel.getTag(tagName);
            tag.addListing(expectedListing);
            expectedModel.setTag(tag, tag);
            expectedListing.addTag(tag);
        }
        expectedModel.setListing(expectedListing, expectedListing);

        String expectedMessage = String.format(AddListingTagCommand.MESSAGE_SUCCESS,
                Messages.format(expectedListing.getTags(), expectedListing));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Set<String> tagSet = new HashSet<>();
        Set<String> newTagSet = new HashSet<>();
        AddListingTagCommand command = new AddListingTagCommand(Index.fromOneBased(2), tagSet, newTagSet);

        assertCommandFailure(command, model, String.format(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX,
                AddListingTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidTag_throwsCommandException() {
        Set<String> tagSet = new HashSet<>();
        tagSet.add("NonexistentTag");
        Set<String> newTagSet = new HashSet<>();

        AddListingTagCommand command = new AddListingTagCommand(Index.fromOneBased(1), tagSet, newTagSet);

        assertCommandFailure(command, model, String.format(AddListingTagCommand.MESSAGE_INVALID_TAGS,
                AddListingTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_duplicateNewTag_throwsCommandException() {
        // Add a tag to the model first
        model.addTags(Set.of("Modern"));

        Set<String> tagSet = new HashSet<>();
        Set<String> newTagSet = new HashSet<>();
        newTagSet.add("Modern");

        AddListingTagCommand command = new AddListingTagCommand(Index.fromOneBased(1), tagSet, newTagSet);

        assertCommandFailure(command, model, String.format(AddListingTagCommand.MESSAGE_DUPLICATE_TAGS,
                AddListingTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_duplicateTagInListing_throwsCommandException() {
        // First add a tag to the listing
        Tag luxuryTag = model.getTag("Luxury");
        luxuryTag.addListing(sampleListing);
        model.setTag(luxuryTag, luxuryTag);
        sampleListing.addTag(luxuryTag);
        model.setListing(sampleListing, sampleListing);

        // Try to add the same tag again
        Set<String> tagSet = new HashSet<>();
        tagSet.add("Luxury");
        Set<String> newTagSet = new HashSet<>();

        AddListingTagCommand command = new
            AddListingTagCommand(Index.fromOneBased(1), tagSet, newTagSet);

        assertCommandFailure(command, model,
            String.format(AddListingTagCommand.MESSAGE_DUPLICATE_TAGS_IN_LISTING,
                AddListingTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        Set<String> tagSet = new HashSet<>();
        Set<String> newTagSet = new HashSet<>();
        AddListingTagCommand addListingTagCommand = new
            AddListingTagCommand(Index.fromOneBased(1), tagSet, newTagSet);

        // same object -> returns true
        assertTrue(addListingTagCommand.equals(addListingTagCommand));

        // same values -> returns true
        AddListingTagCommand addListingTagCommandCopy =
            new AddListingTagCommand(Index.fromOneBased(1), tagSet, newTagSet);
        assertTrue(addListingTagCommand.equals(addListingTagCommandCopy));

        // different types -> returns false
        assertFalse(addListingTagCommand.equals(1));

        // null -> returns false
        assertFalse(addListingTagCommand.equals(null));

        // different index -> returns false
        AddListingTagCommand differentAddListingTagCommand =
            new AddListingTagCommand(Index.fromOneBased(2), tagSet, newTagSet);
        assertFalse(addListingTagCommand.equals(differentAddListingTagCommand));
    }

    @Test
    public void toStringMethod() {
        Set<String> tagSet = new HashSet<>();
        Set<String> newTagSet = new HashSet<>();
        AddListingTagCommand addListingTagCommand =
            new AddListingTagCommand(Index.fromOneBased(1), tagSet, newTagSet);
        String expected = AddListingTagCommand.class.getCanonicalName()
            + "{Index=" + Index.fromOneBased(1) + "}";
        assertEquals(expected, addListingTagCommand.toString());
    }
}
