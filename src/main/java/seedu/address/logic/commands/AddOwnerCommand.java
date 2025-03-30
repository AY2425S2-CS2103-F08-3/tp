package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;

/**
 * Assigns a {@code Listing} identified using it's displayed index in the address book
 * to a {@code Person} identified using it's displayed index in the address book.
 */
public class AddOwnerCommand extends Command {
    public static final String COMMAND_WORD = "addOwner";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to a listing as its seller"
            + "Parameters: PERSON_INDEX (must be a positive integer) "
            + "LISTING_INDEX (must be a positive integer)"
            + "Example: " + COMMAND_WORD + " 2 1";

    public static final String MESSAGE_SUCCESS = "Listing %1$s";
    public static final String MESSAGE_OWNER_ALREADY_IN_LISTING = "This person is already an owner of this listing";

    private final Index personIndex;
    private final Index listingIndex;

    /**
     * Creates an {@code AddOwnerCommand} to add the specified {@code Listing} to {@code Person}.
     *
     * @param personIndex Index of the person in the filtered person list to add listing to
     * @param propertyIndex Index of the listing in the filtered listing list to add
     */
    public AddOwnerCommand(Index personIndex, Index propertyIndex) {
        this.personIndex = personIndex;
        this.listingIndex = propertyIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownPersonList = model.getSortedFilteredPersonList();
        if (personIndex.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person personToAddListing = lastShownPersonList.get(personIndex.getZeroBased());

        List<Listing> lastShownListingList = model.getSortedFilteredListingList();
        if (listingIndex.getZeroBased() >= lastShownListingList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LISTING_DISPLAYED_INDEX);
        }
        Listing listing = lastShownListingList.get(listingIndex.getZeroBased());

        if (listing.getOwners().contains(personToAddListing)) {
            throw new CommandException(MESSAGE_OWNER_ALREADY_IN_LISTING);
        }

        listing.addOwner(personToAddListing);
        personToAddListing.addListing(listing);

        model.setPerson(personToAddListing, personToAddListing);
        model.setListing(listing, listing);
        model.resetAllLists();

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(personToAddListing,
                listing)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddOwnerCommand)) {
            return false;
        }

        AddOwnerCommand otherAddOwnerCommand = (AddOwnerCommand) other;
        return personIndex.equals(otherAddOwnerCommand.personIndex)
                && listingIndex.equals(otherAddOwnerCommand.listingIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
