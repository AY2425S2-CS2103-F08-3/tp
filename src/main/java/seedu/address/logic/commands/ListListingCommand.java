package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.COMPARATOR_SHOW_ALL_LISTINGS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_LISTINGS;

import java.util.HashSet;

import seedu.address.model.Model;
import seedu.address.model.search.SearchContext;
import seedu.address.model.search.SearchType;

/**
 * Lists all persons in the address book to the user.
 */
public class ListListingCommand extends Command {

    public static final String COMMAND_WORD = "listListing";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all Listings\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all listings";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        SearchContext searchContext = model.getSearchContext();

        // If current search was a listing search, set search back to none and clear all search tags and price range.
        if (searchContext.getSearchType() == SearchType.LISTING) {
            searchContext.setSearchType(SearchType.NONE);
            searchContext.setActivePriceRange(null);
            searchContext.setActiveSearchTags(new HashSet<>());
        }

        // Update the filtered listing list to show all listings and default sort.
        model.updateFilteredListingList(PREDICATE_SHOW_ALL_LISTINGS);
        model.updateSortedFilteredListingList(COMPARATOR_SHOW_ALL_LISTINGS);

        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListListingCommand)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return ListListingCommand.class.getCanonicalName() + "{}";
    }
}
