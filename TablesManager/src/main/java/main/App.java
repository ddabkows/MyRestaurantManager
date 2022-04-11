package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

import resources.Resources;

/**App class that creates the main program Window
 *
 */
public class App extends Application {
    /**Application start override, running the launcher window
     * @param stage unused parameter responsible for setting the stage (new stage anyway)
     * @throws Exception thrown for unexisting resource (impossible)
     */
    @Override
    public void start(Stage stage) throws Exception {
        checkResources();
        System.out.println(Resources.LAUNCHER);
        System.out.println(System.getProperty("user.dir"));
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(Resources.LAUNCHER.toString())));
        stage.setTitle("TablesManager");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**Method called by the main class
     * @param args args from the terminal from the main run
     */
    public static void app(String[] args) {
        launch(args);
    }

    private void checkResources() {
        for (Resources resource : Resources.values()) {
            try {
                System.out.println("Checking resource : " + resource + "...");
                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(resource.toString())));
            } catch (IOException | NullPointerException ignored) {
                System.out.println(resource + " not found. Force shut down.");
                System.exit(1);
            }

        }
    }
}
