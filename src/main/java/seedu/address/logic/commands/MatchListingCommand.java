package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.SearchType;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.person.comparators.PersonListingScoreComparator;
import seedu.address.model.person.predicates.PersonMatchesPropertyPredicate;
import seedu.address.model.person.predicates.PropertyPreferencesContainAnyActiveSearchTagsPredicate;

/**
 * Matches a {@code Listing} identified using it's displayed index in the address book to
 * {@code Person}s' @code PropertyPreference}.
 */
public class MatchListingCommand extends Command {

    public static final String COMMAND_WORD = "matchListing";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Matches the listing identified by the index number used in the displayed listing list"
            + " to a Persons' property preferences.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MATCH_LISTING_SUCCESS = "Matched Listing: %1$s";

    private final Index targetIndex;

    /**
     * Creates a {@code MatchListingCommand} to match the specified {@code Listing}
     * to {@code Person}s' @code PropertyPreference}.
     *
     * @param targetIndex of the listing in the filtered listing list to delete
     */
    public MatchListingCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Listing> lastShownList = model.getSortedFilteredListingList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX, MESSAGE_USAGE));
        }

        Listing toMatch = lastShownList.get(targetIndex.getZeroBased());
        matchListing(model, toMatch);

        return new CommandResult(String.format(MESSAGE_MATCH_LISTING_SUCCESS, Messages.format(toMatch)));
    }

    private void matchListing(Model model, Listing listingToMatch) {
        requireAllNonNull(model, listingToMatch);

        model.resetAllLists();

        model.setSearch(listingToMatch.getTags().stream().toList(),
                listingToMatch.getPriceRange(),
                SearchType.PERSON);

        PropertyPreference.setFilterPredicate(new PropertyPreferencesContainAnyActiveSearchTagsPredicate());
        model.updateFilteredPersonList(new PersonMatchesPropertyPredicate(listingToMatch));
        model.updateSortedFilteredPersonList(new PersonListingScoreComparator(listingToMatch));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MatchListingCommand)) {
            return false;
        }

        MatchListingCommand otherDeleteCommand = (MatchListingCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
