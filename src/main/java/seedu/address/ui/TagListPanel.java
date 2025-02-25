package seedu.address.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.Tag;

/**
 * Panel containing the list of properties.
 */
public class TagListPanel extends UiPart<Region> {
    private static final String FXML = "TagListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TagListPanel.class);

    @FXML
    private ListView<Tag> tagListView;

    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code TagListPanel} with the given {@code ObservableList}.
     */
    public TagListPanel(ObservableList<Tag> tagList) {
        super(FXML);
        for (Tag t : tagList) {
            tags.getChildren().add(new Label(t.tagName));
        }
//        tagListView.setItems(tagList);
//        tagListView.setCellFactory(listView -> new TagListViewCell());
    }


    public static ObservableList<Tag> getSampleTagData(){
        ArrayList<Tag> testData = new ArrayList<>();
        Tag sampleTag = new Tag("123456");
        testData.add(sampleTag);
        sampleTag = new Tag( "12A");
        testData.add(sampleTag);
        return FXCollections.observableList(testData);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Tag} using a {@code TagCard}.
     */
    class TagListViewCell extends ListCell<Tag> {
        @Override
        protected void updateItem(Tag tag, boolean empty) {
            super.updateItem(tag, empty);

            if (empty || tag == null) {
                setGraphic(null);
                setText(null);
            } else {

                setGraphic(new Label(tag.tagName));
            }
        }
    }

}
