package resourceLoader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import resources.Resources;

import java.io.IOException;
import java.util.Objects;

/**Simple class with a static method that has as purpose
 * to load a fxml file and to give it's parent
 */
public class ResourceLoader {
    /**Method called to load a fxml file
     * @param resource resource (path) to the fxml file
     * @return Parent object of the resource
     * @throws IOException exception thrown if the file doesn't exist (never thrown)
     */
    public static Parent getResource(Resources resource) throws IOException {
        // FXMLLoader = stage loader from a fxml file
        return FXMLLoader.load(Objects.requireNonNull(
               ResourceLoader.class.getClassLoader().getResource(resource.toString())));
    }
}
