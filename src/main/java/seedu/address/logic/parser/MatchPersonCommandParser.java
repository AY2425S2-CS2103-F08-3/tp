package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_EXPECTED_TWO_INDICES;
import static seedu.address.logic.Messages.MESSAGE_INVALID_OWNER_OR_LISTING_DISPLAYED_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MatchPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code MatchPersonCommandParser} object.
 */
public class MatchPersonCommandParser implements Parser<MatchPersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MatchPersonCommandParser
     * and returns a MatchPersonCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MatchPersonCommand parse(String args) throws ParseException {
        List<Index> multipleIndices;
        try {
            multipleIndices = ParserUtil.parseMultipleIndices(args);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_OWNER_OR_LISTING_DISPLAYED_INDEX,
                    MatchPersonCommand.MESSAGE_USAGE),
                    pe);
        }

        try {
            if (multipleIndices.size() != 2) {
                throw new ParseException("Expected 2 indices");
            }
            return new MatchPersonCommand(multipleIndices.get(0), multipleIndices.get(1));
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_EXPECTED_TWO_INDICES, MatchPersonCommand.MESSAGE_USAGE), pe);
        }
    }

}
