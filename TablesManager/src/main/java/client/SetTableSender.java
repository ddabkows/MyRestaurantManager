package client;

import controllers.tableview.AddedProductHBox;
import org.json.JSONObject;
import packettypes.MainColumn;
import packettypes.TableValuesColumns;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class SetTableSender extends Sender {

    public SetTableSender(Socket socketToSet) {super(socketToSet);}

    public boolean sendPacket(String table, int peopleCount, List<AddedProductHBox> allProducts) throws IOException {
        PrintWriter writer = getSocketWriter();

        writer.println(preparePacket(table, peopleCount, allProducts));

        return getAnswer(getSocketAnswer());
    }

    public boolean getAnswer(String answer) {
        JSONObject answerPacket = new JSONObject(answer);
        if (!Objects.equals(answerPacket.getString(new MainColumn().getMainColumn()), TableValuesColumns.ANSWERTYPE.toString())) {
            return false;
        }
        return answerPacket.getBoolean(TableValuesColumns.CONFIRMED.toString());
    }

    public String preparePacket(String table, int peopleCount, List<AddedProductHBox> allProducts) {
        JSONObject packet = new JSONObject();
        packet.put(new MainColumn().getMainColumn(), TableValuesColumns.TYPE.toString());
        packet.put(TableValuesColumns.REQUEST.toString(), TableValuesColumns.SET.toString());
        packet.put(TableValuesColumns.TABLENAME.toString(), table);
        packet.put(TableValuesColumns.PEOPLECOUNT.toString(), peopleCount);
        JSONObject productsJSONObj = new JSONObject();
        for (AddedProductHBox addedProductHBox : allProducts) {
            if (addedProductHBox.getQuantity() > 0) {
                JSONObject productSpecifics = new JSONObject();
                productSpecifics.put(TableValuesColumns.PRODUCTQUANTITY.toString(), addedProductHBox.getQuantity());
                productSpecifics.put(TableValuesColumns.PRODUCTTYPE.toString(), addedProductHBox.getType());
                productSpecifics.put(TableValuesColumns.PRODUCTPRICE.toString(), addedProductHBox.getPrice());
                productSpecifics.put(TableValuesColumns.PRODUCTCOMMENT.toString(), addedProductHBox.getComment());
                productsJSONObj.put(addedProductHBox.getProduct() + addedProductHBox.getType(), productSpecifics);
            }
        }
        packet.put(TableValuesColumns.PRODUCTS.toString(), productsJSONObj);
        return packet.toString();
    }
}
