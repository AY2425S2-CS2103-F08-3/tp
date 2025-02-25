package seedu.address.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.property.Property;
import seedu.address.model.tag.Tag;

/**
 * Panel containing the list of properties.
 */
public class PropertyListPanel extends UiPart<Region> {
    private static final String FXML = "PropertyListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PropertyListPanel.class);

    @FXML
    private ListView<Property> propertyListView;

    /**
     * Creates a {@code PropertyListPanel} with the given {@code ObservableList}.
     */
    public PropertyListPanel(ObservableList<Property> propertyList) {
        super(FXML);
        propertyListView.setItems(propertyList);
        propertyListView.setCellFactory(listView -> new PropertyListViewCell());
    }


    public static ObservableList<Property> getSamplePropertyData(){
        ArrayList<Property> testData = new ArrayList<>();

        Set<Tag> tags = Arrays.stream(new String[]{"bleh", "ok"})
                .map(Tag::new)
                .collect(Collectors.toSet());

        Person sampleOwner = new Person(new Name("Owner Test"),
                new Phone("12345678"),
                new Email("owner@example.com"),
                new Address("A"),
                tags
        );
        Property sampleProperty = new Property("123456", "", "11-11111");
        sampleProperty.getOwners().add(sampleOwner);
        sampleProperty.setName("Test 1");
        Property sampleProperty2 = new Property("123456", "12A", "");
        sampleProperty2.getOwners().add(sampleOwner);
        sampleProperty.setName("Test 2");

        testData.add(sampleProperty);
        testData.add(sampleProperty2);
        return FXCollections.observableList(testData);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Property} using a {@code PropertyCard}.
     */
    class PropertyListViewCell extends ListCell<Property> {
        @Override
        protected void updateItem(Property property, boolean empty) {
            super.updateItem(property, empty);

            if (empty || property == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PropertyCard(property, getIndex() + 1).getRoot());
            }
        }
    }

}
