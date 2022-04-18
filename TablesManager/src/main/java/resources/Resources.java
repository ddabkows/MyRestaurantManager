package resources;


/**Paths to the fxml files
 *
 */
public enum Resources {
    // Window showed at the startup
    LAUNCHER {public final String toString() {return ResourcesDirectories.LAUNCHERDIR + "LauncherWindow.fxml";}},

    MAINMENU {public final String toString() {return ResourcesDirectories.MAINMENUDIR + "MainMenuWindow.fxml";}},
    OPENNEWTABLE {public final String toString() {return ResourcesDirectories.MAINMENUDIR + "PeopleCountToTablePane.fxml";}},
    TABLEMENU {public final String toString() {return ResourcesDirectories.TABLEVIEW + "MainTableWindow.fxml";}},
}
