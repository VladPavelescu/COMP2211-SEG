package uk.ac.soton.comp2211.scenes;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import uk.ac.soton.comp2211.ui.AppWindow;
import uk.ac.soton.comp2211.ui.GamePane;
import uk.ac.soton.comp2211.utility.SettingsManager;

/**
 * A Base Scene used in the game. Handles common functionality between all scenes.
 */
public abstract class BaseScene {

  protected final AppWindow appWindow;

  protected GamePane root;
  protected Scene scene;

  /**
   * Create a new scene, passing in the AppWindow the scene will be displayed in
   * @param appWindow the game window
   */
  public BaseScene(AppWindow appWindow) {
    this.appWindow = appWindow;
  }

  /**
   * Initialise this scene. Called after creation
   */
  public abstract void initialise();

  /**
   * Build the layout of the scene
   */
  public abstract void build();

  /**
   * Create a new JavaFX scene using the root contained within this scene
   * @return JavaFX scene
   */
  public Scene setScene() {
    var previous = appWindow.getScene();

    Color color = Color.TRANSPARENT;
    Scene scene;
    switch (SettingsManager.getTheme()) {
      case "DEFAULT_THEME" -> color = Color.rgb(150, 195, 215);
      case "DARK_THEME" -> color = Color.rgb(48, 48, 48);
      case "LIGHT_THEME" -> color = Color.rgb(255, 255, 255);
    }
    scene = new Scene(root, previous.getWidth(), previous.getHeight(), color);

//    scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
    this.scene = scene;
    SettingsManager.setTheme(scene);
    return scene;
  }

  public void updateScene() {
    var previous = appWindow.getScene();
    Color color = Color.TRANSPARENT;
    Scene scene;
    switch (SettingsManager.getTheme()) {
      case "DEFAULT_THEME" -> color = Color.rgb(150, 195, 215);
      case "DARK_THEME" -> color = Color.rgb(48, 48, 48);
      case "LIGHT_THEME" -> color = Color.rgb(255, 255, 255);
    }
    scene = new Scene(root, previous.getWidth(), previous.getHeight(), color);
    SettingsManager.setTheme(scene);
  }

  /**
   * Get the JavaFX scene contained inside
   * @return JavaFX scene
   */
  public Scene getScene() {
    return this.scene;
  }

}
