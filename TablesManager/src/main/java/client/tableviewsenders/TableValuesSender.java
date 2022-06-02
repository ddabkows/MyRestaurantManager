package client.tableviewsenders;

import client.Sender;
import org.json.JSONObject;
import packettypes.MainColumn;
import packettypes.TableValuesColumns;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TableValuesSender extends Sender {

    public TableValuesSender(Socket socketToSet) {super(socketToSet);}

    public JSONObject sendPacket(String tableName) throws IOException {
        PrintWriter writer = getSocketWriter();

        writer.println(preparePacket(tableName));

        return getAnswer(getSocketAnswer());
    }

    public JSONObject getAnswer(String answer) {
        JSONObject answerPacket = new JSONObject(answer);
        return answerPacket.getJSONObject(TableValuesColumns.VALUES.toString());
    }

    public String preparePacket(String tableName) {
        JSONObject packet = new JSONObject();
        packet.put(new MainColumn().getMainColumn(), TableValuesColumns.TYPE.toString());
        packet.put(TableValuesColumns.TABLENAME.toString(), tableName);
        packet.put(TableValuesColumns.REQUEST.toString(), TableValuesColumns.FETCH.toString());
        return packet.toString();
    }
}
