package controllers;


import controllers.restaurantview.MainMenuController;
import controllers.restaurantview.SettingsController;
import controllers.tableview.ClosedTablesController;
import controllers.tableview.MainTableController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

import client.socketbuilder.ClientSocketBuilder;
import resources.Resources;




public class Controller {
    private Parent root;
    private ClientSocketBuilder clientSocket;
    private String printerPath;

    public void setClientSocket(ClientSocketBuilder clientSocketToSet) {this.clientSocket = clientSocketToSet;}

    protected ClientSocketBuilder getClientSocket() {return this.clientSocket;}

    protected FXMLLoader setRoot(Resources resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resource.toString()));
        root = loader.load();
        return loader;
    }

    protected ProgressIndicator setProgressIndicator(Pane mainPane) {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefWidth(mainPane.getWidth()/30.0);
        progressIndicator.setPrefHeight(mainPane.getHeight()/30.0);
        mainPane.getChildren().add(progressIndicator);
        return  progressIndicator;
    }

    protected Parent getRoot() {return this.root;}

    protected void allowCloseRequest(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        stage.setOnCloseRequest(event -> stage.close());
    }

    protected Stage getStage(ActionEvent actionEvent) {
        return (Stage)((Node) actionEvent.getSource()).getScene().getWindow();}

    protected void setStage(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    protected void goToMainMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = setRoot(Resources.MAINMENU);
        allowCloseRequest(actionEvent);
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.setClientSocket(getClientSocket());
        mainMenuController.setControllerStage(getStage(actionEvent));
        mainMenuController.setMainMenuController();
        mainMenuController.setPrinterPath(printerPath);
        setStage(actionEvent);
    }

    protected void goToClosedTables(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = setRoot(Resources.CLOSEDTABLES);
        allowCloseRequest(actionEvent);
        ClosedTablesController closedTablesController = loader.getController();
        closedTablesController.setClientSocket(getClientSocket());
        closedTablesController.setPrinterPath(printerPath);
        closedTablesController.setController();
        setStage(actionEvent);
    }

    protected void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(message);
        alert.setContentText("");
        alert.showAndWait();
    }

    public void setPrinterPath(String printerPathToSet) {printerPath = printerPathToSet;}

    public String getPrinterPath() {return printerPath;}

    public void goToSettings(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = setRoot(Resources.SETTINGS);
        allowCloseRequest(actionEvent);
        SettingsController settingsController = loader.getController();
        settingsController.setClientSocket(getClientSocket());
        settingsController.setPrinterPath(printerPath);
        settingsController.setPrintersComboBox();
        setStage(actionEvent);
    }

    public void getToTable(ActionEvent actionEvent, String tableName) throws IOException {
        FXMLLoader loader = setRoot(Resources.TABLEMENU);
        MainTableController mainTableController = loader.getController();
        Stage stage = getStage(actionEvent);
        stage.setOnCloseRequest(Event::consume);
        mainTableController.setClientSocket(getClientSocket());
        mainTableController.setTableWindow(tableName);
        mainTableController.setPrinterPath(printerPath);
        setStage(actionEvent);
    }
}
