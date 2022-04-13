package client;

import org.json.JSONObject;
import org.junit.platform.commons.logging.LoggerFactory;
import java.net.*;
import java.io.*;
import java.util.Objects;
import java.util.function.Supplier;

import packettypes.*;

public class TokenSender {
    Socket socket;
    public TokenSender(Socket socketToSet) {
        this.socket = socketToSet;
    }

    public boolean sendPacket(String token) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream, true);

        writer.println(preparePacket(token));

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader((new InputStreamReader(inputStream)));

        String answer = reader.readLine();

        Supplier<String> answerSupplier = ()->answer;
        LoggerFactory.getLogger(TokenSender.class).info(answerSupplier);
        return getAnswer(answer);
    }

    public boolean getAnswer(String answer) {
        JSONObject answerPacket = new JSONObject(answer);
        if (!Objects.equals(answerPacket.getString(new MainColumn().getMainColumn()), TokenSenderColumns.ANSWERTYPE.toString())) {
            return false;
        }
        return answerPacket.getBoolean(TokenSenderColumns.ANSWERCOL.toString());
    }

    public String preparePacket(String token) {
        JSONObject packet = new JSONObject();
        packet.put(new MainColumn().getMainColumn(), TokenSenderColumns.TYPE.toString());
        packet.put(TokenSenderColumns.TOKENCOL.toString(), token);
        return packet.toString();
    }
}
