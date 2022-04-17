package client;


import org.json.JSONObject;
import java.io.*;
import java.net.Socket;

import packettypes.AllTablesColumns;
import packettypes.MainColumn;


public class AllTablesSender extends Sender {
    public AllTablesSender(Socket socket) {
        super(socket);
    }

    public JSONObject sendPacket() throws IOException {
        PrintWriter writer = getSocketWriter();

        writer.println(preparePacket());

        return getAnswer(getSocketAnswer());
    }

    public JSONObject getAnswer(String answer) {
        JSONObject answerPacket = new JSONObject(answer);
        return answerPacket.getJSONObject(AllTablesColumns.STATUS.toString());
    }

    public String preparePacket() {
        JSONObject packet = new JSONObject();
        packet.put(new MainColumn().getMainColumn(), AllTablesColumns.TYPE.toString());
        packet.put(AllTablesColumns.REQUEST.toString(), AllTablesColumns.FETCH.toString());
        return packet.toString();
    }
}
