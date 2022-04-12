package resourceloader;

import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Objects;

import resources.Resources;


/**Simple class with a static method that has as purpose
 * to load a fxml file and to give it's parent
 */
public final class ResourceLoader {
    private ResourceLoader() {}
    /**Method called to load a fxml file
     * @param resource resource (path) to the fxml file
     * @return Parent object of the resource
     * @throws IOException exception thrown if the file doesn't exist (never thrown)
     */
    public static FXMLLoader getResource(Resources resource) throws IOException {
        // FXMLLoader = stage loader from a fxml file
        return new FXMLLoader(Objects.requireNonNull(ResourceLoader.class.getClassLoader().getResource(
                resource.toString())));
    }
}
