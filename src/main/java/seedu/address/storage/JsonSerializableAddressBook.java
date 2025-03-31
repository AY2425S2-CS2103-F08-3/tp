package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_LISTING = "Listings list contains duplicate listing(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedListing> listings = new ArrayList<>();
    private final List<JsonAdaptedTag> tags = new ArrayList<>();


    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                       @JsonProperty("listings") List<JsonAdaptedListing> listings,
                                       @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.persons.addAll(persons);
        this.listings.addAll(listings);
        this.tags.addAll(tags);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).toList());
        listings.addAll(source.getListingList().stream().map(JsonAdaptedListing::new).toList());
        tags.addAll(source.getTagMap().keySet().stream().map(JsonAdaptedTag::new).toList());
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        addTags(addressBook);
        addPersons(addressBook);
        addListings(addressBook);

        return addressBook;
    }

    /**
     * Creates and returns a list of Person objects from the JSON-adapted persons.
     *
     * @throws IllegalValueException If there are duplicate persons.
     */
    private void addPersons(AddressBook addressBook) throws IllegalValueException {

        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType(addressBook);

            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }

            addressBook.addPerson(person);
        }
    }

    /**
     * Creates Listing objects from the JSON-adapted listings and adds them to the AddressBook.
     *
     * @param addressBook The AddressBook to add the listings to and get owners.
     * @throws IllegalValueException If there are duplicate listings.
     */
    private void addListings(AddressBook addressBook)
            throws IllegalValueException {
        for (JsonAdaptedListing jsonAdaptedListing : listings) {
            Listing listing = jsonAdaptedListing.toModelType(addressBook);
            if (addressBook.hasListing(listing)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_LISTING);
            }
            addressBook.addListing(listing);
        }
    }

    /**
     * Adds tags from the JSON-adapted tags to the AddressBook.
     */
    private void addTags(AddressBook addressBook) throws IllegalValueException {
        Set<String> tagList = new HashSet<>();
        for (JsonAdaptedTag jsonAdaptedTag : tags) {
            tagList.add(jsonAdaptedTag.getTagName());
        }
        addressBook.addTags(tagList);
    }

}
