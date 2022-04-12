package controllers.launcherwindows;


import java.io.IOException;

import client.TokenSender;
import controllers.Controller;


public class LauncherController extends Controller {

    public void getToRestaurant() throws IOException {
        TokenSender tokenSender = new TokenSender(super.getClientSocket().getSocket());
        tokenSender.sendPacket("123");
    }
}
