package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TAGS;

import seedu.address.model.Model;

/**
 * Lists all {@code Tag}(s) in the address book.
 */
public class ListTagCommand extends Command {

    public static final String COMMAND_WORD = "listTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows a list of all tags."
            + "\nExample: "
            + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all tags";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.updateFilteredTagList(PREDICATE_SHOW_ALL_TAGS);

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
