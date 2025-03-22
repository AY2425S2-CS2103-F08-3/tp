package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.predicates.ListingContainsAllTagsPredicate;

/**
 * Searches for properties that contain all specified tags.
 */
public class SearchPropertyByTagCommand extends Command {
    public static final String COMMAND_WORD = "searchProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all properties with all specified tags.\n"
            + "Parameters: " + PREFIX_TAG + "TAG [" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " t/pet-friendly t/pool";

    private final Set<String> tagsToSearch;

    /**
     * Constructs a {@code SearchPropertyByTagCommand} with the given set of tags.
     */
    public SearchPropertyByTagCommand(Set<String> tagsToSearch) {
        requireNonNull(tagsToSearch);
        this.tagsToSearch = tagsToSearch;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (tagsToSearch.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_SEARCH_PROPERTY_TAG_MISSING_PARAMS);
        }

        // Validate each tag exists
        for (String tagName : tagsToSearch) {
            if (!model.hasTags(Set.of(tagName))) {
                throw new CommandException(String.format(Messages.MESSAGE_SEARCH_PROPERTY_TAG_NOT_FOUND, tagName));
            }
        }

        ListingContainsAllTagsPredicate predicate = new ListingContainsAllTagsPredicate(tagsToSearch);
        model.updateFilteredListingList(predicate);

        List<Listing> filteredListings = model.getFilteredListingList();
        if (filteredListings.isEmpty()) {
            return new CommandResult(Messages.MESSAGE_SEARCH_PROPERTY_TAGS_NO_MATCH);
        } else {
            return new CommandResult(String.format(Messages.MESSAGE_SEARCH_PROPERTY_TAGS_SUCCESS,
                    filteredListings.size()));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SearchPropertyByTagCommand
                && tagsToSearch.equals(((SearchPropertyByTagCommand) other).tagsToSearch));
    }
}
