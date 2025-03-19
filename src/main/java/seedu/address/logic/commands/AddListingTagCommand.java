package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;

/**
 * Adds tags to a listing in the addressbook
 */
public class AddListingTagCommand extends Command {
    public static final String COMMAND_WORD = "addListingTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds tags to listing "
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG]... "
            + "[" + PREFIX_NEW_TAG + "NEW_TAG]...\n"
            + "Example: " + COMMAND_WORD + " 2 "
            + PREFIX_TAG + "quiet "
            + PREFIX_TAG + "pet-friendly "
            + PREFIX_NEW_TAG + "family-friendly "
            + PREFIX_NEW_TAG + "spacious";

    public static final String MESSAGE_SUCCESS = "Adds tags to listing %1$s";
    public static final String MESSAGE_DUPLICATE_TAGS = "At least one of the new tags given already exist.";
    public static final String MESSAGE_INVALID_TAGS = "At least one of the tags given does not exist.";
    public static final String MESSAGE_DUPLICATE_TAGS_IN_LISTING = "At least one of the "
            + "tags given already exist in the listing.";

    private final Index index;
    private final Set<String> tagSet;
    private final Set<String> newTagSet;

    /**
     * Creates an AddPersonCommand to add the specified {@code Person}
     */
    public AddListingTagCommand(Index index, Set<String> tags,
                                Set<String> newTags) {
        this.index = index;
        tagSet = tags;
        newTagSet = newTags;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Listing> lastShownList = model.getFilteredListingList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX);
        }

        if (!model.hasTags(tagSet)) {
            throw new CommandException(MESSAGE_INVALID_TAGS);
        }

        if (model.hasNewTags(newTagSet)) {
            throw new CommandException(MESSAGE_DUPLICATE_TAGS);
        }
        model.addTags(newTagSet);

        Listing listingToAddTags = lastShownList.get(index.getZeroBased());
        TagRegistry tagRegistry = TagRegistry.of();
        Set<String> tagNames = new HashSet<>(tagSet);
        tagNames.addAll(newTagSet);

        for (String tagName : tagNames) {
            Tag tag = tagRegistry.get(tagName);
            if (listingToAddTags.getTags().contains(tag)) {
                throw new CommandException(MESSAGE_DUPLICATE_TAGS_IN_LISTING);
            }
            tag.addListing(listingToAddTags);
            tagRegistry.setTag(tag, tag);

            listingToAddTags.addTag(tag);
        }

        model.setListing(listingToAddTags, listingToAddTags);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                Messages.format(listingToAddTags.getTags(), listingToAddTags)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddListingTagCommand)) {
            return false;
        }

        AddListingTagCommand otherAddListingTagCommand = (AddListingTagCommand) other;
        return index.equals(otherAddListingTagCommand.index);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Index", index)
                .toString();
    }

}
