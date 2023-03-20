package uk.ac.soton.comp2211.scenes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import uk.ac.soton.comp2211.ui.AppWindow;
import uk.ac.soton.comp2211.ui.GamePane;
import uk.ac.soton.comp2211.utility.SettingsManager;

public class SettingsScene extends BaseScene{

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

    var fontSizeText = new Label("Set Font Size:");
    var mediumFontButton = new Button("Medium Font");
    var bigFontButton = new Button("Big Font");
    var fontButtons = new HBox();
    //fontSizeText.getStyleClass().add("heading");
    fontSizeText.setTextAlignment(TextAlignment.CENTER);
    fontButtons.getChildren().addAll(mediumFontButton,bigFontButton);
    fontButtons.setAlignment(Pos.CENTER);
    fontButtons.setSpacing(10);

    var themeLabel = new Label("Set Theme:");
    var defaultTheme = new Button("Default Theme");
    var darkTheme = new Button("Dark Theme");
    var lightTheme = new Button("Light Theme");
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
    root.setAlignment(Pos.CENTER);
    root.getChildren().add(menuButtons);

    defaultTheme.setOnAction(event -> {
      if(!SettingsManager.isDefaultThemeEnabled()) {
        SettingsManager.enableDefaultTheme(scene);
      }
    });

    darkTheme.setOnAction(event -> {
      if(!SettingsManager.isDarkThemeEnabled()) {
        SettingsManager.enableDarkTheme(scene);
      }
    });

    lightTheme.setOnAction(event -> {
      if(!SettingsManager.isLightThemeEnabled()) {
        SettingsManager.enableLightTheme(scene);
      }
    });

  }

}
