package seedu.address.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddListingCommand;
import seedu.address.logic.commands.AddListingTagCommand;
import seedu.address.logic.commands.AddOwnerCommand;
import seedu.address.logic.commands.AddPersonCommand;
import seedu.address.logic.commands.AddPreferenceCommand;
import seedu.address.logic.commands.AddPreferenceTagCommand;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.logic.commands.ClearCommand;
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

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2425s2-cs2103-f08-3.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "For more help, visit: " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private GridPane commandGrid;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);

        ArrayList<String[]> helpTexts = getHelpMessages();

        // Add the help text to the GridPane
        for (int i = 0; i < helpTexts.size(); i++) {

            Label commandText = new Label(helpTexts.get(i)[0]);
            Label descriptionText = new Label(helpTexts.get(i)[1]);

            descriptionText.setWrapText(true);
            descriptionText.setMaxWidth(900);
            commandText.getStyleClass().add("text-table-row");
            descriptionText.getStyleClass().add("text-table-row");

            commandGrid.addRow(i + 1,
                    commandText,
                    descriptionText);
        }
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application
     *             Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
        getRoot().setIconified(false);
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }

    private ArrayList<String[]> getHelpMessages() {
        ArrayList<String[]> helpTexts = new ArrayList<>();
        helpTexts.addAll(getAddCommandsHelpMessages());
        helpTexts.addAll(getListCommandsHelpMessages());
        helpTexts.addAll(getFindCommandsHelpMessages());
        helpTexts.addAll(getEditCommandsHelpMessages());
        helpTexts.addAll(getDeleteCommandsHelpMessages());
        helpTexts.addAll(getMiscCommandsHelpMessages());

        return helpTexts;
    }

    private ArrayList<String[]> getAddCommandsHelpMessages() {
        ArrayList<String[]> helpTexts = new ArrayList<>();
        helpTexts.add(new String[]{AddPersonCommand.COMMAND_WORD, AddPersonCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{AddListingCommand.COMMAND_WORD, AddListingCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{AddTagCommand.COMMAND_WORD, AddTagCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{AddPreferenceCommand.COMMAND_WORD, AddPreferenceCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{AddListingTagCommand.COMMAND_WORD, AddListingTagCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{AddPreferenceTagCommand.COMMAND_WORD, AddPreferenceTagCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{AddOwnerCommand.COMMAND_WORD, AddOwnerCommand.MESSAGE_USAGE});
        return helpTexts;
    }

    private ArrayList<String[]> getListCommandsHelpMessages() {
        ArrayList<String[]> helpTexts = new ArrayList<>();
        helpTexts.add(new String[]{ListPersonCommand.COMMAND_WORD, ListPersonCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{ListListingCommand.COMMAND_WORD, ListListingCommand.MESSAGE_USAGE});
        return helpTexts;
    }

    private ArrayList<String[]> getFindCommandsHelpMessages() {
        ArrayList<String[]> helpTexts = new ArrayList<>();
        helpTexts.add(new String[]{SearchPersonByNameCommand.COMMAND_WORD, SearchPersonByNameCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{SearchPersonByTagCommand.COMMAND_WORD, SearchPersonByTagCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{SearchListingByTagCommand.COMMAND_WORD, SearchListingByTagCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{SearchOwnerListingCommand.COMMAND_WORD, SearchOwnerListingCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{MatchPreferenceCommand.COMMAND_WORD, MatchPreferenceCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{MatchListingCommand.COMMAND_WORD, MatchListingCommand.MESSAGE_USAGE});
        return helpTexts;
    }

    private ArrayList<String[]> getEditCommandsHelpMessages() {
        ArrayList<String[]> helpTexts = new ArrayList<>();
        helpTexts.add(new String[]{EditPersonCommand.COMMAND_WORD, EditPersonCommand.MESSAGE_USAGE});
        helpTexts.add(
                new String[]{OverwriteListingTagCommand.COMMAND_WORD, OverwriteListingTagCommand.MESSAGE_USAGE}
        );
        helpTexts.add(
                new String[]{OverwritePreferenceTagCommand.COMMAND_WORD, OverwritePreferenceTagCommand.MESSAGE_USAGE}
        );
        helpTexts.add(new String[]{MarkAvailableCommand.COMMAND_WORD, MarkAvailableCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{MarkUnavailableCommand.COMMAND_WORD, MarkUnavailableCommand.MESSAGE_USAGE});
        return helpTexts;
    }

    private ArrayList<String[]> getDeleteCommandsHelpMessages() {
        ArrayList<String[]> helpTexts = new ArrayList<>();
        helpTexts.add(new String[]{DeletePersonCommand.COMMAND_WORD, DeletePersonCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{DeleteListingCommand.COMMAND_WORD, DeleteListingCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{DeleteTagCommand.COMMAND_WORD, DeleteTagCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{DeletePreferenceCommand.COMMAND_WORD, DeletePreferenceCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{DeleteListingTagCommand.COMMAND_WORD, DeleteListingTagCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{DeletePreferenceTagCommand.COMMAND_WORD, DeletePreferenceTagCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{DeleteOwnerCommand.COMMAND_WORD, DeleteOwnerCommand.MESSAGE_USAGE});
        return helpTexts;
    }

    private ArrayList<String[]> getMiscCommandsHelpMessages() {
        ArrayList<String[]> helpTexts = new ArrayList<>();
        helpTexts.add(new String[]{HelpCommand.COMMAND_WORD, HelpCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{ClearCommand.COMMAND_WORD, ClearCommand.MESSAGE_USAGE});
        helpTexts.add(new String[]{ExitCommand.COMMAND_WORD, ExitCommand.MESSAGE_USAGE});
        return helpTexts;
    }

}
