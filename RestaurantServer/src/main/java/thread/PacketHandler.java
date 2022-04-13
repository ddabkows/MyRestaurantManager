package thread;

import dao.AdminTokenDAO;
import dao.TokenDAO;
import org.json.JSONObject;

import packettypes.*;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class PacketHandler {
    public String getPacketResponse(String packet) throws NoSuchAlgorithmException {
        JSONObject receivedPacket = new JSONObject(packet);
        String packetType = receivedPacket.getString(new  MainColumn().getMainColumn());
        if (Objects.equals(packetType, TokenSenderColumns.TYPE.toString())) {
            return getTokenAnswer(receivedPacket.getString(TokenSenderColumns.TOKENCOL.toString()));}
        return "";
    }

    public String getTokenAnswer(String token) throws NoSuchAlgorithmException {
        JSONObject answerPacket = new JSONObject();
        answerPacket.put(new MainColumn().getMainColumn(), TokenSenderColumns.ANSWERTYPE.toString());
        if (new AdminTokenDAO().checkAdminToken(token)) {
            answerPacket.put(TokenSenderColumns.ANSWERCOL.toString(), true);
            answerPacket.put(TokenSenderColumns.USERTYPECOL.toString(), TokenSenderColumns.ADMIN.toString());
            return answerPacket.toString();
        }
        if (new TokenDAO().checkToken(token)) {
            answerPacket.put(TokenSenderColumns.ANSWERCOL.toString(), true);
            answerPacket.put(TokenSenderColumns.USERTYPECOL.toString(), TokenSenderColumns.USER.toString());
            return answerPacket.toString();
        }
        answerPacket.put(TokenSenderColumns.ANSWERCOL.toString(), false);
        return answerPacket.toString();
    }
}
