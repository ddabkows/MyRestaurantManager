package client.tableviewsenders;

import client.Sender;
import org.json.JSONObject;
import packettypes.TableValuesColumns;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class PrintSender extends Sender {
    String toPrint;

    public PrintSender(Socket socketToSet, String toPrintToSet) {
        super(socketToSet);
        toPrint = toPrintToSet;
    }

    public boolean sendPacket(String tableName, String printer) throws IOException {
        PrintWriter writer = getSocketWriter();

        writer.println(preparePacket(tableName, printer));

        return getAnswer(getSocketAnswer());
    }

    public boolean getAnswer(String answer) {
        JSONObject answerPacket = new JSONObject(answer);
        return answerPacket.getBoolean(TableValuesColumns.CONFIRMED.toString());
    }

    public String preparePacket(String tableName, String printer) {
        JSONObject packet = getTitledJSONObject(TableValuesColumns.TYPE.toString());
        packet.put(TableValuesColumns.TABLENAME.toString(), tableName);
        packet.put(TableValuesColumns.REQUEST.toString(), TableValuesColumns.PRINT.toString());
        packet.put(TableValuesColumns.TOPRINT.toString(), toPrint);
        packet.put(TableValuesColumns.PRINTER.toString(), printer);
        return packet.toString();
    }
}
