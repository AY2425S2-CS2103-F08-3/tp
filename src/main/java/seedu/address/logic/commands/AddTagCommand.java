package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
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
 * Adds a tag to the address book.
 */
public class AddTagCommand extends Command {
    public static final String COMMAND_WORD = "addTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a new tag to the system."
            + "\nParameters: "
            + PREFIX_NEW_TAG + "family-friendly "
            + PREFIX_NEW_TAG + "spacious";

    public static final String MESSAGE_SUCCESS = "Tag added: %1$s";
    public static final String MESSAGE_DUPLICATE_TAGS = "At Least one of the tags already exists in the address book.";

    private final Set<String> toAdd;

    /**
     * Creates an AddPersonCommand to add the specified {@code Person}
     */
    public AddTagCommand(Set<String> tags) {
        requireNonNull(tags);
        toAdd = tags;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasTags(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TAGS);
        }

        model.addTags(toAdd);
        Set<Tag> tagList = getTags(toAdd);
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

    private Set<Tag> getTags(Set<String> tags) {
        Set<Tag> tagList = new HashSet<>();
        for (String tag : tags) {
            tagList.add(new Tag(tag, new ArrayList<>()));
        }
        return tagList;
    }
}
