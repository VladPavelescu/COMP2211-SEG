package uk.ac.soton.comp2211.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.ui.AppWindow;
import uk.ac.soton.comp2211.ui.GamePane;
import uk.ac.soton.comp2211.utility.SettingsManager;

public class SettingsScene extends BaseScene{

  private static final Logger logger = LogManager.getLogger(SettingsScene.class);


  /**
   * Create a new scene, passing in the AppWindow the scene will be displayed in
   *
   * @param appWindow the game window
   */
  public SettingsScene(AppWindow appWindow) {
    super(appWindow);
  }

  @Override
  public void initialise() {

    root.getScene().setOnKeyPressed(keyEvent -> {
      if (keyEvent.getCode() == KeyCode.ESCAPE) {
        appWindow.startMenu();
      }
    });

  }

  @Override
  public void build() {

    root = new GamePane(appWindow.getWidth(), appWindow.getHeight());
    var stackPane = new StackPane();
    stackPane.setMaxWidth(appWindow.getWidth());
    stackPane.setMaxHeight(appWindow.getHeight());
    stackPane.getStyleClass().add("menu-background");
    root.getChildren().add(stackPane);

    var menuButtons = new VBox();
    var menuButtonsBack = new VBox();

    var fontSizeText = new Label("Set Font Size:");
    var defaultFontButton = new Button("Default");
    var bigFontButton = new Button("Big");
    var backButton = new Button("Back");
    var fontButtons = new HBox();
    fontSizeText.setTextAlignment(TextAlignment.CENTER);
    fontButtons.getChildren().addAll(defaultFontButton, bigFontButton);
    fontButtons.setAlignment(Pos.CENTER);
    fontButtons.setSpacing(10);

    var themeLabel = new Label("Set Theme:");
    var defaultTheme = new Button("Default");
    var darkTheme = new Button("Dark");
    var lightTheme = new Button("Light");
    var themeButtons = new HBox();
    themeLabel.getStyleClass().add("heading");
    themeLabel.setTextAlignment(TextAlignment.CENTER);
    themeButtons.getChildren().addAll(defaultTheme, darkTheme, lightTheme);
    themeButtons.setAlignment(Pos.CENTER);
    themeButtons.setSpacing(10);


    //Display buttons
    menuButtons.getChildren().addAll(fontSizeText, fontButtons, themeLabel, themeButtons);
    menuButtons.setAlignment(Pos.CENTER);
    menuButtons.setSpacing(10);

    menuButtonsBack.getChildren().addAll(menuButtons, backButton);
    menuButtonsBack.setAlignment(Pos.CENTER);
    menuButtonsBack.setMaxWidth(appWindow.getWidth());
    menuButtonsBack.setMaxHeight(appWindow.getHeight());
    menuButtonsBack.setSpacing(150);

    stackPane.getChildren().addAll(menuButtonsBack);

    backButton.setOnAction(event -> {
      appWindow.startMenu();
    });

    defaultFontButton.setOnAction(event -> {
      if(SettingsManager.isBigFontEnabled()) {
        SettingsManager.enableDefaultFont(scene);
        logger.info("User set default font");
      }
    });

    bigFontButton.setOnAction(event -> {
      if(!SettingsManager.isBigFontEnabled()) {
        SettingsManager.enableBigFont(scene);
        logger.info("User set big font");
      }
    });

    defaultTheme.setOnAction(event -> {
      if(!SettingsManager.isDefaultThemeEnabled()) {
        SettingsManager.enableDefaultTheme(scene);
        logger.info("User set default theme");
      }
    });

    darkTheme.setOnAction(event -> {
      if(!SettingsManager.isDarkThemeEnabled()) {
        SettingsManager.enableDarkTheme(scene);
        logger.info("User set dark theme");
      }
    });

    lightTheme.setOnAction(event -> {
      if(!SettingsManager.isLightThemeEnabled()) {
        SettingsManager.enableLightTheme(scene);
        logger.info("User set light theme");
      }
    });

  }

}
