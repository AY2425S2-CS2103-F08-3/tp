package seedu.address.logic.parser;


import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkUnavailableCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code MarkUnavailableCommandParser} object.
 */
public class MarkUnavailableCommandParser implements Parser<MarkUnavailableCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MarkUnavailableCommand
     * and returns a MarkUnavailableCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkUnavailableCommand parse(String args) throws ParseException {
        Index index = ParserUtil.parseIndex(args);
        return new MarkUnavailableCommand(index);
    }
}
