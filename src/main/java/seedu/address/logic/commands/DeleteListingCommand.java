package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;

/**
 * Deletes a {@code Listing} identified using it's displayed index in the address book.
 */
public class DeleteListingCommand extends Command {

    public static final String COMMAND_WORD = "deleteListing";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the listing identified by the index number used in the displayed listing list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Listing: %1$s";

    private final Index targetIndex;

    /**
     * Creates a {@code DeleteListingCommand} to delete the specified {@code Listing}.
     *
     * @param targetIndex of the listing in the filtered listing list to delete
     */
    public DeleteListingCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Listing> lastShownList = model.getSortedFilteredListingList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX);
        }

        Listing toDelete = lastShownList.get(targetIndex.getZeroBased());

        removeListingOwners(toDelete, model);
        removeListingFromTags(toDelete);

        model.deleteListing(toDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(toDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteListingCommand)) {
            return false;
        }

        DeleteListingCommand otherDeleteCommand = (DeleteListingCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    private void removeListingOwners(Listing toDelete, Model model) {
        List<Person> owners = new ArrayList<>(toDelete.getOwners());
        for (Person owner : owners) {
            owner.removeListing(toDelete);
            model.setPerson(owner, owner);
        }
    }

    private void removeListingFromTags(Listing toDelete) {

        TagRegistry tagRegistry = TagRegistry.of();
        Set<Tag> tags = new HashSet<>(toDelete.getTags());

        for (Tag tag: tags) {
            tag.removeListing(toDelete);
            tagRegistry.setTag(tag, tag);
        }
    }
}
