package client.tableviewsenders;

import client.Sender;
import javafx.scene.control.Tab;
import org.json.JSONObject;
import packettypes.MainColumn;
import packettypes.TableValuesColumns;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PrintStartersSender extends Sender {

    public PrintStartersSender(Socket socketToSet) {super(socketToSet);}

    public boolean sendPacket(String tableName) throws IOException {
        PrintWriter writer = getSocketWriter();

        writer.println(preparePacket(tableName));

        return getAnswer(getSocketAnswer());
    }

    public boolean getAnswer(String answer) {
        JSONObject answerPacket = new JSONObject(answer);
        return answerPacket.getBoolean(TableValuesColumns.CONFIRMED.toString());
    }

    public String preparePacket(String tableName) {
        JSONObject packet = getTitledJSONObject(TableValuesColumns.TYPE.toString());
        packet.put(TableValuesColumns.TABLENAME.toString(), tableName);
        packet.put(TableValuesColumns.REQUEST.toString(), TableValuesColumns.PRINT.toString());
        packet.put(TableValuesColumns.TOPRINT.toString(), TableValuesColumns.STARTERS.toString());
        return packet.toString();
    }
}
