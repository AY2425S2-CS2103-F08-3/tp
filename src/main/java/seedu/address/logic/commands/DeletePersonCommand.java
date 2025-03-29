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
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;

/**
 * Deletes a {@code Person} identified using it's displayed index from the address book.
 */
public class DeletePersonCommand extends Command {

    public static final String COMMAND_WORD = "deletePerson";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private final Index targetIndex;

    /**
     * Creates a {@code DeletePersonCommand} to delete the specified {@code Person}.
     *
     * @param targetIndex of the listing in the filtered listing list to delete
     */
    public DeletePersonCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getSortedFilteredPersonList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, MESSAGE_USAGE));
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());

        removeListingOwnership(personToDelete, model);
        removePersonPropertyPreferenceFromTags(personToDelete);

        model.deletePerson(personToDelete);
        model.resetAllLists();

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeletePersonCommand)) {
            return false;
        }

        DeletePersonCommand otherDeletePersonCommand = (DeletePersonCommand) other;
        return targetIndex.equals(otherDeletePersonCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }

    private void removeListingOwnership(Person personToDelete, Model model) {
        List<Listing> listings = new ArrayList<>(personToDelete.getListings());
        for (Listing listing : listings) {
            listing.removeOwner(personToDelete);
            model.setListing(listing, listing);
        }
    }

    private void removePersonPropertyPreferenceFromTags(Person personToDelete) {

        TagRegistry tagRegistry = TagRegistry.of();

        List<PropertyPreference> propertyPreferences = new ArrayList<>(personToDelete.getPropertyPreferences());
        for (PropertyPreference propertyPreference : propertyPreferences) {
            Set<Tag> tags = new HashSet<>(propertyPreference.getTags());

            for (Tag tag: tags) {
                tag.removePropertyPreference(propertyPreference);
                tagRegistry.setTag(tag, tag);
            }
        }
    }
}
