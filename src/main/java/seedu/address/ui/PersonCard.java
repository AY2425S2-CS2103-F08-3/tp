package seedu.address.ui;

import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.person.Person;
import seedu.address.model.person.PropertyPreference;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    public final Person person;
    private PreferenceListPanel preferenceListPanel;

    @FXML
    private HBox cardPane;
    @FXML
    private FlowPane personTags;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label email;

    @FXML
    private StackPane preferenceListPanelPlaceholder;

    /**
     * Creates a {@code PersonCode} with the given {@code Person}, index, and current search tags.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);

        if (!person.getListings().isEmpty()) {
            Label sellerTag = new Label("SELLER");
            sellerTag.getStyleClass().add("seller-tag");
            personTags.getChildren().add(sellerTag);
        }

        // Filter preferences if search tags are present
        ObservableList<PropertyPreference> filteredPreferences = FXCollections.observableArrayList(
                person.getPropertyPreferences().stream()
                        .filter(PropertyPreference::isActive)
                        .collect(Collectors.toList())
        );

        if (!filteredPreferences.isEmpty()) {
            Label buyerTag = new Label("BUYER");
            buyerTag.getStyleClass().add("buyer-tag");
            personTags.getChildren().add(buyerTag);

            preferenceListPanel = new PreferenceListPanel(filteredPreferences);
            preferenceListPanelPlaceholder.getChildren().add(preferenceListPanel.getRoot());
        }
    }
}
