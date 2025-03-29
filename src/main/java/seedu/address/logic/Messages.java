package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_ADD_LISTING_PREAMBLE_FOUND = "Add listing should not have preamble. \n%1$s";
    public static final String MESSAGE_ADD_PERSON_PREAMBLE_FOUND = "Add person should not have preamble. \n%1$s";
    public static final String MESSAGE_DELETE_TAG_PREAMBLE_FOUND = "Delete tag should not have preamble. \n%1$s";
    public static final String MESSAGE_SEARCH_PERSON_TAG_PREAMBLE_FOUND = "Search person should not have preamble. "
            + "\n%1$s";
    public static final String MESSAGE_SEARCH_PROPERTY_TAG_PREAMBLE_FOUND = "Search property should not have preamble. "
            + "\n%1$s";
    public static final String MESSAGE_HOUSE_OR_UNIT_NUMBER_REQUIRED =
            "Either house number or unit number must be provided, but not both.\n%1$s";
    public static final String MESSAGE_EXPECTED_TWO_INDICES = "This command expects 2 indices to be provided. \n%1$s";
    public static final String MESSAGE_NAME_REQUIRED = "Name must be provided.\n%1$s";
    public static final String MESSAGE_PHONE_REQUIRED = "Phone must be provided.\n%1$s";
    public static final String MESSAGE_EMAIL_REQUIRED = "Email must be provided.\n%1$s";
    public static final String MESSAGE_TAG_OR_NEW_TAG_REQUIRED = "Provide at least a new tag or existing tag. \n%1$s";
    public static final String MESSAGE_PREFERENCE_TAG_REQUIRED_FOR_DELETE = "At least one preference Tag must be "
            + "provided for deletion.\n%1$s";
    public static final String MESSAGE_PROPERTY_TAG_REQUIRED_FOR_DELETE = "At least one property Tag must be "
            + "provided for deletion.\n%1$s";
    public static final String MESSAGE_TAG_REQUIRED_FOR_DELETE = "At least one Tag must be "
            + "provided for deletion.\n%1$s";
    public static final String MESSAGE_POSTAL_CODE_REQUIRED = "Postal code must be provided. \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid. \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_OR_PREFERENCE_DISPLAYED_INDEX = "The person or preference index "
            + "provided is invalid. \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_OR_LISTING_DISPLAYED_INDEX = "The person or listing index "
            + "provided is invalid. \n%1$s";
    public static final String MESSAGE_INVALID_OWNER_OR_LISTING_DISPLAYED_INDEX = "The owner or listing index "
            + "provided is invalid. \n%1$s";
    public static final String MESSAGE_INVALID_LISTING_DISPLAYED_INDEX = "The listing index provided is invalid. "
            + "\n%1$s";
    public static final String MESSAGE_INVALID_OWNER_DISPLAYED_INDEX = "The owner index provided is invalid. \n%1$s";
    public static final String MESSAGE_DELETE_PROPERTY_TAG_SUCCESS = "Tag(s) in property %s deleted: %s";
    public static final String MESSAGE_TAG_NOT_FOUND_IN_PROPERTY = "Tag(s) not found in property: %s\n%s";
    public static final String MESSAGE_TAG_NOT_FOUND_IN_PREFERENCE = "Tag(s) not found in property preference: %s\n%s";
    public static final String MESSAGE_INVALID_PREFERENCE_DISPLAYED_INDEX = "The property preference index provided "
            + "is invalid.\n%1$s";
    public static final String MESSAGE_ADD_TAG_PREAMBLE_FOUND = "Add tag should not have preamble. \n%1$s";
    public static final String MESSAGE_TAG_OR_NEW_TAG_PREFIX_EMPTY_VALUE = "At least one given tag or new tag prefix "
            + "is empty.\n%1$s";
    public static final String MESSAGE_NEW_TAG_PREFIX_EMPTY_VALUE = "At least one new tag prefix "
            + "is empty.\n%1$s";
    public static final String MESSAGE_INDEX_REQUIRED = "Please provide 1 index for this command";
    public static final String MESSAGE_ARGUMENTS_EMPTY = "Arguments should not be empty. \n%1$s";
    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_INVALID_KEYWORD =
            "ERROR: Invalid keyword '%s'. \n%s";
    public static final String MESSAGE_MISSING_KEYWORD =
            "ERROR: Missing parameters. You must provide at least one keyword.\n%s";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%d persons found matching the keywords.";
    public static final String MESSAGE_SEARCH_PERSON_TAG_NOT_FOUND = "Tag '%s' does not exist.";
    public static final String MESSAGE_SEARCH_PERSON_TAG_MISSING_PARAMS =
            "At least one tag [t/TAG] must be specified for search.\n%s";
    public static final String MESSAGE_SEARCH_PERSON_TAGS_SUCCESS = "%d persons matching the tags.";
    public static final String MESSAGE_SEARCH_PERSON_TAGS_NO_MATCH = "No persons matching the tags.";
    public static final String MESSAGE_SEARCH_PERSON_TAG_PREFIX_EMPTY =
            "Tag prefix specified but no tag value given. Please specify a tag after t/.\n%S";
    public static final String MESSAGE_SEARCH_PROPERTY_TAGS_SUCCESS = "%d properties matching the tags!";
    public static final String MESSAGE_SEARCH_PROPERTY_TAGS_NO_MATCH = "No properties matching the tags.";
    public static final String MESSAGE_TAG_DOES_NOT_EXIST = "Tag '%s' does not exist in the system.\n%s";
    public static final String MESSAGE_SEARCH_PROPERTY_TAG_MISSING_PARAMS =
            "At least one [t/TAG] needs to be specified for search.\n%s";
    public static final String MESSAGE_SEARCH_PROPERTY_TAG_PREFIX_EMPTY =
            "Tag prefix specified but no tag value given. Please specify a tag after t/.\n%s";

    public static final String MESSAGE_ONE_INDEX_EXPECTED = "This command expects only 1 index to be provided. \n%s";

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
     * Formats basic property address details (postal code + unit/house number) for display.
     */
    public static String formatPropertyDetails(Listing property) {
        StringBuilder details = new StringBuilder();
        details.append(" ").append(property.getPostalCode().toString());

        if (property.getUnitNumber() != null) {
            details.append(" ").append(property.getUnitNumber().toString());
        } else if (property.getHouseNumber() != null) {
            details.append(" ").append(property.getHouseNumber().toString());
        }
        return details.toString();
    }

    /**
     * Formats the {@code tags} without any prefix, only displaying the tag names in brackets.
     */
    public static String formatTagsOnly(Set<Tag> tags) {
        return "["
                + tags.stream()
                      .map(Tag::getTagName)
                      .collect(Collectors.joining(", "))
                + "]";
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
        builder.append("; Postal Code: ").append(listing.getPostalCode());

        if (listing.getUnitNumber() == null) {
            builder.append("; House Number: ").append(listing.getHouseNumber());
        } else {
            builder.append("; Unit Number: ").append(listing.getUnitNumber());
        }

        builder.append("; Price Range: ").append(listing.getPriceRange());

        if (listing.getPropertyName() != null) {
            builder.append("; Property Name: ").append(listing.getPropertyName());
        }

        builder.append("; Tags: [")
                .append(listing.getTags().stream()
                        .map(Tag::getTagName)
                        .collect(Collectors.joining(", ")))
                .append("]");

        return builder.toString();
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person, PropertyPreference preference) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName() + ":")
                .append(" Price Range: ")
                .append(preference.getPriceRange())
                .append("; Tags: [")
                .append(preference.getTags().stream()
                        .map(Tag::getTagName)
                        .collect(Collectors.joining(", ")))
                .append("]");
        return builder.toString();
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Set<Tag> tags) {
        final StringBuilder builder = new StringBuilder();
        builder.append("; Tags: [")
                .append(tags.stream()
                        .map(Tag::getTagName)
                        .collect(Collectors.joining(", ")))
                .append("]");
        return builder.toString();
    }

    /**
     * Formats the assign of listing to Person for display to the user.
     */
    public static String format(Person person, Listing listing) {
        final StringBuilder builder = new StringBuilder();
        if (listing.getUnitNumber() == null) {
            builder.append("<")
                    .append(listing.getPostalCode())
                    .append("> <")
                    .append(listing.getHouseNumber())
                    .append(">")
                    .append(" added to ")
                    .append(person.getName());
        } else {
            builder.append("<")
                    .append(listing.getPostalCode())
                    .append("> <")
                    .append(listing.getUnitNumber())
                    .append(">")
                    .append(" added to ")
                    .append(person.getName());
        }

        return builder.toString();
    }

    /**
     * Formats the message of adding tags to listing.
     */
    public static String format(Set<Tag> tags, Listing listingWithTags) {
        final StringBuilder builder = new StringBuilder();
        if (listingWithTags.getUnitNumber() == null) {
            builder.append("<")
                    .append(listingWithTags.getPostalCode())
                    .append("> <")
                    .append(listingWithTags.getHouseNumber())
                    .append(">")
                    .append("; Tags: [")
                    .append(tags.stream()
                            .map(Tag::getTagName)
                            .collect(Collectors.joining(", ")))
                    .append("]");
        } else {
            builder.append("<")
                    .append(listingWithTags.getPostalCode())
                    .append("> <")
                    .append(listingWithTags.getUnitNumber())
                    .append(">")
                    .append("; Tags: [")
                    .append(tags.stream()
                            .map(Tag::getTagName)
                            .collect(Collectors.joining(", ")))
                    .append("]");
        }

        return builder.toString();
    }

    /**
     * Formats the message of marking listing as available or unavailable
     */
    public static String format(boolean isAvailable, Listing listingWithTags) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<")
                .append(listingWithTags.getPostalCode())
                .append("> <");

        if (listingWithTags.getUnitNumber() == null) {
            builder.append(listingWithTags.getHouseNumber())
                    .append(">");
        } else {
            builder.append(listingWithTags.getUnitNumber())
                    .append(">");
        }

        if (isAvailable) {
            builder.append(" marked as available.");
        } else {
            builder.append(" marked as unavailable.");
        }

        return builder.toString();
    }
}

