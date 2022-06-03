package controllers;


import controllers.restaurantview.MainMenuController;
import javafx.event.ActionEvent;
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

    protected Stage getStage(ActionEvent actionEvent) {
        return (Stage)((Node) actionEvent.getSource()).getScene().getWindow();}

    protected void setStage(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    protected void goToMainMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = setRoot(Resources.MAINMENU);
        Stage stage = getStage(actionEvent);
        stage.setOnCloseRequest(event -> stage.close());
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.setClientSocket(getClientSocket());
        mainMenuController.setControllerStage(getStage(actionEvent));
        mainMenuController.setMainMenuController();
        setStage(actionEvent);
    }

    protected void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(message);
        alert.setContentText("");
        alert.showAndWait();
    }
}
