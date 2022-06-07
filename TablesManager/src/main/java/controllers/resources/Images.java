package controllers.resources;

public enum Images {
    CURPATH {public final String toString() {return System.getProperty("user.dir").replace('\\', '/');}},

    MAINPATH  {public final String toString() {return CURPATH + "/TablesManager/src/main/resources/img/";}},

    COMMENT {public final String toString() {return MAINPATH + "messageIcon.png";}},
    SETTINGS {public final String toString() {return MAINPATH + "settings.png";}},
    OPEN {public final String toString() {return MAINPATH + "open.png";}},
}
