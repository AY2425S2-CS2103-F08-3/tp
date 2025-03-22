package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.PostalCode;
import seedu.address.model.listing.UnitNumber;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;

public class SearchPropertyByTagCommandTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());

        // Clear registry first to avoid duplicates
        Set<String> keys = Set.copyOf(TagRegistry.of().asUnmodifiableObservableMap().keySet());
        for (String key : keys) {
            Tag tag = TagRegistry.of().get(key);
            TagRegistry.of().remove(tag);
        }

        // Add tags
        Tag petFriendly = new Tag("pet-friendly", List.of(), List.of());
        Tag pool = new Tag("pool", List.of(), List.of());
        TagRegistry.of().add(petFriendly);
        TagRegistry.of().add(pool);

        // Add sample listing
        Listing listing = Listing.of(
                new PostalCode("123456"),
                new UnitNumber("10-123"),
                null,
                new PriceRange(new Price("500000"), new Price("800000")),
                null,
                Set.of(petFriendly, pool),
                List.of(new Person(
                        new Name("John Doe"),
                        new Phone("98765432"),
                        new Email("john@example.com"),
                        List.of(),
                        List.of()
                ))
        );
        model.addListing(listing);
    }

    @Test
    public void execute_validTag_success() throws Exception {
        Set<String> tagsToSearch = Set.of("pet-friendly", "pool");
        SearchPropertyByTagCommand command = new SearchPropertyByTagCommand(tagsToSearch);

        CommandResult result = command.execute(model);
        assertEquals("1 properties matching the tags!", result.getFeedbackToUser());
    }

    @Test
    public void execute_tagNotFound_throwsCommandException() {
        Set<String> tagsToSearch = Set.of("nonexistenttag");
        SearchPropertyByTagCommand command = new SearchPropertyByTagCommand(tagsToSearch);

        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(Messages.MESSAGE_SEARCH_PROPERTY_TAG_NOT_FOUND,
                "nonexistenttag"), thrown.getMessage());
    }

    @Test
    public void execute_noMatch_successMessageNoMatch() throws Exception {
        // Search for tag not in listings but present in registry
        TagRegistry.of().add(new Tag("garden", List.of(), List.of()));
        Set<String> tagsToSearch = Set.of("garden");
        SearchPropertyByTagCommand command = new SearchPropertyByTagCommand(tagsToSearch);

        CommandResult result = command.execute(model);
        assertEquals(Messages.MESSAGE_SEARCH_PROPERTY_TAGS_NO_MATCH, result.getFeedbackToUser());
    }

    @Test
    public void execute_noParams_throwsCommandException() {
        SearchPropertyByTagCommand command = new SearchPropertyByTagCommand(Set.of());

        CommandException thrown = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(Messages.MESSAGE_SEARCH_PROPERTY_TAG_MISSING_PARAMS, thrown.getMessage());
    }
}
