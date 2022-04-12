package client;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;

public class ExitSender {
    Socket socket;
    public ExitSender(Socket socketToSet) {this.socket = socketToSet;}

    public void sendPacket() throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream, true);

        writer.println("exit");
    }
}
