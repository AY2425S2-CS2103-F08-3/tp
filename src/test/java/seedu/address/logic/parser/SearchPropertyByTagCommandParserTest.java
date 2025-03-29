package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.SearchPropertyByTagCommand;
import seedu.address.model.tag.Tag;

public class SearchPropertyByTagCommandParserTest {

    private final SearchPropertyByTagCommandParser parser = new SearchPropertyByTagCommandParser();

    @Test
    public void parse_validInput_success() {
        Set<String> expectedTags = Set.of("pet-friendly", "pool");
        String userInput = " t/pool t/pet-friendly";
        SearchPropertyByTagCommand expectedCommand = new SearchPropertyByTagCommand(expectedTags);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingPrefixOrRandomInput_showsUsage() {
        String userInput = "random words without prefix";
        assertParseFailure(parser, userInput, String.format(Messages.MESSAGE_SEARCH_PROPERTY_TAG_MISSING_PARAMS,
                SearchPropertyByTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_blankTagPrefix_failure() {
        String userInput = " t/ ";
        assertParseFailure(parser, userInput, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emptyArguments_showsUsage() {
        String userInput = " ";
        assertParseFailure(parser, userInput, String.format(Messages.MESSAGE_ARGUMENTS_EMPTY,
                SearchPropertyByTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTagName_throwsParseException() {
        // This tag is invalid based on Tag.MESSAGE_CONSTRAINTS
        String userInput = " t/!!!";
        assertParseFailure(parser, userInput, Tag.MESSAGE_CONSTRAINTS);
    }
}
