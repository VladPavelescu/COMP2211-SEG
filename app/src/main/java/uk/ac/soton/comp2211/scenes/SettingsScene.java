package uk.ac.soton.comp2211.scenes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.ui.AppWindow;
import uk.ac.soton.comp2211.ui.GamePane;
import uk.ac.soton.comp2211.utility.ThemeManager;

public class SettingsScene extends BaseScene {

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
    menuButtons.setSpacing(20);

    //Create font button
    var fontButton = new Button("Font");
    fontButton.getStyleClass().add("heading");

    //Create font size button
    var fontSizeButton = new Button("Font Size");
    fontSizeButton.getStyleClass().add("heading");

    //Create background changer button
    var backgroundButton = new Button("Change Background");
    backgroundButton.getStyleClass().add("heading");

    //Create dark mode button
    var darkModeButton = new Button("Dark Mode");
    darkModeButton.getStyleClass().add("heading");

    //Create back button
    var backButton = new Button("Back");
    backButton.getStyleClass().add("heading");
    backButton.setOnAction(e -> appWindow.startMenu());

    //Display buttons
    menuButtons.getChildren()
        .addAll(fontButton, fontSizeButton, backgroundButton, darkModeButton, backButton);
    menuButtons.setAlignment(Pos.CENTER);
    stackPane.getChildren().add(menuButtons);

    darkModeButton.setOnAction(event -> {
      if (ThemeManager.isDarkThemeEnabled()) {
        ThemeManager.enableLightTheme(scene);
      } else {
        ThemeManager.enableDarkTheme(scene);
      }
    });
  }
}
