package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

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
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;

/**
 * Adds {@code Tag} to a {@code PropertyPreference} identified using it's displayed index
 * from {@code Person} identified using it's displayed index in the address book.
 */
public class AddPreferenceTagCommand extends Command {

    public static final String COMMAND_WORD = "addPreferenceTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds tags to a preference identified "
            + "by the index number used in the displayed preference list of\n"
            + " a specific person, identified by index number used in the displayed person list.\n"
            + "Parameters: PERSON_INDEX (must be a positive integer) PREFERENCE_INDEX (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG]... "
            + "[" + PREFIX_NEW_TAG + "NEW_TAG]...\n"
            + "Example: " + COMMAND_WORD + " 2 "
            + PREFIX_TAG + "quiet "
            + PREFIX_TAG + "pet-friendly "
            + PREFIX_NEW_TAG + "family-friendly "
            + PREFIX_NEW_TAG + "spacious";

    public static final String MESSAGE_SUCCESS = "Adds tags to preferences: %1$s";
    public static final String MESSAGE_INVALID_TAGS = "At least one of the tags given does not exist.\n%1$s";
    public static final String MESSAGE_DUPLICATE_TAGS = "At least one of the new tags given already exist.\n%1$s";
    public static final String MESSAGE_DUPLICATE_TAGS_IN_LISTING = "At least one of the "
            + "tags given already exist in the preference.\n%1$s";

    private final Index targetPersonIndex;
    private final Index targetPreferenceIndex;
    private final Set<String> tagSet;
    private final Set<String> newTagSet;

    /**
     * Creates an @{code AddPreferenceTagCommand} to add specified {@code Tag} to {@code Preference}.
     *
     * @param personIndex The index of the person from which the preference is located in.
     * @param preferenceIndex The index of the preference from which tags will be removed.
     * @param tagSet The set of existing tags to be added to the preference.
     * @param newTagSet The set of new tags to be added to the preference.
     */
    public AddPreferenceTagCommand(Index personIndex, Index preferenceIndex, Set<String> tagSet,
                                   Set<String> newTagSet) {
        requireAllNonNull(personIndex, preferenceIndex, tagSet, newTagSet);

        this.targetPersonIndex = personIndex;
        this.targetPreferenceIndex = preferenceIndex;
        this.tagSet = tagSet;
        this.newTagSet = newTagSet;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedFilteredPersonList();

        if (targetPersonIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, MESSAGE_USAGE));
        }

        Person targetPerson = lastShownList.get(targetPersonIndex.getZeroBased());

        // Filter preferences according to active filter tags
        List<PropertyPreference> targetPreferenceList = targetPerson.getPropertyPreferences().stream()
                .filter(PropertyPreference::isFiltered)
                .toList();

        if (targetPreferenceIndex.getZeroBased() >= targetPreferenceList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PREFERENCE_DISPLAYED_INDEX,
                    MESSAGE_USAGE));
        }

        PropertyPreference preference = targetPreferenceList.get(targetPreferenceIndex.getZeroBased());

        if (!model.hasTags(tagSet)) {
            throw new CommandException(String.format(MESSAGE_INVALID_TAGS, MESSAGE_USAGE));
        }

        if (model.hasNewTags(newTagSet)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_TAGS, MESSAGE_USAGE));
        }

        model.addTags(newTagSet);

        TagRegistry tagRegistry = TagRegistry.of();
        Set<String> tagNames = new HashSet<>(tagSet);
        Set<Tag> tags = new HashSet<>();
        tagNames.addAll(newTagSet);

        for (String tagName : tagNames) {
            Tag tag = tagRegistry.get(tagName);
            if (preference.getTags().contains(tag)) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_TAGS_IN_LISTING, MESSAGE_USAGE));
            }
            tags.add(tag);
        }

        for (Tag tag : tags) {
            tag.addPropertyPreference(preference);
            tagRegistry.setTag(tag, tag);
            preference.addTag(tag);
        }

        model.setPerson(targetPerson, targetPerson);
        model.resetAllLists();

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                Messages.format(targetPerson, preference), Messages.format(tags)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddPreferenceTagCommand
                && targetPersonIndex.equals(((AddPreferenceTagCommand) other).targetPersonIndex)
                && targetPreferenceIndex.equals(((AddPreferenceTagCommand) other).targetPreferenceIndex)
                && tagSet.equals(((AddPreferenceTagCommand) other).tagSet))
                && newTagSet.equals(((AddPreferenceTagCommand) other).newTagSet);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", targetPersonIndex)
                .add("preferenceIndex", targetPreferenceIndex)
                .add("tags", tagSet)
                .add("newTags", newTagSet)
                .toString();
    }
}
