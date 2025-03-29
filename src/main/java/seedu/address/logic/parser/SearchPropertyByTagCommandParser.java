package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.SearchPropertyByTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code SearchPropertyByTagCommand} object.
 */
public class SearchPropertyByTagCommandParser implements Parser<SearchPropertyByTagCommand> {

    @Override
    public SearchPropertyByTagCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        checkCommandFormat(argMultimap);

        Set<String> tags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        return new SearchPropertyByTagCommand(tags);
    }

    private static void checkCommandFormat(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getPreamble().isEmpty() && argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_ARGUMENTS_EMPTY,
                    SearchPropertyByTagCommand.MESSAGE_USAGE));
        }

        if (ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG)).isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_SEARCH_PROPERTY_TAG_MISSING_PARAMS,
                    SearchPropertyByTagCommand.MESSAGE_USAGE));
        }

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_SEARCH_PROPERTY_TAG_PREAMBLE_FOUND,
                    SearchPropertyByTagCommand.MESSAGE_USAGE));
        }
    }
}
