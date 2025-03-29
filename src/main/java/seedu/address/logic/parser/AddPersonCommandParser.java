package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_ADD_PERSON_PREAMBLE_FOUND;
import static seedu.address.logic.Messages.MESSAGE_ARGUMENTS_EMPTY;
import static seedu.address.logic.Messages.MESSAGE_EMAIL_REQUIRED;
import static seedu.address.logic.Messages.MESSAGE_NAME_REQUIRED;
import static seedu.address.logic.Messages.MESSAGE_PHONE_REQUIRED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.ArrayList;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Parses input arguments and creates a new {@code AddPersonCommandParser} object.
 */
public class AddPersonCommandParser implements Parser<AddPersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddPersonCommand
     * and returns an AddPersonCommand object for execution.
     *
     * @param args arguments to be parsed.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddPersonCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL);

        checkCommandFormat(argMultimap, args);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());

        Person person = new Person(name, phone, email, new ArrayList<>(), new ArrayList<>());
        return new AddPersonCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    private static void checkCommandFormat(ArgumentMultimap argMultimap, String args) throws ParseException {
        boolean hasName = argMultimap.getValue(PREFIX_NAME).isPresent();
        boolean hasPhone = argMultimap.getValue(PREFIX_PHONE).isPresent();
        boolean hasEmail = argMultimap.getValue(PREFIX_EMAIL).isPresent();

        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_ARGUMENTS_EMPTY,
                    AddPersonCommand.MESSAGE_USAGE));
        }

        if (!hasName) {
            throw new ParseException(String.format(MESSAGE_NAME_REQUIRED, AddPersonCommand.MESSAGE_USAGE));
        }

        if (!hasPhone) {
            throw new ParseException(String.format(MESSAGE_PHONE_REQUIRED, AddPersonCommand.MESSAGE_USAGE));
        }

        if (!hasEmail) {
            throw new ParseException(String.format(MESSAGE_EMAIL_REQUIRED, AddPersonCommand.MESSAGE_USAGE));
        }

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_ADD_PERSON_PREAMBLE_FOUND, AddPersonCommand.MESSAGE_USAGE));
        }
    }
}
