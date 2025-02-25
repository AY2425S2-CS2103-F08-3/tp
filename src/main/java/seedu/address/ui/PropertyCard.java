package seedu.address.ui;

import java.util.ArrayList;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.property.Property;
import seedu.address.model.tag.Tag;


/**
 * An UI component that displays information of a {@code Person}.
 */
public class PropertyCard extends UiPart<Region> {

    private static final String FXML = "PropertyListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Property property;
    private OwnerListPanel ownerListPanel;
    @FXML
    private StackPane ownerListPanelPlaceholder;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label postalCode;
    @FXML
    private Label houseNumber;
    @FXML
    private Label unitNumber;
    @FXML
    private Label name;

    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PropertyCode} with the given {@code Property} and index to display.
     */
    public PropertyCard(Property property, int displayedIndex) {
        super(FXML);
        this.property = property;
        id.setText(displayedIndex + ". ");
        postalCode.setText("Postal Code: " + property.getPostalCode());

        houseNumber.setVisible(!property.getHouseNumber().equals(""));
        houseNumber.setManaged(!property.getHouseNumber().equals(""));
        houseNumber.setText("House Number: " + property.getHouseNumber());

        unitNumber.setVisible(!property.getUnitNumber().equals(""));
        unitNumber.setManaged(!property.getUnitNumber().equals(""));
        unitNumber.setText("Unit Number: " + property.getUnitNumber());


        name.setText(property.getName());

        property.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        ownerListPanel = new OwnerListPanel(FXCollections.observableList(property.getOwners()));
        ownerListPanelPlaceholder.getChildren().add(ownerListPanel.getRoot());


    }
}


