package main;

import client.exit.AlertExitSender;
import client.socketbuilder.ClientSocketBuilder;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ShutdownHook extends Thread {
    private final Stage stage;
    private final ClientSocketBuilder clientSocketBuilder;

    public ShutdownHook(Stage stageToSet, ClientSocketBuilder clientSocketBuilderToSet) {
        stage = stageToSet;
        clientSocketBuilder = clientSocketBuilderToSet;
    }

    public void run() {
        ObservableList<Node> widgets = stage.getScene().getRoot().getChildrenUnmodifiable();
        for (Node widget : widgets) {
            if (Objects.equals(widget.getId(), "tableNameLabelSHUTDOWN")) {
                String tableName = ((Label)widget).getText();
                AlertExitSender alertExitSender = new AlertExitSender(clientSocketBuilder.getSocket());
                try {
                    alertExitSender.sendPacket(tableName);
                } catch (IOException ignored) {}
            }
        }
    }
}