package thread;


import org.json.JSONObject;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import dao.AdminTokenDAO;
import dao.TokenDAO;
import packettypes.*;
import restaurant.Restaurant;

public class PacketHandler {
    private final Restaurant restaurant;

    public PacketHandler(Restaurant restaurantToSet) {
        this.restaurant = restaurantToSet;
    }

    public String getPacketResponse(String packet) throws NoSuchAlgorithmException {
        JSONObject receivedPacket = new JSONObject(packet);
        String packetType = receivedPacket.getString(new  MainColumn().getMainColumn());
        if (Objects.equals(packetType, TokenSenderColumns.TYPE.toString())) {
            return getTokenAnswer(receivedPacket.getString(TokenSenderColumns.TOKENCOL.toString()));}
        if (Objects.equals(packetType, AllTablesColumns.TYPE.toString())) {
            return getAllTablesAnswer(receivedPacket);
        }
        return "";
    }

    public String getAllTablesAnswer(JSONObject packet) {
        JSONObject answerPacket = new JSONObject();
        answerPacket.put(new MainColumn().getMainColumn(), AllTablesColumns.ANSWERTYPE.toString());
        String request = packet.getString(AllTablesColumns.REQUEST.toString());
        if (Objects.equals(request, AllTablesColumns.FETCH.toString())) {
            answerPacket.put(AllTablesColumns.STATUS.toString(), restaurant.getAllTablesStatus());
        }

        return answerPacket.toString();
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
