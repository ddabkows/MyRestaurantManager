package controllers.launcherwindows;


import java.io.IOException;

import client.TokenSender;
import controllers.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class LauncherController extends Controller {

    @FXML
    public TextField tokenTextField;

    public void getToRestaurant() throws IOException {
        TokenSender tokenSender = new TokenSender(super.getClientSocket().getSocket());
        if (tokenSender.sendPacket(tokenTextField.getText())) {
            System.out.println("Authorized");
        } else {
            System.out.println("Rejected");
        }
    }

    public void enter() throws IOException {
        getToRestaurant();
    }
}
