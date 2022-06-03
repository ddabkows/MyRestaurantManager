package client.exit;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class AlertExitSender {
    Socket socket;

    public AlertExitSender(Socket socketToSet) {
        socket = socketToSet;
    }

    public void sendPacket(String tableName) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream, true);

        writer.println(tableName);
    }
}
