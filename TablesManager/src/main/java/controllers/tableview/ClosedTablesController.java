package controllers.tableview;

import client.restaurantviewsenders.ClosedTablesSender;
import controllers.Controller;
import controllers.resources.Images;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.json.JSONArray;
import org.json.JSONObject;
import packetcomponents.ClosedTable;
import packettypes.ClosedTablesColumns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClosedTablesController extends Controller {
    @FXML
    private ListView<HBox> closedTablesListView;
    @FXML
    private AnchorPane mainPane;
    Image openImage;

    public void back(ActionEvent actionEvent) throws IOException {
        goToMainMenu(actionEvent);
    }

    public HBox generateHBox(String tableName, String tableDate) {
        int tableNameWidth = 100;
        int dateWidth = 500;
        int height = 30;
        Label tableNameLabel = new Label("Table: " + tableName);
        Label tableDateLabel = new Label(tableDate);
        tableDateLabel.setFont(new Font(16.0));
        tableNameLabel.setPrefSize(tableNameWidth, height);
        tableDateLabel.setPrefSize(dateWidth, height);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(tableNameLabel, tableDateLabel);

        Button openButton = new Button();
        ImageView openImgView = new ImageView(openImage);
        openImgView.setFitHeight(60); openImgView.setFitWidth(60);
        openButton.setGraphic(openImgView);
        openButton.setPrefSize(30, 30);
        openButton.setStyle("-fx-background-color: transparent");
        HBox hBox = new HBox();
        hBox.getChildren().addAll(vBox, openButton);
        return hBox;
    }

    public void loadClosedTables() throws IOException {
        ProgressIndicator progressIndicator = setProgressIndicator(mainPane);
        JSONObject answer = new ClosedTablesSender(getClientSocket().getSocket()).sendPacket();
        mainPane.getChildren().remove(progressIndicator);
        List<ClosedTable> closedTables = new ArrayList<>();
        JSONArray closedTablesJSONObjs = answer.getJSONArray(ClosedTablesColumns.ANSWER.toString());
        for (int i=0; i < closedTablesJSONObjs.length(); ++i) {
            JSONObject closedTableJSONObj = closedTablesJSONObjs.getJSONObject(i);
            closedTables.add(new ClosedTable(closedTableJSONObj.getString(ClosedTablesColumns.TABLE.toString()), closedTableJSONObj.getString(ClosedTablesColumns.DAY.toString())));
        }
        for (ClosedTable closedTable : closedTables) {
            HBox tableHBox = generateHBox(closedTable.getTableName(), closedTable.getClosedAt());
            closedTablesListView.getItems().add(tableHBox);
        }
    }

    public void setController() throws IOException {
        openImage = new Image(Images.OPEN.toString());
        loadClosedTables();
    }
}
