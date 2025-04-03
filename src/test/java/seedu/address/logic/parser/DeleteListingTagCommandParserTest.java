package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_LISTING_TAG_REQUIRED_FOR_DELETE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteListingTagCommand;
import seedu.address.model.tag.Tag;

/**
 * Unit tests for DeleteListingTagCommandParser.
 */
public class DeleteListingTagCommandParserTest {

    private final DeleteListingTagCommandParser parser = new DeleteListingTagCommandParser();

    @Test
    public void parse_validInput_success() {
        Index expectedIndex = Index.fromOneBased(3);
        Set<String> expectedTags = Set.of("pet-friendly", "pool");

        String userInput = "3 " + PREFIX_TAG + "pet-friendly " + PREFIX_TAG + "pool";
        DeleteListingTagCommand expectedCommand = new DeleteListingTagCommand(expectedIndex, expectedTags);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingIndex_failure() {
        String userInput = PREFIX_TAG + "pet-friendly";
        String expectedMessage = String.format(MESSAGE_LISTING_TAG_REQUIRED_FOR_DELETE,
                DeleteListingTagCommand.MESSAGE_USAGE);

        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_missingTags_failure() {
        String userInput = "3"; // No tags provided
        String expectedMessage = String.format(MESSAGE_LISTING_TAG_REQUIRED_FOR_DELETE,
                DeleteListingTagCommand.MESSAGE_USAGE);

        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String userInput = "abc " + PREFIX_TAG + "pet-friendly";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteListingTagCommand.MESSAGE_USAGE);

        assertParseFailure(parser, userInput, "Index is not a non-zero unsigned integer.");
    }

    @Test
    public void parse_blankTag_failure() {
        String userInput = "3 " + PREFIX_TAG + " "; // empty tag value
        assertParseFailure(parser, userInput, Tag.MESSAGE_CONSTRAINTS);
    }
}
