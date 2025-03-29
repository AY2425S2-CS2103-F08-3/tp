package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkAvailableCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code MarkAvailableCommandParser} object.
 */
public class MarkAvailableCommandParser implements Parser<MarkAvailableCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the MarkAvailableCommand
     * and returns a MarkAvailableCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkAvailableCommand parse(String args) throws ParseException {
        Index index = ParserUtil.parseIndex(args);
        return new MarkAvailableCommand(index);
    }
}
