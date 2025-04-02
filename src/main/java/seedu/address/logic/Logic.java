package seedu.address.logic;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.listing.Listing;
import seedu.address.model.person.Person;
import seedu.address.model.search.SearchContext;
import seedu.address.model.tag.Tag;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     *
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException if an error occurs during command execution.
     * @throws ParseException if an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the AddressBook.
     *
     * @see seedu.address.model.Model#getAddressBook()
     */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns an unmodifiable view of the filtered list of persons
     */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Returns an unmodifiable view of the sorted filtered list of persons
     */
    ObservableList<Person> getSortedFilteredPersonList();

    /**
     * Returns an unmodifiable view of the filtered list of listings
     */
    ObservableList<Listing> getFilteredListingList();

    /**
     * Returns an unmodifiable view of the sorted filtered list of listings
     */
    ObservableList<Listing> getSortedFilteredListingList();

    /**
     * Returns an unmodifiable view of the filtered list of tags
     */
    ObservableList<Tag> getFilteredTagList();

    /**
     * Returns an unmodifiable view of the sorted filtered list of tags
     */
    ObservableList<Tag> getSortedFilteredTagList();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the current search context.
     */
    SearchContext getSearchContext();
}
