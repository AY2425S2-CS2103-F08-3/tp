package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_ARGUMENTS_EMPTY;
import static seedu.address.logic.Messages.MESSAGE_ONE_INDEX_EXPECTED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOWER_BOUND_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UPPER_BOUND_PRICE;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddPreferenceCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;

/**
 * Parses input arguments and creates a new {@code AddPreferenceCommandParser} object.
 */
public class AddPreferenceCommandParser implements Parser<AddPreferenceCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddPreferenceCommand
     * and returns an AddPreferenceCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddPreferenceCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_LOWER_BOUND_PRICE, PREFIX_UPPER_BOUND_PRICE, PREFIX_TAG,
                        PREFIX_NEW_TAG);
        checkCommandFormat(argMultimap, args);
        Index index;

        index = ParserUtil.parseIndex(argMultimap.getPreamble());

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_LOWER_BOUND_PRICE, PREFIX_UPPER_BOUND_PRICE);
        Price lowerBoundPrice = ParserUtil.parsePrice(argMultimap.getValue(PREFIX_LOWER_BOUND_PRICE).orElse(null));
        Price upperBoundPrice = ParserUtil.parsePrice(argMultimap.getValue(PREFIX_UPPER_BOUND_PRICE).orElse(null));
        PriceRange priceRange = createPriceRange(lowerBoundPrice, upperBoundPrice);
        Set<String> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        Set<String> newTagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_NEW_TAG));

        return new AddPreferenceCommand(index, priceRange, tagList, newTagList);
    }

    private static PriceRange createPriceRange(Price lowerBoundPrice, Price upperBoundPrice) {
        if (lowerBoundPrice == null && upperBoundPrice == null) {
            return new PriceRange();
        } else if (lowerBoundPrice == null) {
            return new PriceRange(upperBoundPrice, true);
        } else if (upperBoundPrice == null) {
            return new PriceRange(lowerBoundPrice, false);
        } else {
            return new PriceRange(lowerBoundPrice, upperBoundPrice);
        }
    }

    private static void checkCommandFormat(ArgumentMultimap argMultimap, String args) throws ParseException {
        String preamble = argMultimap.getPreamble().trim();
        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_ARGUMENTS_EMPTY,
                    AddPreferenceCommand.MESSAGE_USAGE));
        }

        if (preamble.isEmpty() || preamble.split("\\s+").length != 1) {
            throw new ParseException(String.format(MESSAGE_ONE_INDEX_EXPECTED,
                    AddPreferenceCommand.MESSAGE_USAGE));
        }
    }

}
