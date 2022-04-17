package controllers.restaurantview;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import client.AllTablesSender;
import controllers.Controller;

public class MainMenuController extends Controller {
    Map<String, Button> buttons = new HashMap<>();

    public void setMainMenuController() throws IOException {
        setButtons();
        fetchAllTablesStatus();
    }

    private void fetchAllTablesStatus() throws IOException {
        AllTablesSender allTablesSender = new AllTablesSender(getClientSocket().getSocket());
        JSONObject allTablesStatus = allTablesSender.sendPacket();
        for (String table : allTablesStatus.keySet()) {
            if (allTablesStatus.getBoolean(table)) {
                buttons.get(table).setStyle("-fx-background-color : #355c7d");
            }
            else {
                buttons.get(table).setStyle("-fx-background-color : #000000");
            }
        }
    }

    public void openTable(ActionEvent actionEvent) {
        System.out.println(((Button)actionEvent.getSource()).getText());
    }

    @FXML
    public Button A;
    @FXML
    public Button B;
    @FXML
    public Button C;
    @FXML
    public Button D;
    @FXML
    public Button E;
    @FXML
    public Button F;
    @FXML
    public Button G;
    @FXML
    public Button H;
    @FXML
    public Button I;
    @FXML
    public Button J;
    @FXML
    public Button K;
    @FXML
    public Button L;
    @FXML
    public Button M;
    @FXML
    public Button N;
    @FXML
    public Button O;
    @FXML
    public Button P;
    @FXML
    public Button Q;
    @FXML
    public Button R;
    @FXML
    public Button S;
    @FXML
    public Button T;
    @FXML
    public Button U;
    @FXML
    public Button V;
    @FXML
    public Button W;
    @FXML
    public Button X;
    @FXML
    public Button Y;
    @FXML
    public Button Z;
    @FXML
    public Button AA;

    private void setButtons() {
        buttons.put(A.getText(), A);
        buttons.put(B.getText(), B);
        buttons.put(C.getText(), C);
        buttons.put(D.getText(), D);
        buttons.put(E.getText(), E);
        buttons.put(F.getText(), F);
        buttons.put(G.getText(), G);
        buttons.put(H.getText(), H);
        buttons.put(I.getText(), I);
        buttons.put(J.getText(), J);
        buttons.put(K.getText(), K);
        buttons.put(L.getText(), L);
        buttons.put(M.getText(), M);
        buttons.put(N.getText(), N);
        buttons.put(O.getText(), O);
        buttons.put(P.getText(), P);
        buttons.put(Q.getText(), Q);
        buttons.put(R.getText(), R);
        buttons.put(S.getText(), S);
        buttons.put(T.getText(), T);
        buttons.put(U.getText(), U);
        buttons.put(V.getText(), V);
        buttons.put(W.getText(), W);
        buttons.put(X.getText(), X);
        buttons.put(Y.getText(), Y);
        buttons.put(Z.getText(), Z);
        buttons.put(AA.getText(), AA);
    }
}
