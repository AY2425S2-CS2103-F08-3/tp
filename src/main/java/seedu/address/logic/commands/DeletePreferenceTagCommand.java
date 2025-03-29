package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
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
 * Deletes {@code Tag}(s) from a {@code PropertyPreference} identified using it's displayed index
 * from {@code Person} identified using it's displayed index in the address book.
 */
public class DeletePreferenceTagCommand extends Command {

    public static final String COMMAND_WORD = "deletePreferenceTag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes tags from a preference identified "
            + "by the index number used in the displayed preference list of\n"
            + " a specific person, identified by index number used in the displayed person list.\n"
            + "Parameters: PERSON_INDEX (must be a positive integer) PREFERENCE_INDEX (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 3 1 " + PREFIX_TAG + "pet-friendly " + PREFIX_TAG + "pool";

    public static final String MESSAGE_DELETE_PREFERENCE_TAG_SUCCESS = "Tag(s) in property preference %s deleted: %s";

    private final Index targetPersonIndex;
    private final Index targetPreferenceIndex;
    private final Set<String> tagsToDelete;

    /**
     * Creates a {@code DeletePreferenceTagCommand} to delete a set of {@code Tag}
     * from the specified {@code Preference}.
     *
     * @param personIndex The index of the person from which the preference is located in.
     * @param preferenceIndex The index of the preference from which tags will be removed.
     * @param tagsToDelete  The set of tag names to be deleted.
     */
    public DeletePreferenceTagCommand(Index personIndex, Index preferenceIndex, Set<String> tagsToDelete) {
        requireAllNonNull(personIndex, preferenceIndex, tagsToDelete);

        this.targetPersonIndex = personIndex;
        this.targetPreferenceIndex = preferenceIndex;
        this.tagsToDelete = tagsToDelete;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedFilteredPersonList();

        if (targetPersonIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, MESSAGE_USAGE));
        }
        Person targetPerson = lastShownList.get(targetPersonIndex.getZeroBased());

        List<PropertyPreference> targetPreferenceList = targetPerson.getPropertyPreferences();
        if (targetPreferenceIndex.getZeroBased() >= targetPreferenceList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PREFERENCE_DISPLAYED_INDEX,
                    MESSAGE_USAGE));
        }

        PropertyPreference preferenceToDelete = targetPreferenceList.get(targetPreferenceIndex.getZeroBased());

        TagRegistry tagRegistry = TagRegistry.of();
        Set<Tag> tags = new HashSet<>();

        for (String tagName : tagsToDelete) {
            Tag tag = new Tag(tagName, new ArrayList<>(), new ArrayList<>());
            if (!tagRegistry.contains(tag)) {
                throw new CommandException(String.format(Messages.MESSAGE_TAG_DOES_NOT_EXIST, tagName,
                        MESSAGE_USAGE));
            }
            Tag tagToRemove = tagRegistry.get(tagName);
            if (!preferenceToDelete.getTags().contains(tag)) {
                throw new CommandException(String.format(Messages.MESSAGE_TAG_NOT_FOUND_IN_PREFERENCE, tagName,
                        MESSAGE_USAGE));
            }
            tags.add(tagToRemove);
        }

        for (Tag tag : tags) {
            tag.removePropertyPreference(preferenceToDelete);
            tagRegistry.setTag(tag, tag);
            preferenceToDelete.removeTag(tag);
        }

        model.setPerson(targetPerson, targetPerson);
        model.resetAllLists();

        return new CommandResult(String.format(MESSAGE_DELETE_PREFERENCE_TAG_SUCCESS,
                Messages.format(targetPerson, preferenceToDelete), Messages.format(tags)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeletePreferenceTagCommand
                && targetPersonIndex.equals(((DeletePreferenceTagCommand) other).targetPersonIndex)
                && targetPreferenceIndex.equals(((DeletePreferenceTagCommand) other).targetPreferenceIndex)
                && tagsToDelete.equals(((DeletePreferenceTagCommand) other).tagsToDelete));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", targetPersonIndex)
                .add("preferenceIndex", targetPreferenceIndex)
                .add("tagsToDelete", tagsToDelete)
                .toString();
    }
}
