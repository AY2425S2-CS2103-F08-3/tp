package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TAGS;

import seedu.address.model.Model;

/**
 * Lists all tags in the address book to the user.
 */
public class ListTagCommand extends Command {

    public static final String COMMAND_WORD = "listTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all Tags\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all tags";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.updateFilteredTagList(PREDICATE_SHOW_ALL_TAGS);

        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListTagCommand)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return ListTagCommand.class.getCanonicalName() + "{}";
    }
}
