package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/**
 * Adds a {@code Tag} to the address book.
 */
public class AddTagCommand extends Command {
    public static final String COMMAND_WORD = "addTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds new tags to the system."
            + "\nParameters: "
            + PREFIX_NEW_TAG + "NEW_TAG..."
            + "\nExample: "
            + COMMAND_WORD + " "
            + PREFIX_NEW_TAG + "family-friendly "
            + PREFIX_NEW_TAG + "spacious";

    public static final String MESSAGE_SUCCESS = "Tag added: %1$s";
    public static final String MESSAGE_DUPLICATE_TAGS = "At least one of the tags already exists in the address book."
            + "\n%1$s";

    private final Set<String> toAdd;

    /**
     * Creates an {@code AddTagCommand} to add the specified set of {@code Tags}.
     *
     * @param tagSet The set of tags to be added to the unique tag map.
     */
    public AddTagCommand(Set<String> tagSet) {
        requireAllNonNull(tagSet);
        this.toAdd = tagSet;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasNewTags(toAdd)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_TAGS, MESSAGE_USAGE));
        }

        model.addTags(toAdd);
        Set<Tag> tagList = new HashSet<>();
        for (String tag : toAdd) {
            tagList.add(new Tag(tag, new ArrayList<>(), new ArrayList<>()));
        }

        model.resetAllLists();
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(tagList)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddTagCommand)) {
            return false;
        }

        AddTagCommand otherAddTagCommand = (AddTagCommand) other;
        return toAdd.equals(otherAddTagCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
