package seedu.address.model.person.predicates;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s property preferences matches a {@code Listing}.
 * Used for {@code MatchListingCommand}.
 */
public class PersonMatchesPropertyPredicate implements Predicate<Person> {
    private final Listing listingToMatch;

    public PersonMatchesPropertyPredicate(Listing listingToMatch) {
        this.listingToMatch = listingToMatch;
    }

    @Override
    public boolean test(Person person) {
        List<PropertyPreference> propertyPreferences = person.getPropertyPreferences();

        // If person has no preferences, reject everything.
        if (propertyPreferences.isEmpty()) {
            return false;
        }

        // If person owns the listing, reject.
        if (listingToMatch.getOwners().contains(person)) {
            return false;
        }

        // If listing is not available, reject.
        if (!listingToMatch.getAvailability()) {
            return false;
        }

        Set<Tag> tagsToMatch = listingToMatch.getTags();
        for (PropertyPreference pref : propertyPreferences) {
            // If any tag matches, return true.
            if (!Collections.disjoint(pref.getTags(), tagsToMatch)) {
                return true;
            }

            // If price range matches, return true.
            if (pref.getPriceRange().doPriceRangeOverlap(listingToMatch.getPriceRange())) {
                return true;
            }
        }

        // If no preferences match, return false.
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PersonMatchesPropertyPredicate
                && listingToMatch.equals(((PersonMatchesPropertyPredicate) other).listingToMatch));
    }
}
