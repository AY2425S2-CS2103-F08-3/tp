package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
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
 * Deletes {@code Tag}(s) from a {@code Listing} identified using it's displayed index in the address book.
 */
public class DeletePropertyTagCommand extends Command {

    public static final String COMMAND_WORD = "deletePropertyTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes tags from a property identified "
            + "by the index number used in the displayed property list.\n"
            + "Parameters: PROPERTY_INDEX (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 3 " + PREFIX_TAG + "pet-friendly " + PREFIX_TAG + "pool";

    private final Index propertyIndex;
    private final Set<String> tagsToDelete;

    /**
     * Creates a {@code DeletePreferenceTagCommand} to delete a set of {@code Tag} from the specified {@code Listing}.
     *
     * @param propertyIndex The index of the property from which tags will be removed.
     * @param tagsToDelete  The set of tag names to be deleted.
     */
    public DeletePropertyTagCommand(Index propertyIndex, Set<String> tagsToDelete) {
        requireNonNull(propertyIndex);
        requireNonNull(tagsToDelete);

        this.propertyIndex = propertyIndex;
        this.tagsToDelete = tagsToDelete;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Listing> lastShownList = model.getSortedFilteredListingList();

        if (propertyIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX);
        }

        Listing listingToEdit = lastShownList.get(propertyIndex.getZeroBased());

        TagRegistry tagRegistry = TagRegistry.of();
        Set<Tag> deletedTags = new HashSet<>();

        // Modify listing and tag registry in place
        for (String tagName : tagsToDelete) {
            Tag tagToDelete = tagRegistry.get(tagName);
            if (!listingToEdit.getTags().contains(tagToDelete)) {
                throw new CommandException(Messages.MESSAGE_TAG_NOT_FOUND);
            }
            deletedTags.add(tagToDelete);
        }

        for (Tag tag : deletedTags) {
            listingToEdit.removeTag(tag);
            model.setListing(listingToEdit, listingToEdit);
            tag.removeListing(listingToEdit);
            tagRegistry.setTag(tag, tag);
            deletedTags.add(tag);
        }
        return new CommandResult(String.format(Messages.MESSAGE_DELETE_PROPERTY_TAG_SUCCESS,
                listingToEdit.getPostalCode(), Messages.format(deletedTags)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeletePropertyTagCommand
                && propertyIndex.equals(((DeletePropertyTagCommand) other).propertyIndex)
                && tagsToDelete.equals(((DeletePropertyTagCommand) other).tagsToDelete));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("propertyIndex", propertyIndex)
                .add("tagsToDelete", tagsToDelete)
                .toString();
    }
}
