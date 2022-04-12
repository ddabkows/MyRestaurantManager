package thread;

import org.junit.platform.commons.logging.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.function.Supplier;

public class ServerThread extends Thread {
    private final Socket socket;

    public ServerThread(Socket socketToSet) {
        this.socket = socketToSet;
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);

            String receivedPacket;

            do {
                receivedPacket = reader.readLine();
                String finalTxt = receivedPacket;
                if (Objects.equals(finalTxt, "exit")) {continue;}
                Supplier<String> txtSupplier = ()-> finalTxt;
                LoggerFactory.getLogger(ServerThread.class).info(txtSupplier);
                String answer = new PacketHandler().getPacketResponse(receivedPacket);
                writer.println(answer);
            } while (!receivedPacket.equals("exit"));
            Supplier<String> exitSupplier = ()-> "Client " + socket.getRemoteSocketAddress() + " disconnected...";
            LoggerFactory.getLogger(ServerThread.class).info(exitSupplier);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
