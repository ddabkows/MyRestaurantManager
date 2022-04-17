package client;

import org.junit.platform.commons.logging.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.function.Supplier;

public class Sender {
    private Socket socket;
    public Sender(Socket socketToSet) {this.socket = socketToSet;}

    protected Socket getSocket() {return this.socket;}

    protected PrintWriter getSocketWriter() throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        return new PrintWriter(outputStream, true);
    }

    protected String getSocketAnswer() throws IOException {
        InputStream inputStream = getSocket().getInputStream();
        BufferedReader reader = new BufferedReader((new InputStreamReader(inputStream)));

        String answer = reader.readLine();

        Supplier<String> answerSupplier = ()->answer;
        LoggerFactory.getLogger(TokenSender.class).info(answerSupplier);

        return answer;
    }
}
