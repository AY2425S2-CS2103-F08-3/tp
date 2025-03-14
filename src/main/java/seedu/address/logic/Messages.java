package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_INVALID_KEYWORD =
                "ERROR: Invalid keyword '%s'. Keywords can only contain letters, spaces, hyphens, or apostrophes.";
    public static final String MESSAGE_MISSING_KEYWORD =
                "ERROR: Missing parameters. You must provide at least one keyword.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%d persons found matching the keywords.";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail());
        return builder.toString();
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Listing listing) {
        final StringBuilder builder = new StringBuilder();
        if (listing.getUnitNumber() == null) {
            builder.append("; Postal Code: ")
                    .append(listing.getPostalCode())
                    .append("; House Number: ")
                    .append(listing.getHouseNumber())
                    .append("; Price Range: ")
                    .append(listing.getPriceRange())
                    .append("; Property Name: ")
                    .append(listing.getPropertyName());
        } else {
            builder.append("; Postal Code: ")
                    .append(listing.getPostalCode())
                    .append("; Unit Number: ")
                    .append(listing.getUnitNumber())
                    .append("; Price Range: ")
                    .append(listing.getPriceRange())
                    .append("; Property Name: ")
                    .append(listing.getPropertyName());
        }
        return builder.toString();
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Set<Tag> tags) {
        final StringBuilder builder = new StringBuilder();
        builder.append("; Tags: ");
        tags.forEach(builder::append);
        return builder.toString();
    }

}
