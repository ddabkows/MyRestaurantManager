package client;


import org.junit.platform.commons.logging.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.function.Supplier;


public class ClientSocketBuilder {
    String hostname = "localhost";
    int port = 5596;
    Socket socket;

    public ClientSocketBuilder() {
        try {
            socket = new Socket(hostname, port);
        } catch (IOException throwables) {
            Supplier<String> error = throwables::getMessage;
            LoggerFactory.getLogger(ClientSocketBuilder.class).error(error);
            System.exit(1);
        }
    }

    public Socket getSocket() {return this.socket;}
}
