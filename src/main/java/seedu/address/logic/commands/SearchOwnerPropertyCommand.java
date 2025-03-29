package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.predicates.ListingContainsOwnerPredicate;
import seedu.address.model.person.Person;

/**
 * Searches for {@code Listing}(s) that are owned by a {@code Person} identified using it's displayed index.
 */
public class SearchOwnerPropertyCommand extends Command {

    public static final String COMMAND_WORD = "searchOwnerProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds all properties owned by a person identified by the index number "
            + "used in the displayed person list.\n"
            + "Parameters: PERSON_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SEARCH_SELLER_PROPERTY_SUCCESS = "Searched Properties of Person: %1$s";

    private final Index targetIndex;

    /**
     * Creates a {@code SearchOwnerPropertyCommand} to list the specified {@code Person}'s owned
     * {@code Listing}s.
     *
     * @param targetIndex of the listing in the filtered listing list to delete
     */
    public SearchOwnerPropertyCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * Executes the search command and updates the filtered listing list in the model.
     *
     * @param model {@code Model} which the command should operate on.
     * @return CommandResult with the number of matches found.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, MESSAGE_USAGE));
        }

        Person targetPerson = lastShownList.get(targetIndex.getZeroBased());

        searchSellerProperty(model, targetPerson);

        return new CommandResult(String.format(MESSAGE_SEARCH_SELLER_PROPERTY_SUCCESS, Messages.format(targetPerson)));
    }

    private void searchSellerProperty(Model model, Person targetPerson) {
        Predicate<Listing> predicate = new ListingContainsOwnerPredicate(targetPerson);
        model.updateFilteredListingList(predicate);
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

        SearchOwnerPropertyCommand otherDeletePersonCommand = (SearchOwnerPropertyCommand) other;
        return targetIndex.equals(otherDeletePersonCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
