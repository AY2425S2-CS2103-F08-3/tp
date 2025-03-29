package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;

/**
 * Deletes {@code Tag}(s) using a specified tag name(s).
 */
public class DeleteTagCommand extends Command {

    public static final String COMMAND_WORD = "deleteTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the tag identified by the tag name used in the displayed tag list.\n"
            + "Parameters: "
            + "[" + PREFIX_TAG + "TAG]... "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "quiet "
            + PREFIX_TAG + "pet-friendly ";

    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Deleted Tags: %1$s";
    public static final String MESSAGE_INVALID_TAGS = "At least one of the tags given does not exist.\n%1$s";

    private final Set<String> toDelete;

    /**
     * Creates a {@code DeleteTagCommand} to delete set of {@code Tag}.
     *
     * @param tagSet Set of tags to delete.
     */
    public DeleteTagCommand(Set<String> tagSet) {
        this.toDelete = tagSet;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasTags(toDelete)) {
            throw new CommandException(String.format(MESSAGE_INVALID_TAGS, MESSAGE_USAGE));
        }

        TagRegistry tagRegistry = TagRegistry.of();
        Set<Tag> deletedTags = new HashSet<>();
        for (String tagName : toDelete) {
            Tag tagToDelete = tagRegistry.get(tagName);
            removeTagsFromPropertyPreference(tagToDelete, model);
            removeTagsFromListings(tagToDelete, model);
            model.deleteTag(tagToDelete);
            deletedTags.add(tagToDelete);
        }

        model.resetAllLists();

        return new CommandResult(String.format(MESSAGE_DELETE_TAG_SUCCESS, Messages.format(deletedTags)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteTagCommand)) {
            return false;
        }

        DeleteTagCommand otherDeleteCommand = (DeleteTagCommand) other;
        return toDelete.equals(otherDeleteCommand.toDelete);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("tags to delete ", toDelete)
                .toString();
    }

    private void removeTagsFromListings(Tag toDelete, Model model) {

        List<Listing> listings = new ArrayList<>(toDelete.getListings());
        for (Listing listing : listings) {
            listing.removeTag(toDelete);
            model.setListing(listing, listing);
        }
    }

    private void removeTagsFromPropertyPreference(Tag toDelete, Model model) {

        List<PropertyPreference> propertyPreferences = new ArrayList<>(toDelete.getPropertyPreferences());
        for (PropertyPreference propertyPreference : propertyPreferences) {
            propertyPreference.removeTag(toDelete);
            model.setPerson(propertyPreference.getPerson(), propertyPreference.getPerson());
        }

    }
}
