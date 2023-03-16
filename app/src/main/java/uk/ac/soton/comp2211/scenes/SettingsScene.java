package uk.ac.soton.comp2211.scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.ui.AppWindow;
import uk.ac.soton.comp2211.ui.GamePane;

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

    //Display buttons
    menuButtons.getChildren().addAll(fontButton, fontSizeButton, backgroundButton, darkModeButton);
    menuButtons.setAlignment(Pos.CENTER);
    root.getChildren().add(menuButtons);

    darkModeButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent e) {
        scene.getStylesheets().clear();
        scene.setUserAgentStylesheet(null);
        scene.getStylesheets().add(getClass().getResource("/css/darkTheme.css").toExternalForm());
      }
    });


  }


}
