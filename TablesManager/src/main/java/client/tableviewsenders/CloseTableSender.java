package client.tableviewsenders;

import client.Sender;
import org.json.JSONObject;
import packettypes.MainColumn;
import packettypes.TableValuesColumns;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class CloseTableSender extends Sender {
    public CloseTableSender(Socket socketToSet) {
        super(socketToSet);
    }

    public void sendPacket(String tableName) throws IOException {
        PrintWriter writer = getSocketWriter();

        writer.println(preparePacket(tableName));

        getSocketAnswer();
    }

    public String preparePacket(String tableName) {
        JSONObject packet = new JSONObject();
        packet.put(new MainColumn().getMainColumn(), TableValuesColumns.TYPE.toString());
        packet.put(TableValuesColumns.TABLENAME.toString(), tableName);
        packet.put(TableValuesColumns.REQUEST.toString(), TableValuesColumns.CLOSE.toString());
        return packet.toString();
    }
}
