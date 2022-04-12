package thread;

import dao.AdminTokenDAO;
import org.json.JSONObject;

import packettypes.*;

import java.util.Objects;

public class PacketHandler {
    public String getPacketResponse(String packet) {
        JSONObject receivedPacket = new JSONObject(packet);
        String packetType = receivedPacket.getString(new MainColumn().getMainColumn());
        if (Objects.equals(packetType, TokenSenderColumns.TYPE.toString())) {
            return getTokenAnswer(receivedPacket.getString(TokenSenderColumns.TOKENCOL.toString()));}
        return "";
    }

    public String getTokenAnswer(String token) {
        JSONObject answerPacket = new JSONObject();
        answerPacket.put(new MainColumn().getMainColumn(), TokenSenderColumns.ANSWERTYPE.toString());
        answerPacket.put(TokenSenderColumns.ANSWERCOL.toString(), new AdminTokenDAO().checkAdminToken(token));
        return answerPacket.toString();
    }
}
