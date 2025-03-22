package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.SearchPropertyByTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SearchPropertyByTagCommand object.
 */
public class SearchPropertyByTagCommandParser implements Parser<SearchPropertyByTagCommand> {

    @Override
    public SearchPropertyByTagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        Set<String> tags = argMultimap.getAllValues(PREFIX_TAG).stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        if (tags.isEmpty()) {
            throw new ParseException(Messages.MESSAGE_SEARCH_PROPERTY_TAG_MISSING_PARAMS);
        }
        if (argMultimap.getAllValues(PREFIX_TAG).stream().anyMatch(String::isBlank)) {
            throw new ParseException(Messages.MESSAGE_SEARCH_PROPERTY_TAG_PREFIX_EMPTY);
        }

        return new SearchPropertyByTagCommand(tags);
    }
}
