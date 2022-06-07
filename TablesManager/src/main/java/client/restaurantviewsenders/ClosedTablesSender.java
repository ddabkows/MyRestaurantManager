package client.restaurantviewsenders;

import client.Sender;
import org.json.JSONObject;
import packettypes.ClosedTablesColumns;
import packettypes.MainColumn;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClosedTablesSender extends Sender {
    public ClosedTablesSender(Socket socket) {super(socket);}

    public JSONObject sendPacket() throws IOException {
        PrintWriter writer = getSocketWriter();

        writer.println(preparePacket());

        return getAnswer(getSocketAnswer());
    }

    public JSONObject getAnswer(String answer) {
        return new JSONObject(answer);
    }

    public String preparePacket() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String now = dtf.format(LocalDateTime.now());
        JSONObject packet = new JSONObject();
        packet.put(new MainColumn().getMainColumn(), ClosedTablesColumns.TYPE.toString());
        packet.put(ClosedTablesColumns.REQUEST.toString(), ClosedTablesColumns.ALLTABLES.toString());
        packet.put(ClosedTablesColumns.DAY.toString(), now);
        return packet.toString();
    }
}
