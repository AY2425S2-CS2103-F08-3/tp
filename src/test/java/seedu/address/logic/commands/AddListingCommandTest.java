package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
 * {@code AddListingCommand}.
 */
public class AddListingCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
        expectedModel = new ModelManager(new AddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newListing_success() {
        // Create a new listing
        PostalCode postalCode = new PostalCode("123456");
        HouseNumber houseNumber = new HouseNumber("123");
        PriceRange priceRange = new PriceRange(new Price("100000"), new Price("200000"));
        PropertyName propertyName = new PropertyName("Sunny Villa");
        Set<Tag> tags = new HashSet<>();
        Listing listing = new Listing(postalCode, houseNumber, priceRange, propertyName, tags, new ArrayList<>(), true);

        // Create sets for existing and new tags
        Set<String> tagSet = new HashSet<>();
        Set<String> newTagSet = new HashSet<>();
        newTagSet.add("modern");
        newTagSet.add("luxury");

        AddListingCommand command = new AddListingCommand(listing, tagSet, newTagSet);

        // Create expected model state
        expectedModel.addTags(newTagSet);
        for (String tagName : newTagSet) {
            Tag tag = expectedModel.getTag(tagName);
            tag.addListing(listing);
            expectedModel.setTag(tag, tag);
            listing.addTag(tag);
        }
        expectedModel.addListing(listing);
        expectedModel.resetAllLists();

        String expectedMessage = String.format(AddListingCommand.MESSAGE_SUCCESS, Messages.format(listing));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateListing_throwsCommandException() {
        // Create a listing and add it to the model first
        PostalCode postalCode = new PostalCode("123456");
        HouseNumber houseNumber = new HouseNumber("123");
        PriceRange priceRange = new PriceRange(new Price("100000"), new Price("200000"));
        PropertyName propertyName = new PropertyName("Sunny Villa");
        Set<Tag> tags = new HashSet<>();
        Listing listing = new Listing(postalCode, houseNumber, priceRange, propertyName, tags, new ArrayList<>(), true);
        model.addListing(listing);

        Set<String> tagSet = new HashSet<>();
        Set<String> newTagSet = new HashSet<>();

        AddListingCommand command = new AddListingCommand(listing, tagSet, newTagSet);

        assertCommandFailure(command, model, String.format(AddListingCommand.MESSAGE_DUPLICATE_LISTING,
                AddListingCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_invalidTag_throwsCommandException() {
        // Create a new listing
        PostalCode postalCode = new PostalCode("123456");
        HouseNumber houseNumber = new HouseNumber("123");
        PriceRange priceRange = new PriceRange(new Price("100000"), new Price("200000"));
        PropertyName propertyName = new PropertyName("Sunny Villa");
        Set<Tag> tags = new HashSet<>();
        Listing listing = new Listing(postalCode, houseNumber, priceRange, propertyName, tags, new ArrayList<>(), true);

        // Create sets for existing and new tags
        Set<String> tagSet = new HashSet<>();
        tagSet.add("nonexistentTag"); // This tag doesn't exist in the model
        Set<String> newTagSet = new HashSet<>();

        AddListingCommand command = new AddListingCommand(listing, tagSet, newTagSet);

        assertCommandFailure(command, model, String.format(AddListingCommand.MESSAGE_INVALID_TAGS,
                AddListingCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_duplicateNewTag_throwsCommandException() {
        // Create a new listing
        PostalCode postalCode = new PostalCode("123456");
        HouseNumber houseNumber = new HouseNumber("123");
        PriceRange priceRange = new PriceRange(new Price("100000"), new Price("200000"));
        PropertyName propertyName = new PropertyName("Sunny Villa");
        Set<Tag> tags = new HashSet<>();
        Listing listing = new Listing(postalCode, houseNumber, priceRange, propertyName, tags, new ArrayList<>(), true);

        // Create sets for existing and new tags
        Set<String> tagSet = new HashSet<>();
        Set<String> newTagSet = new HashSet<>();
        newTagSet.add("modern"); // This tag already exists in the model

        // Add the tag to the model first
        model.addTags(Set.of("modern"));

        AddListingCommand command = new AddListingCommand(listing, tagSet, newTagSet);

        assertCommandFailure(command, model, String.format(AddListingCommand.MESSAGE_DUPLICATE_TAGS,
                AddListingCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        // Create two listings with the same details
        PostalCode postalCode = new PostalCode("123456");
        HouseNumber houseNumber = new HouseNumber("123");
        PriceRange priceRange = new PriceRange(new Price("100000"), new Price("200000"));
        PropertyName propertyName = new PropertyName("Sunny Villa");
        Set<Tag> tags = new HashSet<>();
        Listing listing = new Listing(postalCode, houseNumber, priceRange, propertyName, tags, new ArrayList<>(), true);

        Set<String> tagSet = new HashSet<>();
        Set<String> newTagSet = new HashSet<>();
        AddListingCommand addListingCommand = new AddListingCommand(listing, tagSet, newTagSet);

        // same object -> returns true
        assertTrue(addListingCommand.equals(addListingCommand));

        // same values -> returns true
        AddListingCommand addListingCommandCopy = new AddListingCommand(listing, tagSet, newTagSet);
        assertTrue(addListingCommand.equals(addListingCommandCopy));

        // different types -> returns false
        assertFalse(addListingCommand.equals(1));

        // null -> returns false
        assertFalse(addListingCommand.equals(null));

        // different listing -> returns false
        PostalCode differentPostalCode = new PostalCode("654321");
        Listing differentListing = new Listing(differentPostalCode, houseNumber, priceRange, propertyName, tags,
                new ArrayList<>(), true);
        AddListingCommand differentAddListingCommand = new AddListingCommand(differentListing, tagSet, newTagSet);
        assertFalse(addListingCommand.equals(differentAddListingCommand));
    }

    @Test
    public void toStringMethod() {
        // Create a listing
        PostalCode postalCode = new PostalCode("123456");
        HouseNumber houseNumber = new HouseNumber("123");
        PriceRange priceRange = new PriceRange(new Price("100000"), new Price("200000"));
        PropertyName propertyName = new PropertyName("Sunny Villa");
        Set<Tag> tags = new HashSet<>();
        Listing listing = new Listing(postalCode, houseNumber, priceRange, propertyName, tags, new ArrayList<>(), true);

        Set<String> tagSet = new HashSet<>();
        Set<String> newTagSet = new HashSet<>();
        AddListingCommand addListingCommand = new AddListingCommand(listing, tagSet, newTagSet);
        String expected = AddListingCommand.class.getCanonicalName() + "{toAdd=" + listing + ", tagSet=" + tagSet
                + ", newTagSet=" + newTagSet + "}";
        assertEquals(expected, addListingCommand.toString());
    }
}
