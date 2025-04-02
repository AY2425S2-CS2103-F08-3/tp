package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CommandUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.search.SearchType;
import seedu.address.model.search.comparators.ListingPreferenceScoreComparator;
import seedu.address.model.search.predicates.ListingMatchesPreferencePredicate;

/**
 * Find matches for @{code Person}'s {@code PropertyPreference} based on tags and attributes (i.e. within price range),
 * ranking them based on number of tags and attributes matching.
 */
public class MatchPreferenceCommand extends Command {

    public static final String COMMAND_WORD = "matchPreference";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Find listings that match a person's property preference identified by index number used "
            + "in the displayed person and preference list.\n"
            + "Parameters: PERSON_INDEX (must be a positive integer) PREFERENCE_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_MATCH_PERSON_SUCCESS = "Matched Listings for %1$s's Preference - "
            + "Price Range: %2$s, Tags: %3$s";

    private final Index targetPersonIndex;
    private final Index targetPreferenceIndex;

    /**
     * Creates a {@code MatchPreferenceCommand} to match {@code Listing}s from the specified {@code PropertyPreference}.
     *
     * @param targetPersonIndex     Index of the person in the filtered person list to delete the preference from.
     * @param targetPreferenceIndex Index of the preference in the person to delete.
     */
    public MatchPreferenceCommand(Index targetPersonIndex, Index targetPreferenceIndex) {
        requireAllNonNull(targetPersonIndex, targetPreferenceIndex);

        this.targetPersonIndex = targetPersonIndex;
        this.targetPreferenceIndex = targetPreferenceIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.getSearchContext().setActivePriceRange(null);
        model.getSearchContext().setActiveSearchTags(new HashSet<>());
        Person targetPerson = CommandUtil.getValidatedPerson(model, targetPersonIndex, MESSAGE_USAGE);

        List<PropertyPreference> targetPreferenceList = targetPerson.getPropertyPreferences()
                .stream()
                .filter(preference -> model.getSearchContext().matches(preference))
                .toList();

        if (targetPreferenceIndex.getZeroBased() >= targetPreferenceList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PREFERENCE_DISPLAYED_INDEX,
                    MESSAGE_USAGE));
        }

        PropertyPreference preferenceToMatch = targetPreferenceList.get(targetPreferenceIndex.getZeroBased());
        matchPreference(model, preferenceToMatch);

        String successMessage = String.format(MESSAGE_MATCH_PERSON_SUCCESS,
                targetPerson.getName(),
                preferenceToMatch.getPriceRange(),
                Messages.format(preferenceToMatch.getTags()));

        return new CommandResult(successMessage);
    }

    private void matchPreference(Model model, PropertyPreference preferenceToMatch) {
        requireAllNonNull(model, preferenceToMatch);

        model.resetAllLists();

        model.setSearch(preferenceToMatch.getTags(),
                preferenceToMatch.getPriceRange(),
                SearchType.LISTING,
                Model.PREDICATE_SHOW_ALL_PROPERTY_PREFERENCES);

        model.updateFilteredListingList(new ListingMatchesPreferencePredicate(preferenceToMatch));
        model.updateSortedFilteredListingList(new ListingPreferenceScoreComparator(preferenceToMatch));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MatchPreferenceCommand)) {
            return false;
        }

        MatchPreferenceCommand otherDeleteCommand = (MatchPreferenceCommand) other;
        return targetPersonIndex.equals(otherDeleteCommand.targetPersonIndex)
                && targetPreferenceIndex.equals(otherDeleteCommand.targetPreferenceIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetPersonIndex", targetPersonIndex)
                .add("targetPreferenceIndex", targetPreferenceIndex)
                .toString();
    }
}
