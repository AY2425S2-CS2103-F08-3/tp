package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Represents a command to find and list {@code Person} in the address book whose names match the given keyword(s).
 * Keyword matching is case insensitive and must adhere to a valid name format.
 */
public class FindPersonCommand extends Command {

    public static final String COMMAND_WORD = "findPerson";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds all persons whose names match the given keyword(s). "
            + "Keywords must contain only letters, spaces, hyphens, full stops, or apostrophes.\n"
            + "Every keyword can only start with a letter.\n"
            + "Parameters: " + COMMAND_WORD + " KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " Alex Yeoh";

    private static final String VALID_NAME_REGEX = "^[A-Za-z][A-Za-z' .-]{0,49}$";
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile(VALID_NAME_REGEX);

    private final List<String> keywords;
    private final Predicate<Person> predicate;

    /**
     * Constructs a @{code FindPersonCommand} to find {@code Person} with the given keywords.
     *
     * @param keywords List of keywords to match against names.
     * @throws CommandException if the keywords are empty or invalid.
     */
    public FindPersonCommand(List<String> keywords) throws CommandException {
        requireNonNull(keywords);
        if (keywords.isEmpty()) {
            throw new CommandException(String.format(Messages.MESSAGE_MISSING_KEYWORD, MESSAGE_USAGE));
        }

        // Validate each keyword before proceeding
        for (String keyword : keywords) {
            if (!VALID_NAME_PATTERN.matcher(keyword).matches()) {
                throw new CommandException(String.format(Messages.MESSAGE_INVALID_KEYWORD, keyword, MESSAGE_USAGE));
            }
        }

        this.keywords = keywords;
        this.predicate = person -> keywords.stream()
                .anyMatch(keyword -> person.getName().fullName.toLowerCase().contains(keyword.toLowerCase()));
    }

    /**
     * Executes the find command and updates the filtered person list in the model.
     *
     * @param model {@code Model} which the command should operate on.
     * @return CommandResult with the number of matches found.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        int count = model.getFilteredPersonList().size();

        if (count == 0) {
            return new CommandResult("No persons found matching the keywords.");
        }
        return new CommandResult(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, count));
    }

    /**
     * Checks if another object is equal to this FindCommand.
     *
     * @param other Object to compare with.
     * @return true if the other object is a FindCommand with the same keywords, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FindPersonCommand)) {
            return false;
        }

        FindPersonCommand otherFindPersonCommand = (FindPersonCommand) other;
        return keywords.equals(otherFindPersonCommand.keywords);
    }

    /**
     * Returns a string representation of the FindCommand.
     *
     * @return String representation of the command with keywords.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("keywords", keywords)
                .toString();
    }
}
