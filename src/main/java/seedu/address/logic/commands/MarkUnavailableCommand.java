package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;

/**
 * Mark unavailable a {@code Listing} identified using it's displayed index in the address book.
 */
public class MarkUnavailableCommand extends Command {
    public static final String COMMAND_WORD = "markUnavailable";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the listing as unavailable. "
            + "Parameters: INDEX (must be a positive integer) "
            + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_MARK_UNAVAILABLE_SUCCESS = "Marked Listing as unavailable: %1$s";

    private final Index targetIndex;

    /**
     * Creates a {@code MarkUnavailableCommand} to mark the specified {@code Listing} as unavailable.
     *
     * @param targetIndex of the listing in the filtered listing list to mark unavailable.
     */
    public MarkUnavailableCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Listing> lastShownList = model.getSortedFilteredListingList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX, MESSAGE_USAGE));
        }

        Listing toMarkUnavailable = lastShownList.get(targetIndex.getZeroBased());
        toMarkUnavailable.markUnavailable();
        model.setListing(toMarkUnavailable, toMarkUnavailable);

        model.resetAllLists();

        return new CommandResult(String.format(MESSAGE_MARK_UNAVAILABLE_SUCCESS,
                Messages.format(toMarkUnavailable.getAvailability(), toMarkUnavailable)));
    }
}
