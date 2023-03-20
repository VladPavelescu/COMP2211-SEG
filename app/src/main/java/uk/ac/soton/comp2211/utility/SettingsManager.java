package uk.ac.soton.comp2211.utility;

import javafx.scene.Scene;

public class SettingsManager {

  private static final String DEFAULT_THEME = ThemeManager.class.getResource("/css/app.css").toExternalForm();
  private static final String DARK_THEME = ThemeManager.class.getResource("/css/darkTheme.css").toExternalForm();
  private static final String LIGHT_THEME = ThemeManager.class.getResource("/css/lightTheme.css").toExternalForm();


  private static boolean isDefaultThemeEnabled = true;
  private static boolean isDarkThemeEnabled = false;
  private static boolean isLightThemeEnabled = false;

  public static boolean isDarkThemeEnabled() {
    return isDarkThemeEnabled;
  }
  public static boolean isDefaultThemeEnabled() {
    return isDefaultThemeEnabled;
  }
  public static boolean isLightThemeEnabled() {
    return isLightThemeEnabled;
  }

  public static void enableDefaultTheme(Scene scene) {
    isDefaultThemeEnabled = true;
    isDarkThemeEnabled = false;
    isLightThemeEnabled = false;
    setTheme(scene);
  }

  public static void enableDarkTheme(Scene scene) {
    isDefaultThemeEnabled = false;
    isDarkThemeEnabled = true;
    isLightThemeEnabled = false;
    setTheme(scene);
  }

  public static void enableLightTheme(Scene scene) {
    isDefaultThemeEnabled = false;
    isDarkThemeEnabled = false;
    isLightThemeEnabled = true;
    setTheme(scene);
  }

  public static void setTheme(Scene scene) {
    scene.getStylesheets().clear();
    scene.setUserAgentStylesheet(null);
    if (isDefaultThemeEnabled) {
      scene.getStylesheets().add(DEFAULT_THEME);
    } else if (isDarkThemeEnabled) {
      scene.getStylesheets().add(DARK_THEME);
    } else if (isLightThemeEnabled) {
      scene.getStylesheets().add(LIGHT_THEME);
    }
  }
}
