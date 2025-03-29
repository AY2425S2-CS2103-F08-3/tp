package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.SearchPersonByTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

public class SearchPersonByTagCommandParserTest {

    private final SearchPersonByTagCommandParser parser = new SearchPersonByTagCommandParser();

    @Test
    public void parse_validTags_success() throws Exception {
        SearchPersonByTagCommand command = parser.parse(" t/gym t/pool");
        assertEquals(new SearchPersonByTagCommand(Set.of("gym", "pool")), command);
    }

    @Test
    public void parse_emptyInput_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(""));
    }

    @Test
    public void parse_missingTagValue_throwsParseException() {
        ParseException thrown = assertThrows(ParseException.class, () -> parser.parse(" t/"));
        assertEquals(Tag.MESSAGE_CONSTRAINTS, thrown.getMessage());
    }

    @Test
    public void parse_tagsWithSpaces_trimmedAndParsed() throws Exception {
        SearchPersonByTagCommand command = parser.parse(" t/ gym t/ pool ");
        assertEquals(new SearchPersonByTagCommand(Set.of("gym", "pool")), command);
    }

    @Test
    public void parse_missingPrefixOrEmpty_throwsParseException() {
        ParseException thrown = assertThrows(ParseException.class, () -> parser.parse("random input"));
        assertEquals(String.format(Messages.MESSAGE_SEARCH_PERSON_TAG_MISSING_PARAMS,
                SearchPersonByTagCommand.MESSAGE_USAGE), thrown.getMessage());

        ParseException thrownEmpty = assertThrows(ParseException.class, () -> parser.parse(""));
        assertEquals(String.format(Messages.MESSAGE_ARGUMENTS_EMPTY, SearchPersonByTagCommand.MESSAGE_USAGE),
                thrownEmpty.getMessage());
    }

    @Test
    public void parse_invalidTagName_throwsParseException() {
        ParseException thrown = assertThrows(ParseException.class, () -> parser.parse(" t/invalid#tag"));
        assertEquals(Tag.MESSAGE_CONSTRAINTS, thrown.getMessage());
    }
}
