package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HOUSE_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOWER_BOUND_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROPERTY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UNIT_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UPPER_BOUND_PRICE;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;

/**
 * Adds a {@code Listing} to the address book.
 */
public class AddListingCommand extends Command {
    public static final String COMMAND_WORD = "addListing";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a listing to the address book. "
            + "Parameters: "
            + PREFIX_POSTAL_CODE + "POSTAL_CODE "
            + "[" + PREFIX_UNIT_NUMBER + "UNIT_NUMBER] "
            + "[" + PREFIX_HOUSE_NUMBER + "HOUSE_NUMBER] "
            + "[" + PREFIX_LOWER_BOUND_PRICE + "LOWER_BOUND_PRICE] "
            + "[" + PREFIX_UPPER_BOUND_PRICE + "UPPER_BOUND_PRICE] "
            + "[" + PREFIX_PROPERTY_NAME + "PROPERTY_NAME] "
            + "[" + PREFIX_TAG + "TAG]... "
            + "[" + PREFIX_NEW_TAG + "NEW_TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_POSTAL_CODE + "654321 "
            + PREFIX_HOUSE_NUMBER + "12 "
            + PREFIX_LOWER_BOUND_PRICE + "300000 "
            + PREFIX_UPPER_BOUND_PRICE + "600000 "
            + PREFIX_PROPERTY_NAME + "Sunny Villa "
            + PREFIX_TAG + "quiet "
            + PREFIX_TAG + "pet-friendly "
            + PREFIX_NEW_TAG + "family-friendly "
            + PREFIX_NEW_TAG + "spacious";

    public static final String MESSAGE_SUCCESS = "New listing added: %1$s";
    public static final String MESSAGE_DUPLICATE_LISTING = "This listing already exists in the address book.\n%1$s";
    public static final String MESSAGE_DUPLICATE_TAGS = "At least one of the new tags given already exist.\n%1$s";
    public static final String MESSAGE_INVALID_TAGS = "At least one of the tags given does not exist.\n%1$s";

    private final Listing toAdd;
    private final Set<String> tagSet;
    private final Set<String> newTagSet;

    /**
     * Creates an {@code AddListingCommand} to add the specified {@code Listing}.
     *
     * @param listing Listing to be added.
     * @param tagSet Set of tags to be added to the listing.
     * @param newTagSet Set of new tags to be added to the tag registry.
     */
    public AddListingCommand(Listing listing, Set<String> tagSet, Set<String> newTagSet) {
        requireNonNull(listing);
        toAdd = listing;
        this.tagSet = tagSet;
        this.newTagSet = newTagSet;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasTags(tagSet)) {
            throw new CommandException(String.format(MESSAGE_INVALID_TAGS, MESSAGE_USAGE));
        }

        if (model.hasNewTags(newTagSet)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_TAGS, MESSAGE_USAGE));
        }

        if (model.hasListing(toAdd)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_LISTING, MESSAGE_USAGE));
        }

        model.addTags(newTagSet);
        Set<String> tagNames = new HashSet<>(tagSet);
        tagNames.addAll(newTagSet);

        TagRegistry tagRegistry = TagRegistry.of();
        for (String tagName: tagNames) {
            Tag tag = tagRegistry.get(tagName);
            tag.addListing(toAdd);
            tagRegistry.setTag(tag, tag);
            toAdd.addTag(tagRegistry.get(tagName));
        }

        model.addListing(toAdd);
        model.resetAllLists();

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddListingCommand)) {
            return false;
        }

        AddListingCommand otherAddListingCommand = (AddListingCommand) other;
        return toAdd.equals(otherAddListingCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .add("tagSet", tagSet)
                .add("newTagSet", newTagSet)
                .toString();
    }

}
