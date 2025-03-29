package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_MISSING_KEYWORD;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.commands.FindPersonCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code FindPersonCommandParser} object.
 */
public class FindPersonCommandParser implements Parser<FindPersonCommand> {

    /**
     * Parses the given {@code String} of arguments and returns a FindPersonCommand object.
     *
     * @param args User input arguments.
     * @return FindPersonCommand instance with extracted keywords.
     * @throws ParseException if no keywords are provided.
     */
    @Override
    public FindPersonCommand parse(String args) throws ParseException {
        checkCommandFormat(args);
        List<String> keywords = Arrays.stream(args.trim().split("\\s+"))
                .collect(Collectors.toList());
        try {
            return new FindPersonCommand(keywords);
        } catch (CommandException e) {
            throw new ParseException(e.getMessage());
        }
    }

    private static void checkCommandFormat(String args) throws ParseException {
        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_MISSING_KEYWORD,
                    FindPersonCommand.MESSAGE_USAGE));
        }
    }
}
