package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_ARGUMENTS_EMPTY;
import static seedu.address.logic.Messages.MESSAGE_EXPECTED_TWO_INDICES;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeletePreferenceCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code DeletePreferenceCommandParser} object.
 */
public class DeletePreferenceCommandParser implements Parser<DeletePreferenceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeletePreferenceCommand
     * and returns a DeletePreferenceCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeletePreferenceCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);
        checkCommandFormat(argMultimap, args);
        List<Index> multipleIndices = ParserUtil.parseMultipleIndices(argMultimap.getPreamble());
        return new DeletePreferenceCommand(multipleIndices.get(0), multipleIndices.get(1));

    }

    private static void checkCommandFormat(ArgumentMultimap argMultimap, String args) throws ParseException {
        String preamble = argMultimap.getPreamble().trim();

        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_ARGUMENTS_EMPTY,
                    DeletePreferenceCommand.MESSAGE_USAGE));
        }

        if (preamble.isEmpty() || preamble.split("\\s+").length != 2) {
            throw new ParseException(String.format(MESSAGE_EXPECTED_TWO_INDICES,
                    DeletePreferenceCommand.MESSAGE_USAGE));
        }
    }

}
