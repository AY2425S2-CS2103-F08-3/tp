package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddListingCommand;
import seedu.address.logic.commands.AddListingTagCommand;
import seedu.address.logic.commands.AddOwnerCommand;
import seedu.address.logic.commands.AddPersonCommand;
import seedu.address.logic.commands.AddPreferenceCommand;
import seedu.address.logic.commands.AddPreferenceTagCommand;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteListingCommand;
import seedu.address.logic.commands.DeleteListingTagCommand;
import seedu.address.logic.commands.DeleteOwnerCommand;
import seedu.address.logic.commands.DeletePersonCommand;
import seedu.address.logic.commands.DeletePreferenceCommand;
import seedu.address.logic.commands.DeletePreferenceTagCommand;
import seedu.address.logic.commands.DeleteTagCommand;
import seedu.address.logic.commands.EditPersonCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListListingCommand;
import seedu.address.logic.commands.ListPersonCommand;
import seedu.address.logic.commands.MarkAvailableCommand;
import seedu.address.logic.commands.MarkUnavailableCommand;
import seedu.address.logic.commands.MatchListingCommand;
import seedu.address.logic.commands.MatchPreferenceCommand;
import seedu.address.logic.commands.OverwriteListingTagCommand;
import seedu.address.logic.commands.OverwritePreferenceTagCommand;
import seedu.address.logic.commands.SearchListingByTagCommand;
import seedu.address.logic.commands.SearchOwnerListingCommand;
import seedu.address.logic.commands.SearchPersonByNameCommand;
import seedu.address.logic.commands.SearchPersonByTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput The full user input string.
     * @return the command based on the user input.
     * @throws ParseException if the user input does not conform the expected format.
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddPersonCommand.COMMAND_WORD:
            return new AddPersonCommandParser().parse(arguments);

        case AddListingCommand.COMMAND_WORD:
            return new AddListingCommandParser().parse(arguments);

        case AddPreferenceCommand.COMMAND_WORD:
            return new AddPreferenceCommandParser().parse(arguments);

        case AddTagCommand.COMMAND_WORD:
            return new AddTagCommandParser().parse(arguments);

        case DeletePersonCommand.COMMAND_WORD:
            return new DeletePersonCommandParser().parse(arguments);

        case DeleteListingCommand.COMMAND_WORD:
            return new DeleteListingCommandParser().parse(arguments);

        case DeleteOwnerCommand.COMMAND_WORD:
            return new DeleteOwnerCommandParser().parse(arguments);

        case DeleteTagCommand.COMMAND_WORD:
            return new DeleteTagCommandParser().parse(arguments);

        case DeletePreferenceCommand.COMMAND_WORD:
            return new DeletePreferenceCommandParser().parse(arguments);

        case AddListingTagCommand.COMMAND_WORD:
            return new AddListingTagCommandParser().parse(arguments);

        case AddPreferenceTagCommand.COMMAND_WORD:
            return new AddPreferenceTagCommandParser().parse(arguments);

        case SearchPersonByNameCommand.COMMAND_WORD:
            return new SearchPersonByNameCommandParser().parse(arguments);

        case MatchListingCommand.COMMAND_WORD:
            return new MatchListingCommandParser().parse(arguments);

        case MatchPreferenceCommand.COMMAND_WORD:
            return new MatchPreferenceCommandParser().parse(arguments);

        case SearchOwnerListingCommand.COMMAND_WORD:
            return new SearchOwnerListingCommandParser().parse(arguments);

        case EditPersonCommand.COMMAND_WORD:
            return new EditPersonCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case MarkUnavailableCommand.COMMAND_WORD:
            return new MarkUnavailableCommandParser().parse(arguments);

        case MarkAvailableCommand.COMMAND_WORD:
            return new MarkAvailableCommandParser().parse(arguments);

        case ListPersonCommand.COMMAND_WORD:
            return new ListPersonCommand();

        case ListListingCommand.COMMAND_WORD:
            return new ListListingCommand();

        case AddOwnerCommand.COMMAND_WORD:
            return new AddOwnerCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case DeleteListingTagCommand.COMMAND_WORD:
            return new DeleteListingTagCommandParser().parse(arguments);

        case DeletePreferenceTagCommand.COMMAND_WORD:
            return new DeletePreferenceTagCommandParser().parse(arguments);

        case OverwritePreferenceTagCommand.COMMAND_WORD:
            return new OverwritePreferenceTagCommandParser().parse(arguments);

        case OverwriteListingTagCommand.COMMAND_WORD:
            return new OverwriteListingTagCommandParser().parse(arguments);

        case SearchPersonByTagCommand.COMMAND_WORD:
            return new SearchPersonByTagCommandParser().parse(arguments);

        case SearchListingByTagCommand.COMMAND_WORD:
            return new SearchListingByTagCommandParser().parse(arguments);

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
