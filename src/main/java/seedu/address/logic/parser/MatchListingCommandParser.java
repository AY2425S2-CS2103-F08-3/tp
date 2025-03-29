package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MatchListingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code MatchListingCommandParser} object.
 */
public class MatchListingCommandParser implements Parser<MatchListingCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MatchListingCommand
     * and returns a MatchListingCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MatchListingCommand parse(String args) throws ParseException {
        Index index = ParserUtil.parseIndex(args);
        return new MatchListingCommand(index);
    }

}
