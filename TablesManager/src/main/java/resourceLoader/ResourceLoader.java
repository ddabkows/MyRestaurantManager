package resourceLoader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import resources.Resources;

import java.io.IOException;
import java.util.Objects;

public class ResourceLoader {
    public static Parent getResource(Resources resource) throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(
               ResourceLoader.class.getClassLoader().getResource(resource.toString())));
    }
}
