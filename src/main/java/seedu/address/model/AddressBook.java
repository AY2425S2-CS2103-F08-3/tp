package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.listing.Listing;
import seedu.address.model.listing.UniqueListingList;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagRegistry;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueListingList listings;
    private final TagRegistry tagRegistry;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        listings = new UniqueListingList();
        tagRegistry = TagRegistry.of();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the listing list with {@code listings}.
     * Ensures that {@code listings} does not contain duplicate listings.
     *
     * @param listings The new list of listings.
     */
    public void setListings(List<Listing> listings) {
        this.listings.setListings(listings);
    }

    /**
     * Replaces the given listing {@code target} with {@code editedListing}.
     * Ensures that the {@code target} exists in the address book.
     *
     * @param target The original listing to be replaced.
     * @param editedPerson The new listing replacing the target.
     */
    public void setListings(Listing target, Listing editedPerson) {
        requireNonNull(editedPerson);
        listings.setListing(target, editedPerson);
    }

    /**
     * Returns an unmodifiable view of the tag list.
     *
     * @return An ObservableMap representing the tag list.
     */
    public ObservableMap<String, Tag> getTagList() {
        return tagRegistry.asUnmodifiableObservableMap();
    }

    /**
     * Returns an unmodifiable view of the listing list.
     *
     * @return An ObservableList of listings.
     */
    @Override
    public ObservableList<Listing> getListingList() {
        return listings.asUnmodifiableObservableList();
    }


    /**
     * Adds a listing to the address book.
     * Ensures that the listing does not already exist in the address book.
     *
     * @param listing The listing to add.
     */
    public void addListing(Listing listing) {
        listings.add(listing);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setListings(newData.getListingList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Returns true if a listing with the same identity as {@code listing} exists in the address book.
     *
     * @param listing The listing to check for.
     * @return True if the listing exists, false otherwise.
     */
    public boolean hasListing(Listing listing) {
        requireNonNull(listing);
        return listings.contains(listing);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /**
     * Adds multiple tags to the tag registry.
     *
     * @param tags A set of tags to add.
     */
    public void addTags(Set<String> tags) {
        requireNonNull(tags);
        TagRegistry tagRegistry = TagRegistry.of();
        for (String tagName : tags) {
            tagRegistry.add(new Tag(tagName, new ArrayList<>(), new ArrayList<>()));
        }
    }

    /**
     * Checks if all given tags exist in the tag registry.
     *
     * @param tags A set of tags to check.
     * @return True if all tags exist, false otherwise.
     */
    public boolean hasTags(Set<String> tags) {
        requireNonNull(tags);
        TagRegistry tagRegistry = TagRegistry.of();
        for (String tagName : tags) {
            Tag tag = new Tag(tagName, new ArrayList<>(), new ArrayList<>());
            if (!tagRegistry.contains(tag)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if at least one of the given tags exists in the tag registry.
     *
     * @param tags A set of tags to check.
     * @return True if at least one tag exists, false otherwise.
     */
    public boolean hasNewTags(Set<String> tags) {
        requireNonNull(tags);
        TagRegistry tagRegistry = TagRegistry.of();
        for (String tagName : tags) {
            Tag tag = new Tag(tagName, new ArrayList<>(), new ArrayList<>());
            if (tagRegistry.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a listing to the specified tags.
     *
     * @param tags The set of tags to associate with the listing.
     * @param listing The listing to add to the tags.
     */
    public void addListingToTags(Set<String> tags, Listing listing) {
        requireNonNull(tags);
        TagRegistry tagRegistry = TagRegistry.of();
        for (String tag : tags) {
            tagRegistry.addListingToTag(tag, listing);
        }
    }

    /**
     * Associates a set of tags with a property preference.
     *
     * @param tags The tags to associate.
     * @param preference The property preference to associate with.
     */
    public void addPreferenceToTags(Set<String> tags, PropertyPreference preference) {
        requireNonNull(tags);
        TagRegistry tagRegistry = TagRegistry.of();
        for (String tag : tags) {
            tagRegistry.addPropertyPreferenceToTag(tag, preference);
        }
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }

}
