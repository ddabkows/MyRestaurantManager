package main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import resourceLoader.ResourceLoader;
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
        // Check of all the resources
        checkResources();
        // Setting the launcher window resource
        Parent root = ResourceLoader.getResource(Resources.LAUNCHER);
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

    /**Method created to check all the resources
     * If a resource does not exist, the program exits
     */
    private void checkResources() {
        for (Resources resource : Resources.values()) {
            try {
                System.out.println("Checking resource : " + resource + "...");
                ResourceLoader.getResource(resource);
            } catch (IOException | NullPointerException ignored) {
                System.out.println(resource + " not found. Force shut down.");
                System.exit(1);
            }

        }
    }
}
