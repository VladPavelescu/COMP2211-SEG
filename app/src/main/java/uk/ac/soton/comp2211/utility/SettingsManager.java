package uk.ac.soton.comp2211.utility;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import uk.ac.soton.comp2211.scenes.BaseScene;

public class SettingsManager {

  private static final String DEFAULT_THEME = ThemeManager.class.getResource("/css/app.css").toExternalForm();
  private static final String DARK_THEME = ThemeManager.class.getResource("/css/darkTheme.css").toExternalForm();
  private static final String LIGHT_THEME = ThemeManager.class.getResource("/css/lightTheme.css").toExternalForm();
  private static final String BIG_FONT = ThemeManager.class.getResource("/css/bigFont.css").toExternalForm();

  private static boolean isDefaultThemeEnabled = true;
  private static boolean isDarkThemeEnabled = false;
  private static boolean isLightThemeEnabled = false;
  private static boolean isBigFontEnabled = false;

  public static boolean isDarkThemeEnabled() {
    return isDarkThemeEnabled;
  }
  public static boolean isDefaultThemeEnabled() {
    return isDefaultThemeEnabled;
  }
  public static boolean isLightThemeEnabled() {
    return isLightThemeEnabled;
  }
  public static boolean isBigFontEnabled() {
    return isBigFontEnabled;
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

  public static void enableBigFont(Scene scene) {
    isBigFontEnabled = true;
    setTheme(scene);
  }

  public static void enableDefaultFont(Scene scene) {
    isBigFontEnabled = false;
    setTheme(scene);
  }

  public static void setTheme(Scene scene) {
    scene.getStylesheets().clear();
    scene.setUserAgentStylesheet(null);
    if(isBigFontEnabled) {
      if (isDefaultThemeEnabled) {
        scene.getStylesheets().addAll(DEFAULT_THEME, BIG_FONT);
      } else if (isDarkThemeEnabled) {
        scene.getStylesheets().addAll(DARK_THEME, BIG_FONT);
      } else if (isLightThemeEnabled) {
        scene.getStylesheets().addAll(LIGHT_THEME, BIG_FONT);
      }
    } else {
      if (isDefaultThemeEnabled) {
        scene.getStylesheets().add(DEFAULT_THEME);
      } else if (isDarkThemeEnabled) {
        scene.getStylesheets().add(DARK_THEME);
      } else if (isLightThemeEnabled) {
        scene.getStylesheets().add(LIGHT_THEME);
      }
    }

  }

  public static void setTheme(Pane pane) {
    pane.getStylesheets().clear();
    if(isBigFontEnabled) {
      if (isDefaultThemeEnabled) {
        pane.getStylesheets().addAll(DEFAULT_THEME, BIG_FONT);
      } else if (isDarkThemeEnabled) {
        pane.getStylesheets().addAll(DARK_THEME, BIG_FONT);
      } else if (isLightThemeEnabled) {
        pane.getStylesheets().addAll(LIGHT_THEME, BIG_FONT);
      }
    } else {
      if (isDefaultThemeEnabled) {
        pane.getStylesheets().add(DEFAULT_THEME);
      } else if (isDarkThemeEnabled) {
        pane.getStylesheets().add(DARK_THEME);
      } else if (isLightThemeEnabled) {
        pane.getStylesheets().add(LIGHT_THEME);
      }
    }

  }

  public static String getTheme() {
    if (isDefaultThemeEnabled) {
      return "DEFAULT_THEME";
    } else if (isDarkThemeEnabled) {
      return "DARK_THEME";
    } else {
      return "LIGHT_THEME";
    }
  }
}
