package uk.ac.soton.comp2211.utility;

import javafx.scene.Scene;

public class ThemeManager {

    private static final String DARK_THEME = ThemeManager.class.getResource("/css/darkTheme.css").toExternalForm();
    private static final String LIGHT_THEME = ThemeManager.class.getResource("/css/app.css").toExternalForm();

    private static boolean isDarkThemeEnabled = false;

    public static boolean isDarkThemeEnabled() {
        return isDarkThemeEnabled;
    }

    public static void enableDarkTheme(Scene scene) {
        isDarkThemeEnabled = true;
        setTheme(scene);
    }

    public static void enableLightTheme(Scene scene) {
        isDarkThemeEnabled = false;
        setTheme(scene);
    }

    public static void setTheme(Scene scene) {
        scene.getStylesheets().clear();
        scene.setUserAgentStylesheet(null);
        if (isDarkThemeEnabled) {
            scene.getStylesheets().add(DARK_THEME);
        } else {
            scene.getStylesheets().add(LIGHT_THEME);
        }
    }
}