package client;

import org.json.JSONObject;
import org.junit.platform.commons.logging.LoggerFactory;
import java.net.*;
import java.io.*;
import java.util.Objects;
import java.util.function.Supplier;

import packettypes.*;

public class TokenSender extends Sender {
    public TokenSender(Socket socket) {
        super(socket);
    }

    public boolean sendPacket(String token) throws IOException {
        PrintWriter writer = getSocketWriter();

        writer.println(preparePacket(token));

        return getAnswer(getSocketAnswer());
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
