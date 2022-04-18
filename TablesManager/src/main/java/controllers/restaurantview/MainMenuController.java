package controllers.restaurantview;


import client.OpenTableSender;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import client.AllTablesSender;
import controllers.Controller;
import org.junit.platform.commons.logging.LoggerFactory;
import resources.Resources;

public class MainMenuController extends Controller {
    String backGroundString = "-fx-background-color : ";
    String closedTableColour = "#355c7d";
    String openTableColour = "#000000";
    PeopleCountToTableController peopleCountToTableController;
    private Stage controllerStage;
    Map<String, Button> buttons = new HashMap<>();

    public void setMainMenuController() throws IOException {
        setButtons();
        fetchAllTablesStatus();
    }

    private ProgressIndicator setProgressIndicator(Pane mainPane) {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefWidth(mainPane.getWidth()/30.0);
        progressIndicator.setPrefHeight(mainPane.getHeight()/30.0);
        mainPane.getChildren().add(progressIndicator);
        return  progressIndicator;
    }

    public void setControllerStage(Stage controllerStageToSet) {this.controllerStage = controllerStageToSet;}

    private void fetchAllTablesStatus() throws IOException {
        Pane mainPane = (Pane)controllerStage.getScene().getRoot();
        ProgressIndicator progressIndicator = setProgressIndicator(mainPane);
        AllTablesSender allTablesSender = new AllTablesSender(getClientSocket().getSocket());
        JSONObject allTablesStatus = allTablesSender.sendPacket();
        mainPane.getChildren().remove(progressIndicator);
        for (String table : allTablesStatus.keySet()) {
            if (allTablesStatus.getBoolean(table)) {
                buttons.get(table).setStyle(backGroundString + openTableColour);
            }
            else {
                buttons.get(table).setStyle(backGroundString + closedTableColour);
            }
        }
    }

    public void openTable(ActionEvent actionEvent) throws IOException {
        String tableName = ((Button)actionEvent.getSource()).getText();
        String buttonStyle = ((Button)actionEvent.getSource()).getStyle().substring(backGroundString.length());
        if (buttonStyle.equals(openTableColour)) {
            Pane mainPane = (Pane)controllerStage.getScene().getRoot();
            ProgressIndicator progressIndicator = setProgressIndicator(mainPane);
            OpenTableSender openTableSender = new OpenTableSender(getClientSocket().getSocket());
            boolean openTableAnswer = openTableSender.sendPacket(tableName, 0);
            mainPane.getChildren().remove(progressIndicator);
        } else {
            FXMLLoader peopleCountLoader = new FXMLLoader(getClass().getClassLoader().getResource(Resources.OPENNEWTABLE.toString()));
            Pane peopleCountPane = peopleCountLoader.load();
            Pane mainPane = (Pane)getStage(actionEvent).getScene().getRoot();
            peopleCountToTableController = peopleCountLoader.getController();
            peopleCountToTableController.setCountInput(Integer.parseInt(peopleCountToTableController.peopleCountInputTextField.getText()));
            peopleCountToTableController.people2.setOnAction(actionEvent1 -> handleCustomCount(tableName, 2));
            peopleCountToTableController.people3.setOnAction(actionEvent1 -> handleCustomCount(tableName, 3));
            peopleCountToTableController.people4.setOnAction(actionEvent1 -> handleCustomCount(tableName, 4));
            peopleCountToTableController.people5.setOnAction(actionEvent1 -> handleCustomCount(tableName, 5));
            peopleCountToTableController.people6.setOnAction(actionEvent1 -> handleCustomCount(tableName, 6));
            peopleCountToTableController.peopleCountInputTextField.setOnKeyReleased(actionEvent1 -> {
                if (!Objects.equals(peopleCountToTableController.peopleCountInputTextField.getText(), "")) {
                    if (peopleCountToTableController.peopleCountInputTextField.getText().length() > 1) {
                        confirmTextFieldText();
                        peopleCountToTableController.peopleCountInputTextField.positionCaret(peopleCountToTableController.peopleCountInputTextField.getText().length());
                    } else {
                        if (!confirmTextFieldText()) {
                            peopleCountToTableController.peopleCountInputTextField.setText("");
                        } else {
                            peopleCountToTableController.peopleCountInputTextField.positionCaret(peopleCountToTableController.peopleCountInputTextField.getText().length());
                        }
                    }
                } else {
                    peopleCountToTableController.setCountInput(0);
                }
            });
            peopleCountToTableController.confirmButton.setOnAction(actionEvent1 -> {
                ProgressIndicator progressIndicator = setProgressIndicator(mainPane);
                OpenTableSender openTableSender = new OpenTableSender(getClientSocket().getSocket());
                try {
                    boolean openTableAnswer = openTableSender.sendPacket(tableName, peopleCountToTableController.getCountInput());
                } catch (IOException error) {
                    logError(error);
                }
                mainPane.getChildren().remove(progressIndicator);
            });
            peopleCountToTableController.cancelButton.setOnAction(actionEvent1 -> {
                for (Button tableButton : buttons.values()) {
                    tableButton.setDisable(false);
                }
                mainPane.getChildren().remove(peopleCountPane);
            });
            double panesDiffX = mainPane.getWidth() - peopleCountPane.getPrefWidth();
            double panesDiffY = mainPane.getHeight() - peopleCountPane.getPrefHeight();
            peopleCountPane.setLayoutX(panesDiffX / 2);
            peopleCountPane.setLayoutY(panesDiffY / 2);
            for (Button tableButton : buttons.values()) {
                tableButton.setDisable(true);
            }
            mainPane.getChildren().add(peopleCountPane);
        }
    }

    public boolean confirmTextFieldText() {
        boolean passed = true;
        try {
            int newPeopleCount = Integer.parseInt(peopleCountToTableController.peopleCountInputTextField.getText());
            peopleCountToTableController.setCountInput(newPeopleCount);
            peopleCountToTableController.setPeopleCountInputTextField();
        } catch (Exception ignored) {
            passed = false;
            peopleCountToTableController.setPeopleCountInputTextField();
        }
        return passed;
    }

    public void handleCustomCount(String tableName, int customCount) {
        peopleCountToTableController.setCountInput(customCount);
        Pane mainPane = (Pane)controllerStage.getScene().getRoot();
        ProgressIndicator progressIndicator = setProgressIndicator(mainPane);
        OpenTableSender openTableSender = new OpenTableSender(getClientSocket().getSocket());
        try {
            boolean openTableAnswer = openTableSender.sendPacket(tableName, 2);
        } catch (IOException throwables) {
            logError(throwables);
        }
        mainPane.getChildren().remove(progressIndicator);
    }

    public void logError(IOException throwables) {
        Supplier<String> error = throwables::getMessage;
        LoggerFactory.getLogger(MainMenuController.class).error(error);
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
