package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_ARGUMENTS_EMPTY;
import static seedu.address.logic.Messages.MESSAGE_EXPECTED_TWO_INDICES;
import static seedu.address.logic.Messages.MESSAGE_TAG_OR_NEW_TAG_REQUIRED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.OverwritePreferenceTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code OverwritePreferenceTagCommand} object.
 */
public class OverwritePreferenceTagCommandParser implements Parser<OverwritePreferenceTagCommand> {

    private static final String WHITESPACE_REGEX = "\\s+";
    private static final int EXPECTED_PREAMBLE_PARTS = 2;
    private static final int PERSON_INDEX = 0;
    private static final int PREFERENCE_INDEX = 1;

    /**
     * Parses the given {@code String} of arguments in the context of the OverwritePreferenceTagCommand
     * and returns an OverwritePreferenceTagCommand object for execution.
     *
     * @param args The arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public OverwritePreferenceTagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG, PREFIX_NEW_TAG);
        argMultimap.verifyNoDuplicateTagValues(OverwritePreferenceTagCommand.MESSAGE_USAGE);
        Index personIndex;
        Index preferenceIndex;

        checkCommandFormat(argMultimap, args);

        List<Index> multipleIndices = ParserUtil.parseMultipleIndices(argMultimap.getPreamble());

        personIndex = multipleIndices.get(PERSON_INDEX);
        preferenceIndex = multipleIndices.get(PREFERENCE_INDEX);

        Set<String> tagSet = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        Set<String> newTagSet = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_NEW_TAG));

        return new OverwritePreferenceTagCommand(personIndex, preferenceIndex, tagSet, newTagSet);
    }

    private static void checkCommandFormat(ArgumentMultimap argMultimap, String args) throws ParseException {
        String preamble = argMultimap.getPreamble().trim();
        boolean hasTags = !argMultimap.getAllValues(PREFIX_TAG).isEmpty();
        boolean hasNewTags = !argMultimap.getAllValues(PREFIX_NEW_TAG).isEmpty();
        boolean hasCombinedTags = hasTags || hasNewTags;

        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_ARGUMENTS_EMPTY,
                    OverwritePreferenceTagCommand.MESSAGE_USAGE));
        }

        if (!hasCombinedTags) {
            throw new ParseException(String.format(MESSAGE_TAG_OR_NEW_TAG_REQUIRED,
                    OverwritePreferenceTagCommand.MESSAGE_USAGE));
        }

        if (preamble.isEmpty() || preamble.split(WHITESPACE_REGEX).length != EXPECTED_PREAMBLE_PARTS) {
            throw new ParseException(String.format(MESSAGE_EXPECTED_TWO_INDICES,
                    OverwritePreferenceTagCommand.MESSAGE_USAGE));
        }
    }
}
