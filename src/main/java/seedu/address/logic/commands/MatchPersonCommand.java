package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Find matches for @{code Person}'s {@code PropertyPreference} based on tags and attributes (i.e. within price range),
 * ranking them based on number of tags and attributes matching.
 */
public class MatchPersonCommand extends Command {

    public static final String COMMAND_WORD = "matchPerson";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Find listings that match a person's property preference identified by index number used "
            + "in the displayed person and preference list.\n"
            + "Parameters: PERSON_INDEX  (must be a positive integer) PREFERENCE_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_MATCH_PERSON_SUCCESS = "Matched Listings for %1$s's Preference - "
            + "Price Range: %2$s, Tags: %3$s";

    private final Index targetPersonIndex;
    private final Index targetPreferenceIndex;

    /**
     * Creates a {@code MatchPersonCommand} to match {@code Listing}s from the specified {@code PropertyPreference}.
     *
     * @param targetPersonIndex Index of the person in the filtered person list to delete the preference from.
     * @param targetPreferenceIndex Index of the preference in the person to delete.
     */
    public MatchPersonCommand(Index targetPersonIndex, Index targetPreferenceIndex) {
        this.targetPersonIndex = targetPersonIndex;
        this.targetPreferenceIndex = targetPreferenceIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetPersonIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person targetPerson = lastShownList.get(targetPersonIndex.getZeroBased());

        List<PropertyPreference> targetPreferenceList = targetPerson.getPropertyPreferences();
        if (targetPreferenceIndex.getZeroBased() >= targetPreferenceList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PREFERENCE_DISPLAYED_INDEX);
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
        HashMap<Listing, Integer> listingScores = new HashMap<>();

        model.updateFilteredListingList(model.PREDICATE_SHOW_ALL_LISTINGS);
        model.updateSortedFilteredListingList(model.COMPARATOR_SHOW_ALL_LISTINGS);

        for (Listing listing : model.getFilteredListingList()) {
            int score = 0;

            if (preferenceToMatch.getPriceRange().doPriceRangeOverlap(listing.getPriceRange())) {
                score += 1;
            }

            for (Tag tag : preferenceToMatch.getTags()) {
                if (listing.getTags().contains(tag)) {
                    score += 1;
                }
            }

            if (score == 0) {
                continue;
            }

            listingScores.put(listing, score);
        }

        Predicate<Listing> predicate = listing -> listingScores.containsKey(listing);

        Comparator<Listing> comparator = (listing1, listing2) -> {
            int score1 = listingScores.getOrDefault(listing1, 0);
            int score2 = listingScores.getOrDefault(listing2, 0);
            return Integer.compare(score2, score1);
        };

        model.updateFilteredListingList(predicate);
        model.updateSortedFilteredListingList(comparator);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MatchPersonCommand)) {
            return false;
        }

        MatchPersonCommand otherDeleteCommand = (MatchPersonCommand) other;
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
