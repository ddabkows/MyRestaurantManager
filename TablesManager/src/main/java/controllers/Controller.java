package controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import client.ClientSocketBuilder;
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

    protected Stage getStage(ActionEvent actionEvent) {
        return (Stage)((Node) actionEvent.getSource()).getScene().getWindow();}

    protected void setStage(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
