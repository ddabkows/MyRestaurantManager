package client;

import org.json.JSONObject;
import packettypes.MainColumn;
import packettypes.OpenTableColumns;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class OpenTableSender extends Sender {
    public OpenTableSender(Socket socket) {super(socket);}

    public  boolean sendPacket(String table, int peopleCount) throws IOException {
        PrintWriter writer = getSocketWriter();

        writer.println(preparePacket(table, peopleCount));

        return getAnswer(getSocketAnswer());
    }

    public boolean getAnswer(String answer) {
        JSONObject answerPacket = new JSONObject(answer);
        if (!Objects.equals(answerPacket.getString(new MainColumn().getMainColumn()), OpenTableColumns.ANSWERTYPE.toString())) {
            return false;
        }
        return answerPacket.getBoolean(OpenTableColumns.OPENED.toString());
    }

    public String preparePacket(String table, int peopleCount) {
        JSONObject packet = new JSONObject();
        packet.put(new MainColumn().getMainColumn(), OpenTableColumns.TYPE.toString());
        packet.put(OpenTableColumns.TABLE.toString(), table);
        packet.put(OpenTableColumns.COUNT.toString(), peopleCount);
        return packet.toString();
    }
}
