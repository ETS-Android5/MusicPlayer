package au.syd.project.erwinmaysonq.musicplayer;

public class Setting {

    //Constructor
    public Setting() {
    }

    //Getter and setters
    public static boolean isDarkMode = false;

    public static boolean isIsDarkMode() {
        return isDarkMode;
    }

    public static void setIsDarkMode(boolean isDarkMode) {
        Setting.isDarkMode = isDarkMode;
    }
}
