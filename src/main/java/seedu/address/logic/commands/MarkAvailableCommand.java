package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;

/**
 * Mark available a {@code Listing} identified using it's displayed index in the address book.
 */
public class MarkAvailableCommand extends Command {
    public static final String COMMAND_WORD = "markAvailable";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the listing as available. "
            + "Parameters: INDEX (must be a positive integer) "
            + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_MARK_AVAILABLE_SUCCESS = "Marked Listing as available: %1$s";

    private final Index targetIndex;

    /**
     * Creates a {@code MarkAvailableCommand} to mark the specified {@code Listing} as available.
     *
     * @param targetIndex of the listing in the filtered listing list to mark available.
     */
    public MarkAvailableCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Listing> lastShownList = model.getSortedFilteredListingList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX, MESSAGE_USAGE));
        }

        Listing toMarkAvailable = lastShownList.get(targetIndex.getZeroBased());
        toMarkAvailable.markAvailable();
        model.setListing(toMarkAvailable, toMarkAvailable);

        model.resetAllLists();

        return new CommandResult(String.format(MESSAGE_MARK_AVAILABLE_SUCCESS,
                Messages.format(toMarkAvailable.getAvailability(), toMarkAvailable)));
    }
}
