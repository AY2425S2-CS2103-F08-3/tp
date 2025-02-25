package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class OwnerListPanel extends UiPart<Region> {
    private static final String FXML = "OwnerListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(OwnerListPanel.class);

    @FXML
    private ListView<Person> ownerListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public OwnerListPanel(ObservableList<Person> personList) {
        super(FXML);
        ownerListView.setItems(personList);
        ownerListView.setCellFactory(listView -> new OwnerListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class OwnerListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new OwnerCard(person, getIndex() + 1).getRoot());
            }
        }
    }

}
