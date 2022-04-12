package main;

import org.junit.platform.commons.logging.LoggerFactory;
import thread.ServerThread;

import java.io.IOException;
import java.net.*;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        int port = 5596;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Supplier<String> portInfo = ()-> "Listening on port " + port + "...";
            LoggerFactory.getLogger(Main.class).info(portInfo);
            while (true) {
                Socket socket = serverSocket.accept();

                Supplier<String> newClientInfo = ()-> "New client connected : " + socket.getRemoteSocketAddress();
                LoggerFactory.getLogger(Main.class).info(newClientInfo);

                new ServerThread(socket).start();
            }
        } catch (IOException throwables) {
            Supplier<String> stringSupplier = throwables::getMessage;
            LoggerFactory.getLogger(Main.class).error(stringSupplier);
        }
    }
}
