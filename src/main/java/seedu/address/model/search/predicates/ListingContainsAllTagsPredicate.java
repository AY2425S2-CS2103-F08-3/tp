package seedu.address.model.search.predicates;

import static java.util.Objects.requireNonNull;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.model.listing.Listing;
import seedu.address.model.tag.Tag;

/**
 * Tests if a {@code Listing} contains all specified tags.
 */
public class ListingContainsAllTagsPredicate implements Predicate<Listing> {
    private final Set<String> tagsToMatch;

    /**
     * Constructs a {@code ListingContainsAllTagsPredicate} with the specified tags to match.
     *
     * @param tagsToMatch The set of tags to match.
     */
    public ListingContainsAllTagsPredicate(Set<String> tagsToMatch) {
        requireNonNull(tagsToMatch);

        this.tagsToMatch = tagsToMatch;
    }

    @Override
    public boolean test(Listing listing) {
        requireNonNull(listing);

        Set<String> listingTags = listing.getTags().stream()
                .map(Tag::getTagName)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        return listingTags.containsAll(tagsToMatch.stream().map(String::toLowerCase).collect(Collectors.toSet()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ListingContainsAllTagsPredicate
                && tagsToMatch.equals(((ListingContainsAllTagsPredicate) other).tagsToMatch));
    }
}
