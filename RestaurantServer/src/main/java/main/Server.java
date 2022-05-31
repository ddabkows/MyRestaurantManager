package main;

import org.junit.platform.commons.logging.LoggerFactory;
import restaurant.Restaurant;
import thread.ServerThread;

import java.io.IOException;
import java.net.*;
import java.util.Set;
import java.util.function.Supplier;

public class Server {
    public static void main(String[] args) {
        int port = 5596;
        Restaurant restaurant = new Restaurant();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Supplier<String> portInfo = ()-> "Listening on port " + port + "...";
            LoggerFactory.getLogger(Server.class).info(portInfo);
            while (true) {
                Socket socket = serverSocket.accept();

                Supplier<String> newClientInfo = ()-> "New client connected : " + socket.getRemoteSocketAddress();
                LoggerFactory.getLogger(Server.class).info(newClientInfo);

                new ServerThread(socket, restaurant).start();
            }
        } catch (IOException throwables) {
            Supplier<String> stringSupplier = throwables::getMessage;
            LoggerFactory.getLogger(Server.class).error(stringSupplier);
        }
    }
}
