package au.edu.unsw.infs3634.musicrecommender;

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
