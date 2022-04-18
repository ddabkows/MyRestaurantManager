package resources;


/**Paths to the fxml files
 *
 */
public enum Resources {
    // Window showed at the startup
    LAUNCHER {public String toString() {return ResourcesDirectories.LAUNCHERDIR + "LauncherWindow.fxml";}},

    MAINMENU {public String toString() {return ResourcesDirectories.MAINMENUDIR + "MainMenuWindow.fxml";}},
    OPENNEWTABLE {public String toString() {return ResourcesDirectories.MAINMENUDIR + "PeopleCountToTablePane.fxml";}},
}
