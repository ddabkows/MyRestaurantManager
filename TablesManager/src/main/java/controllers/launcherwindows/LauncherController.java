package controllers.launcherwindows;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import client.launchersenders.TokenSender;
import controllers.Controller;
import javafx.scene.layout.Pane;


public class LauncherController extends Controller {

    @FXML
    public TextField tokenTextField;

    public void getToRestaurant(ActionEvent actionEvent) throws IOException {
        Pane mainPane = (Pane)getStage(actionEvent).getScene().getRoot();
        ProgressIndicator progressIndicator = setProgressIndicator(mainPane);
        TokenSender tokenSender = new TokenSender(getClientSocket().getSocket());
        if (tokenSender.sendPacket(tokenTextField.getText())) {
            goToMainMenu(actionEvent);
        } else {
            System.out.println("Rejected");
            mainPane.getChildren().remove(progressIndicator);
        }
    }

    public void enter(ActionEvent actionEvent) throws IOException {
        getToRestaurant(actionEvent);
    }
}
