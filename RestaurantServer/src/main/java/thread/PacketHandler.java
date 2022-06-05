package thread;

import org.json.JSONArray;
import org.json.JSONObject;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import dao.AdminTokenDAO;
import dao.TokenDAO;
import packettypes.*;
import restaurant.Product;
import restaurant.Restaurant;
import restaurant.Table;

public class PacketHandler {
    private final Restaurant restaurant;

    public PacketHandler(Restaurant restaurantToSet) {
        this.restaurant = restaurantToSet;
    }

    public String getPacketResponse(String packet) throws NoSuchAlgorithmException {
        try {
            JSONObject receivedPacket = new JSONObject(packet);
            String packetType = receivedPacket.getString(new  MainColumn().getMainColumn());
            if (Objects.equals(packetType, TokenSenderColumns.TYPE.toString())) {
                return getTokenAnswer(receivedPacket.getString(TokenSenderColumns.TOKENCOL.toString()));}
            if (Objects.equals(packetType, AllTablesColumns.TYPE.toString())) {
                return getAllTablesAnswer(receivedPacket);}
            if (Objects.equals(packetType, OpenTableColumns.TYPE.toString())) {
                return getOpenTableAnswer(receivedPacket);}
            if (Objects.equals(packetType, TableValuesColumns.TYPE.toString())) {
                return getTableRequest(receivedPacket);}

        } catch (Exception e) {
            e.printStackTrace();
            Table table = restaurant.getTable(packet);
            table.unbusyTable();
        }
        return "";
    }

    private String setTableValues(JSONObject packet) {
        JSONObject answerPacket = new JSONObject();
        answerPacket.put(new MainColumn().getMainColumn(), TableValuesColumns.ANSWERTYPE.toString());
        String tableName = packet.getString(TableValuesColumns.TABLENAME.toString());
        int peopleCount = packet.getInt(TableValuesColumns.PEOPLECOUNT.toString());
        JSONObject productsJSONObj = packet.getJSONObject(TableValuesColumns.PRODUCTS.toString());
        Table table = restaurant.getTable(tableName);
        answerPacket.put(TableValuesColumns.CONFIRMED.toString(), table.set(peopleCount, productsJSONObj));
        return  answerPacket.toString();
    }

    private void fillWithProducts(JSONArray productsJSONArr, List<Product> products) {
        for (Product product : products) {
            JSONObject productSpecifics = new JSONObject();
            productSpecifics.put(TableValuesColumns.PRODUCTNAME.toString(), product.getName());
            productSpecifics.put(TableValuesColumns.PRODUCTQUANTITY.toString(), product.getQuantity());
            productSpecifics.put(TableValuesColumns.PRODUCTTYPE.toString(), product.getType());
            productSpecifics.put(TableValuesColumns.PRODUCTPRICE.toString(), product.getPrice());
            productSpecifics.put(TableValuesColumns.PRODUCTCOMMENT.toString(), product.getComment());
            productsJSONArr.put(productSpecifics);
        }
    }



    private String getTableValues(JSONObject packet) {
        JSONObject answerPacket = new JSONObject();
        answerPacket.put(new MainColumn().getMainColumn(), TableValuesColumns.ANSWERTYPE.toString());
        JSONObject values = new JSONObject();
        Table table = restaurant.getTable(packet.getString(TableValuesColumns.TABLENAME.toString()));
        int peopleCount = table.getPeopleCount();
        values.put(TableValuesColumns.PEOPLECOUNT.toString(), peopleCount);
        JSONArray productsJSONArr = new JSONArray();
        fillWithProducts(productsJSONArr, table.getStarters());
        fillWithProducts(productsJSONArr, table.getDishes());
        fillWithProducts(productsJSONArr, table.getDesserts());
        fillWithProducts(productsJSONArr, table.getDrinks());
        values.put(TableValuesColumns.PRODUCTS.toString(), productsJSONArr);
        answerPacket.put(TableValuesColumns.VALUES.toString(), values);
        return answerPacket.toString();
    }

    private String closeTable(JSONObject packet) {
        String tableToClose = packet.getString(TableValuesColumns.TABLENAME.toString());
        boolean closed = restaurant.getTable(tableToClose).close(tableToClose, restaurant.getCurInvoice());
        if (closed) {
            restaurant.incCurInvoice();
            return String.format("Table %s closed.", tableToClose);
        } else {
            return String.format("Table %s not closed. Fetch status.", tableToClose);
        }
    }

    public String printStarters(JSONObject packet) {
        String table = packet.getString(TableValuesColumns.TABLENAME.toString());
        String printer = packet.getString(TableValuesColumns.PRINTER.toString());
        boolean printed = restaurant.getTable(table).printStarters(table, printer);
        JSONObject answerPacket = new JSONObject();
        answerPacket.put(new MainColumn().getMainColumn(), TableValuesColumns.ANSWERTYPE.toString());
        answerPacket.put(TableValuesColumns.CONFIRMED.toString(), printed);
        return answerPacket.toString();
    }

    private String getTableRequest(JSONObject packet) {
        String request = packet.getString(TableValuesColumns.REQUEST.toString());
        if (Objects.equals(request, TableValuesColumns.FETCH.toString())) {
            return getTableValues(packet);
        }
        if (Objects.equals(request, TableValuesColumns.CLOSE.toString())) {
            return closeTable(packet);
        }
        if (Objects.equals(request, TableValuesColumns.SET.toString())) {
            return setTableValues(packet);
        }
        if (Objects.equals(request, TableValuesColumns.PRINT.toString())) {
            if (Objects.equals(packet.getString(TableValuesColumns.TOPRINT.toString()), TableValuesColumns.STARTERS.toString())) {
                return printStarters(packet);
            }
        }
        return "";
    }

    private String getOpenTableAnswer(JSONObject packet) {
        JSONObject answerPacket = new JSONObject();
        answerPacket.put(new MainColumn().getMainColumn(), OpenTableColumns.ANSWERTYPE.toString());
        Table tableToOpen = restaurant.getTable(packet.getString(OpenTableColumns.TABLE.toString()));
        String message = "Tables status not up to date.\nFetching data.";
        answerPacket.put(OpenTableColumns.OPENED.toString(), tableToOpen.open(packet.getInt(OpenTableColumns.COUNT.toString())));
        if (tableToOpen.getTableBusy()) {
            message = "Unable to open.\nTable busy.";
        }
        answerPacket.put(OpenTableColumns.MESSAGE.toString(), message);
        return answerPacket.toString();
    }

    private String getAllTablesAnswer(JSONObject packet) {
        JSONObject answerPacket = new JSONObject();
        answerPacket.put(new MainColumn().getMainColumn(), AllTablesColumns.ANSWERTYPE.toString());
        String request = packet.getString(AllTablesColumns.REQUEST.toString());
        if (Objects.equals(request, AllTablesColumns.FETCH.toString())) {
            answerPacket.put(AllTablesColumns.STATUS.toString(), restaurant.getAllTablesStatus());
        }

        return answerPacket.toString();
    }

    private String getTokenAnswer(String token) throws NoSuchAlgorithmException {
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
