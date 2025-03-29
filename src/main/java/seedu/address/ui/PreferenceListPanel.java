package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.PropertyPreference;

/**
 * Panel containing the list of preferences.
 */
public class PreferenceListPanel extends UiPart<Region> {
    private static final String FXML = "PreferenceListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PreferenceListPanel.class);

    @FXML
    private ListView<PropertyPreference> preferenceListView;

    /**
     * Creates a {@code PreferenceListPanel} with the given {@code ObservableList}.
     */
    public PreferenceListPanel(ObservableList<PropertyPreference> preferenceList) {
        super(FXML);
        preferenceListView.setItems(preferenceList);
        preferenceListView.setCellFactory(listView -> new PreferenceListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Preference} using a {@code PreferenceCard}.
     */
    class PreferenceListViewCell extends ListCell<PropertyPreference> {
        @Override
        protected void updateItem(PropertyPreference preference, boolean empty) {
            super.updateItem(preference, empty);

            if (empty || preference == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PreferenceCard(preference, getIndex() + 1).getRoot());
            }
        }
    }
}
