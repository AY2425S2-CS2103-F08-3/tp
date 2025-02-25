package seedu.address.ui;

import java.util.Comparator;
import java.util.HashSet;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PreferenceCard extends UiPart<Region> {

    private static final String FXML = "PreferenceListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final HashSet<Tag> tagList;
    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    @FXML
    private StackPane preferenceListPanelPlaceholder;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PreferenceCard(HashSet<Tag> tagList, int displayedIndex) {
        super(FXML);
        this.tagList = tagList;
        id.setText(displayedIndex + ". ");

        tagList.stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
