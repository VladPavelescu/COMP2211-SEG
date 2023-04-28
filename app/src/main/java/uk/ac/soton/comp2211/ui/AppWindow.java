package uk.ac.soton.comp2211.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.scenes.BaseScene;
import uk.ac.soton.comp2211.scenes.DashboardScene;
import uk.ac.soton.comp2211.scenes.MenuScene;
import uk.ac.soton.comp2211.scenes.SettingsScene;
import uk.ac.soton.comp2211.utility.SettingsManager;

public class AppWindow {

  private static final Logger logger = LogManager.getLogger(AppWindow.class);

  private final int width;
  private final int height;

  private final Stage stage;

  private BaseScene currentScene;
  private Scene scene;

  public AppWindow(Stage stage, int width, int height) {
    this.width = width;
    this.height = height;

    this.stage = stage;

    //Setup window
    setupStage();

    //Setup resources
    setupResources();

    //Setup default scene
    setupDefaultScene();

    //Load Menu scene
    startMenu();

  }

  /**
   * Loads the menu scene
   */
  public void startMenu() {
    loadScene(new MenuScene(this));
  }

  /**
   * Loads the dashboard scene
   */
  public void startDashboard(){ loadScene(new DashboardScene(this));}

  /**
   * Loads the settings scene
   */
  public void startSettingsScene(){ loadScene(new SettingsScene(this));}

  /**
   * Load a given scene which extends BaseScene and switch over.
   *
   * @param newScene new scene to load
   */
  public void loadScene(BaseScene newScene) {
    //Create the new scene and set it up
    newScene.build();
    currentScene = newScene;
    scene = newScene.setScene();
    stage.setScene(scene);

    //Initialise the scene when ready
    Platform.runLater(() -> currentScene.initialise());
  }

  /**
   * Setup the default settings for the stage itself (the window), such as the title and minimum
   * width and height.
   */
  private void setupStage() {
    stage.setTitle("Ad Auction Dashboard");
    stage.setMinWidth(width);
    stage.setMinHeight(height + 20);
    stage.setOnCloseRequest(ev -> App.getInstance().shutdown());
  }

  private void setupDefaultScene() {
    Color color = Color.TRANSPARENT;
    Scene scene;
    switch (SettingsManager.getTheme()) {
      case "DEFAULT_THEME" -> color = Color.rgb(150, 195, 215);
      case "DARK_THEME" -> color = Color.rgb(48, 48, 48);
      case "LIGHT_THEME" -> color = Color.rgb(255, 255, 255);
    }
    this.scene = new Scene(new Pane(), width, height, color);
    stage.setScene(this.scene);
  }

  /**
   * Setup the font and any other resources we need
   */
  private void setupResources() {
    logger.info("Loading resources");

  }

  /**
   * Get the current scene being displayed
   *
   * @return scene
   */
  public Scene getScene() {
    return scene;
  }

  /**
   * Get the width of the Game Window
   *
   * @return width
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Get the height of the Game Window
   *
   * @return height
   */
  public int getHeight() {
    return this.height;
  }
}
