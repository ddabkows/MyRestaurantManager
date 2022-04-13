package controllers.launcherwindows;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import client.TokenSender;
import controllers.Controller;
import resources.Resources;


public class LauncherController extends Controller {

    @FXML
    public TextField tokenTextField;

    public void getToRestaurant(ActionEvent actionEvent) throws IOException {
        TokenSender tokenSender = new TokenSender(super.getClientSocket().getSocket());
        if (tokenSender.sendPacket(tokenTextField.getText())) {
            goToMainMenu(actionEvent);
        } else {
            System.out.println("Rejected");
        }
    }

    private void goToMainMenu(ActionEvent actionEvent) throws IOException {
        setRoot(Resources.MAINMENU);
        setStage(actionEvent);
    }

    public void enter(ActionEvent actionEvent) throws IOException {
        getToRestaurant(actionEvent);
    }
}
