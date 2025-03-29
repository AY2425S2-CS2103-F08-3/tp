package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.predicates.ListingContainsAllTagsPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;

/**
 * Searches for {@code Listing}(s) whose tags contain all specified tags.
 */
public class SearchPropertyByTagCommand extends Command {
    public static final String COMMAND_WORD = "searchProperty";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all properties with all specified tags.\n"
            + "Parameters: " + PREFIX_TAG + "TAG [" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " t/pet-friendly t/pool";
    private static final Logger logger = LogsCenter.getLogger(SearchPropertyByTagCommand.class);
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
        logger.info("Executing SearchPropertyByTagCommand with tags: " + tagsToSearch);

        if (tagsToSearch.isEmpty()) {
            throw new CommandException(String.format(Messages.MESSAGE_SEARCH_PROPERTY_TAG_MISSING_PARAMS,
                    MESSAGE_USAGE));
        }

        // Validate each tag exists
        for (String tagName : tagsToSearch) {
            if (!model.hasTags(Set.of(tagName))) {
                throw new CommandException(String
                        .format(Messages.MESSAGE_TAG_DOES_NOT_EXIST, tagName, MESSAGE_USAGE));
            }
        }

        List<Tag> activeTags = new ArrayList<>();
        for (String tagName : tagsToSearch) {
            activeTags.add(TagRegistry.of().get(tagName));
        }
        Tag.setActiveSearchTags(activeTags);

        ListingContainsAllTagsPredicate predicate = new ListingContainsAllTagsPredicate(tagsToSearch);
        model.updateFilteredListingList(predicate);

        List<Listing> filteredListings = model.getSortedFilteredListingList();

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
