package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CommandUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Adds {@code Tag} to a {@code PropertyPreference} of a {@code Person} in the address book.
 * The {@code PropertyPreference} is identified using it's displayed index within the {@code Person}'s preferences,
 * and the {@code Person} is identified using it's displayed index.
 */
public class AddPreferenceTagCommand extends Command {

    public static final String COMMAND_WORD = "addPreferenceTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds tags to an existing preference."
            + "\nParameters: "
            + "PERSON_INDEX (must be a positive integer) "
            + "PREFERENCE_INDEX (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG]{1}... "
            + "[" + PREFIX_NEW_TAG + "NEW_TAG]{1}..."
            + "\nExample: "
            + COMMAND_WORD + " 2 1 "
            + PREFIX_TAG + "quiet "
            + PREFIX_TAG + "pet-friendly "
            + PREFIX_NEW_TAG + "family-friendly "
            + PREFIX_NEW_TAG + "spacious";

    public static final String MESSAGE_SUCCESS = "Adds tags to preferences: %1$s";
    public static final String MESSAGE_INVALID_TAGS = "At least one of the tags given does not exist.\n%1$s";
    public static final String MESSAGE_DUPLICATE_TAGS = "At least one of the new tags given already exist.\n%1$s";
    public static final String MESSAGE_DUPLICATE_TAGS_IN_PREFERENCE = "At least one of the "
            + "tags given already exist in the preference.\n%1$s";

    private final Index targetPersonIndex;
    private final Index targetPreferenceIndex;
    private final Set<String> tagSet;
    private final Set<String> newTagSet;

    /**
     * Creates an @{code AddPreferenceTagCommand} to add the specified {@code Tag}(s) to the specified
     * {@code PropertyPreference}.
     *
     * @param targetPersonIndex The index of the person in the filtered person list that the preference is located in.
     * @param targetPreferenceIndex The index of the preference to add tags to.
     * @param tagSet The set of existing tags to be added to the preference.
     * @param newTagSet The set of new tags to be added to the preference and to the unique tag map.
     */
    public AddPreferenceTagCommand(Index targetPersonIndex, Index targetPreferenceIndex, Set<String> tagSet,
                                   Set<String> newTagSet) {
        requireAllNonNull(targetPersonIndex, targetPreferenceIndex, tagSet, newTagSet);

        this.targetPersonIndex = targetPersonIndex;
        this.targetPreferenceIndex = targetPreferenceIndex;
        this.tagSet = tagSet;
        this.newTagSet = newTagSet;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Get and validate person
        Person targetPerson = CommandUtil.getValidatedPerson(model, targetPersonIndex, MESSAGE_USAGE);

        // Get and validate preference
        PropertyPreference targetPreference = CommandUtil.getValidatedPreference(model, targetPerson,
                targetPreferenceIndex, MESSAGE_USAGE, true);

        // Validate and process tags
        CommandUtil.validateTags(model, tagSet, newTagSet, MESSAGE_USAGE,
                MESSAGE_INVALID_TAGS, MESSAGE_DUPLICATE_TAGS);
        Set<Tag> tags = processAndGetTags(model, targetPreference);

        // Apply changes
        applyTagsToPreference(model, targetPerson, targetPreference, tags);

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                Messages.format(targetPerson, targetPreference), Messages.format(tags)));
    }

    /**
     * Processes tags and checks for duplicates in the preference.
     * Returns the set of tags to be added.
     */
    private Set<Tag> processAndGetTags(Model model, PropertyPreference preference) throws CommandException {
        model.addTags(newTagSet);
        Set<String> tagNames = new HashSet<>(tagSet);
        Set<Tag> tags = new HashSet<>();
        tagNames.addAll(newTagSet);

        for (String tagName : tagNames) {
            Tag tag = model.getTag(tagName);
            if (preference.getTags().contains(tag)) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_TAGS_IN_PREFERENCE, MESSAGE_USAGE));
            }
            tags.add(tag);
        }
        return tags;
    }

    /**
     * Applies the validated tags to the preference and updates the model.
     */
    private void applyTagsToPreference(Model model, Person targetPerson,
        PropertyPreference preference, Set<Tag> tags) {
        for (Tag tag : tags) {
            tag.addPropertyPreference(preference);
            model.setTag(tag, tag);
            preference.addTag(tag);
        }
        model.setPerson(targetPerson, targetPerson);
        model.resetAllLists();
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
                .add("targetPersonIndex", targetPersonIndex)
                .add("targetPreferenceIndex", targetPreferenceIndex)
                .add("tags", tagSet)
                .add("newTags", newTagSet)
                .toString();
    }
}
