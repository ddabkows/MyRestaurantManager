package thread;

import org.junit.platform.commons.logging.LoggerFactory;

import java.io.*;
import java.net.*;
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

            String txt;

            do {
                txt = reader.readLine();
                String finalTxt = txt;
                Supplier<String> txtSupplier = ()-> finalTxt;
                LoggerFactory.getLogger(ServerThread.class).info(txtSupplier);
                String answer = "Hello.";
                writer.println(answer);
            } while (!txt.equals("exit"));
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
