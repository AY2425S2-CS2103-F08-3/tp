package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HOUSE_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOWER_BOUND_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTAL_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROPERTY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UNIT_NUMBER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UPPER_BOUND_PRICE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddListingCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.listing.HouseNumber;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.PostalCode;
import seedu.address.model.listing.PropertyName;
import seedu.address.model.listing.UnitNumber;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

public class AddListingCommandParser implements Parser<AddListingCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddPersonCommand
     * and returns an AddPersonCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddListingCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_POSTAL_CODE, PREFIX_UNIT_NUMBER, PREFIX_HOUSE_NUMBER,
                        PREFIX_LOWER_BOUND_PRICE, PREFIX_UPPER_BOUND_PRICE, PREFIX_PROPERTY_NAME, PREFIX_TAG,
                        PREFIX_NEW_TAG);

        boolean hasUnitNumber = argMultimap.getValue(PREFIX_UNIT_NUMBER).isPresent();
        boolean hasHouseNumber = argMultimap.getValue(PREFIX_HOUSE_NUMBER).isPresent();
        boolean hasExclusiveHouseOrUnitNumber = hasUnitNumber ^ hasHouseNumber;
        boolean hasPostalCode = argMultimap.getValue(PREFIX_POSTAL_CODE).isPresent();


        if (!hasPostalCode || !hasExclusiveHouseOrUnitNumber
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddListingCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_POSTAL_CODE, PREFIX_UNIT_NUMBER, PREFIX_HOUSE_NUMBER,
                PREFIX_LOWER_BOUND_PRICE, PREFIX_UPPER_BOUND_PRICE, PREFIX_PROPERTY_NAME);
        PostalCode postalCode = ParserUtil.parsePostalCode(argMultimap.getValue(PREFIX_POSTAL_CODE).get());
        UnitNumber unitNumber = ParserUtil.parseUnitNumber(argMultimap.getValue(PREFIX_UNIT_NUMBER).orElse(null));
        HouseNumber houseNumber = ParserUtil.parseHouseNumber(argMultimap.getValue(PREFIX_HOUSE_NUMBER).orElse(null));
        Price lowerBoundPrice = ParserUtil.parsePrice(argMultimap.getValue(PREFIX_LOWER_BOUND_PRICE).orElse(null));
        Price upperBoundPrice = ParserUtil.parsePrice(argMultimap.getValue(PREFIX_UPPER_BOUND_PRICE).orElse(null));
        PriceRange priceRange = createPriceRange(lowerBoundPrice, upperBoundPrice);
        PropertyName propertyName = ParserUtil.parsePropertyName(argMultimap.getValue(PREFIX_PROPERTY_NAME).orElse(null));
        Listing listing = createListing(postalCode, unitNumber, houseNumber, priceRange, propertyName);

        Set<String> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        Set<String> newTagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_NEW_TAG));

        return new AddListingCommand(listing, tagList, newTagList);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
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

    private static Listing createListing(PostalCode postalCode, UnitNumber unitNumber, HouseNumber houseNumber,
                                         PriceRange priceRange, PropertyName propertyName) {
        if (unitNumber == null && propertyName == null) {
            return new Listing(postalCode, houseNumber, priceRange, new HashSet<>(), new ArrayList<>());
        } else if (houseNumber == null && propertyName == null) {
            return new Listing(postalCode, unitNumber, priceRange, new HashSet<>(), new ArrayList<>());
        }

        if (unitNumber == null) {
            return new Listing(postalCode, houseNumber, priceRange, propertyName, new HashSet<>(), new ArrayList<>());
        }
        return new Listing(postalCode, unitNumber, priceRange, propertyName, new HashSet<>(), new ArrayList<>());
    }

}