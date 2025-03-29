package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_ADD_TAG_PREAMBLE_FOUND;
import static seedu.address.logic.Messages.MESSAGE_NEW_TAG_PREFIX_EMPTY_VALUE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;

import java.util.Set;

import seedu.address.logic.commands.AddTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code AddTagCommandParser} object.
 */
public class AddTagCommandParser implements Parser<AddTagCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddTagCommand
     * and returns an AddTagCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NEW_TAG);
        checkCommandFormat(argMultimap);

        Set<String> newTagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_NEW_TAG));

        return new AddTagCommand(newTagList);
    }

    private static void checkCommandFormat(ArgumentMultimap argMultimap) throws ParseException {
        boolean hasNewTags = !(argMultimap.getAllValues(PREFIX_NEW_TAG).isEmpty());
        boolean hasPreamble = !argMultimap.getPreamble().isEmpty();

        if (!hasNewTags) {
            throw new ParseException(String.format(MESSAGE_NEW_TAG_PREFIX_EMPTY_VALUE,
                    AddTagCommand.MESSAGE_USAGE));
        }

        if (hasPreamble) {
            throw new ParseException(String.format(MESSAGE_ADD_TAG_PREAMBLE_FOUND, AddTagCommand.MESSAGE_USAGE));
        }
    }
}
