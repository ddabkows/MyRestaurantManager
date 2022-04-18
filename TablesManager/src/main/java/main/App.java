package main;


import client.ExitSender;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Supplier;
import org.junit.platform.commons.logging.LoggerFactory;

import resourceloader.ResourceLoader;
import resources.Images;
import resources.Resources;
import client.ClientSocketBuilder;
import controllers.launcherwindows.LauncherController;

/**App class that creates the main program Window
 *
 */
public class App extends Application {
    ClientSocketBuilder clientSocketBuilder = new ClientSocketBuilder();
    /**Application start override, running the launcher window
     * @param stage unused parameter responsible for setting the stage (new stage anyway)
     * @throws Exception thrown for unexisting resource (impossible)
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Check of all the resources
        checkImages();
        checkResources();
        // Setting the launcher window resource
        FXMLLoader rootLoader = ResourceLoader.getResource(Resources.LAUNCHER);
        Parent root = rootLoader.load();
        LauncherController launcherController = rootLoader.getController();
        launcherController.setClientSocket(clientSocketBuilder);
        stage.setTitle("TablesManager");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        new ExitSender(clientSocketBuilder.getSocket()).sendPacket();
        super.stop();
    }

    /**Method called by the main class
     * @param args args from the terminal from the main run
     */
    public static void app(String[] args) {
        launch(args);
    }

    private void checkImages() {
        for (Images image : Images.values()) {
            try {
                Supplier<String> checkMessage = ()-> "Checking image : " + image + "...";
                LoggerFactory.getLogger(Main.class).info(checkMessage);
                new FileInputStream(image.toString());
            } catch (FileNotFoundException error) {
                Supplier<String> errorMessage = ()-> image + " not found. Force shut down.";
                LoggerFactory.getLogger(Main.class).error(errorMessage);
                System.exit(1);
            }
        }
    }

    /**Method created to check all the resources
     * If a resource does not exist, the program exits
     */
    private void checkResources() {
        for (Resources resource : Resources.values()) {
            try {
                Supplier<String> checkMessage = ()-> "Checking resource : " + resource + "...";
                LoggerFactory.getLogger(Main.class).info(checkMessage);
                ResourceLoader.getResource(resource);
            } catch (IOException ignored) {
                Supplier<String> errorMessage = ()-> resource + " not found. Force shut down.";
                LoggerFactory.getLogger(Main.class).error(errorMessage);
                System.exit(1);
            }
        }
    }
}
