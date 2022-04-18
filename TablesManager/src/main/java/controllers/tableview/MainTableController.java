package controllers.tableview;

import controllers.Controller;
import databaseparams.Categories;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public class MainTableController extends Controller {
    @FXML
    private ListView<String> categoriesListView;
    @FXML
    private TextField peopleCountTextField;
    @FXML
    private Label tableNameLabel;
    private String oldPeopleCount;

    public void setTableWindow(String tableName) {
        setTableNameLabel(tableName);
        oldPeopleCount = peopleCountTextField.getText();
        setCategoriesListView();
    }

    private void setCategoriesListView() {
        categoriesListView.getItems().clear();
        categoriesListView.setStyle("""
                -fx-background-color : #f8b195;
                -fx-text-fill : #f8b195;
                -fx-font-size : 21;
                -fx-font-family : Lato;
                -fx-font-weight : bold;
                fx-font-style : italic;
                -fx-control-inner-background : #f8b195;
                -fx-selection-bar : #c06c84;
                """);
        categoriesListView.setOnMouseClicked(mouseEvent -> categoriesListView.getSelectionModel().getSelectedItem());
        for (Categories category : Categories.values()) {
            categoriesListView.getItems().add(category.toString());
        }
    }

    private void setTableNameLabel(String tableName) {
        tableNameLabel.setText(tableName);
    }

    public void setPeopleCount() {
        if (!Objects.equals(peopleCountTextField.getText(), "")) {
             try {
                 Integer.parseInt(peopleCountTextField.getText());
                 oldPeopleCount = peopleCountTextField.getText();
             } catch (Exception ignored) {
                 if (peopleCountTextField.getText().length() > 1) {
                     peopleCountTextField.setText(oldPeopleCount);
                 } else {
                     peopleCountTextField.setText("");
                 }
             }
             if (Objects.equals(peopleCountTextField.getText(), "")) {
                 peopleCountTextField.setText("0");
             }
             peopleCountTextField.positionCaret(peopleCountTextField.getText().length());
        }
    }

    public void positionCaret() {
        peopleCountTextField.positionCaret(peopleCountTextField.getText().length());
    }
}
