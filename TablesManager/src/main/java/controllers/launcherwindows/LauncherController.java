package controllers.launcherwindows;


import java.io.IOException;

import controllers.restaurantview.MainMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;

import client.TokenSender;
import controllers.Controller;
import resources.Resources;


public class LauncherController extends Controller {

    @FXML
    public TextField tokenTextField;

    public void getToRestaurant(ActionEvent actionEvent) throws IOException {
        TokenSender tokenSender = new TokenSender(getClientSocket().getSocket());
        if (tokenSender.sendPacket(tokenTextField.getText())) {
            goToMainMenu(actionEvent);
        } else {
            System.out.println("Rejected");
        }
    }

    private void goToMainMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = setRoot(Resources.MAINMENU);
        MainMenuController mainMenuController = loader.getController();
        mainMenuController.setClientSocket(getClientSocket());
        mainMenuController.setMainMenuController();
        setStage(actionEvent);
    }

    public void enter(ActionEvent actionEvent) throws IOException {
        getToRestaurant(actionEvent);
    }
}
