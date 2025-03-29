package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOWER_BOUND_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UPPER_BOUND_PRICE;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;

/**
 * Adds a {@code PropertyPreference} to a {@code Person} identified using it's displayed index in
 * the address book.
 */
public class AddPreferenceCommand extends Command {
    public static final String COMMAND_WORD = "addPreference";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a new property preference with "
            + "specified tags to a person."
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_LOWER_BOUND_PRICE + "LOWER BOUND PRICE "
            + PREFIX_UPPER_BOUND_PRICE + "UPPER BOUND PRICE "
            + "[" + PREFIX_TAG + "TAG]... "
            + "[" + PREFIX_NEW_TAG + "NEW_TAG]...\n"
            + "Example: " + COMMAND_WORD + " 2 "
            + PREFIX_LOWER_BOUND_PRICE + "300000 "
            + PREFIX_UPPER_BOUND_PRICE + "600000 "
            + PREFIX_TAG + "quiet "
            + PREFIX_TAG + "pet-friendly "
            + PREFIX_NEW_TAG + "family-friendly "
            + PREFIX_NEW_TAG + "spacious";

    public static final String MESSAGE_SUCCESS = "New property preference added to %1$s";
    public static final String MESSAGE_DUPLICATE_TAGS = "At least one of the new tags given already exist.%1$s\n";
    public static final String MESSAGE_INVALID_TAGS = "At least one of the tags given does not exist.%1$s\n";

    private final Index index;
    private final PriceRange priceRange;
    private final Set<String> tagSet;
    private final Set<String> newTagSet;

    /**
     * Creates an {@code AddPreferenceCommand} to add the specified {@code Preference} to {@code Person}.
     *
     * @param index Index of the person in the filtered person list to add preference to
     * @param priceRange Price range of the preference
     * @param tags  The set of existing tags to be added to the preference
     * @param newTags The set of tags to be added to the preference and to the tag registry
     */
    public AddPreferenceCommand(Index index, PriceRange priceRange, Set<String> tags,
                                Set<String> newTags) {
        requireNonNull(priceRange);
        this.index = index;
        this.priceRange = priceRange;
        tagSet = tags;
        newTagSet = newTags;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getSortedFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, MESSAGE_USAGE));
        }

        if (!model.hasTags(tagSet)) {
            throw new CommandException(String.format(MESSAGE_INVALID_TAGS, MESSAGE_USAGE));
        }

        if (model.hasNewTags(newTagSet)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_TAGS, MESSAGE_USAGE));
        }

        Person personToAddPreference = lastShownList.get(index.getZeroBased());
        PropertyPreference preference = new PropertyPreference(priceRange, new HashSet<>(), personToAddPreference);

        model.addTags(newTagSet);

        Set<String> tagNames = new HashSet<>(tagSet);
        tagNames.addAll(newTagSet);

        TagRegistry tagRegistry = TagRegistry.of();
        for (String tagName: tagNames) {
            Tag tag = tagRegistry.get(tagName);
            tag.addPropertyPreference(preference);
            tagRegistry.setTag(tag, tag);
            preference.addTag(tagRegistry.get(tagName));
        }

        personToAddPreference.addPropertyPreference(preference);

        model.setPerson(personToAddPreference, personToAddPreference);
        model.resetAllLists();

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                Messages.format(personToAddPreference, preference)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddPreferenceCommand)) {
            return false;
        }

        AddPreferenceCommand otherAddPreferenceCommand = (AddPreferenceCommand) other;
        return priceRange.equals(otherAddPreferenceCommand.priceRange);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("priceRange", priceRange)
                .toString();
    }

}
