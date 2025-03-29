package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_EXPECTED_TWO_INDICES;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        checkCommandFormat(argMultimap);

        List<Index> multipleIndices = ParserUtil.parseMultipleIndices(args);
        return new MatchPersonCommand(multipleIndices.get(0), multipleIndices.get(1));
    }

    private static void checkCommandFormat(ArgumentMultimap argMultimap) throws ParseException {
        String preamble = argMultimap.getPreamble().trim();

        if (preamble.isEmpty() || preamble.split("\\s+").length != 2) {
            throw new ParseException(String.format(MESSAGE_EXPECTED_TWO_INDICES,
                    MatchPersonCommand.MESSAGE_USAGE));
        }
    }

}
