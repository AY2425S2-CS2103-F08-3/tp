package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.person.predicates.PersonPropertyPreferencesContainAllTagsPredicate;
import seedu.address.model.person.predicates.PropertyPreferencesContainAllActiveSearchTagsPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;

/**
 * Searches for persons whose property preferences contain all specified tags.
 */
public class SearchPersonByTagCommand extends Command {
    public static final String COMMAND_WORD = "searchPerson";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons with property preferences "
            + "containing all specified tags.\n"
            + "Parameters: " + PREFIX_TAG + "TAG [" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " t/gym t/pet-friendly";

    private final Set<String> tagsToSearch;

    /**
     * Constructs a {@code SearchPersonByTagCommand} with the given set of tags to search for.
     *
     * @param tagsToSearch The set of tag names to search for in persons' property preferences.
     */
    public SearchPersonByTagCommand(Set<String> tagsToSearch) {
        requireNonNull(tagsToSearch);
        this.tagsToSearch = tagsToSearch;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (tagsToSearch.isEmpty()) {
            throw new CommandException(String.format(Messages.MESSAGE_SEARCH_PERSON_TAG_MISSING_PARAMS, MESSAGE_USAGE));
        }

        // Validate each tag exists
        for (String tagName : tagsToSearch) {
            if (!model.hasTags(Set.of(tagName))) {
                throw new CommandException(String
                        .format(Messages.MESSAGE_TAG_DOES_NOT_EXIST, tagName, MESSAGE_USAGE));
            }
        }

        List<Tag> activeTags = new ArrayList<>();
        for (String tagName : tagsToSearch) {
            activeTags.add(TagRegistry.of().get(tagName));
        }
        Tag.setActiveSearchTags(activeTags);

        PropertyPreference.setFilterPredicate(new PropertyPreferencesContainAllActiveSearchTagsPredicate());

        PersonPropertyPreferencesContainAllTagsPredicate predicate =
                new PersonPropertyPreferencesContainAllTagsPredicate(tagsToSearch);
        model.updateFilteredPersonList(predicate);

        List<Person> filteredPersons = model.getSortedFilteredPersonList();

        if (filteredPersons.isEmpty()) {
            return new CommandResult(Messages.MESSAGE_SEARCH_PERSON_TAGS_NO_MATCH);
        } else {
            return new CommandResult(String.format(Messages.MESSAGE_SEARCH_PERSON_TAGS_SUCCESS,
                    filteredPersons.size()));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SearchPersonByTagCommand
                && tagsToSearch.equals(((SearchPersonByTagCommand) other).tagsToSearch));
    }
}
