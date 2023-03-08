package uk.ac.soton.comp2211.scenes;

import java.io.File;
import java.util.Arrays;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.logic.SQLExecutor;
import uk.ac.soton.comp2211.logic.Switcher;
import uk.ac.soton.comp2211.ui.AppWindow;
import uk.ac.soton.comp2211.ui.GamePane;


public class MenuScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(MenuScene.class);

  /**
   * Create a new scene, passing in the AppWindow the scene will be displayed in
   *
   * @param appWindow the game window
   */
  public MenuScene(AppWindow appWindow) {
    super(appWindow);
  }

  @Override
  public void initialise() {
    scene.setOnKeyPressed((e) -> {
      if (e.getCode() == KeyCode.ESCAPE) {
        App.getInstance().shutdown();
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

    var mainPane = new BorderPane();
    stackPane.getChildren().add(mainPane);

    //create the file chooser and restrict it to only accept csv files
    var fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV files", "*.csv"));

    //placeholder label for the app
    var label = new Label("Ad Auction Dashboard");
    mainPane.setTop(label);
    BorderPane.setAlignment(label, Pos.CENTER);

    //HBox for the buttons displayed on the menu
    var hbox = new HBox();
    mainPane.setCenter(hbox);
    BorderPane.setAlignment(hbox, Pos.CENTER);
    hbox.setAlignment(Pos.CENTER);
    hbox.setSpacing(10);

    //
    var loadFileButton = new Button("Upload log files");
    loadFileButton.getStyleClass().add("menu-item");
    hbox.getChildren().add(loadFileButton);
    loadFileButton.setOnAction(e -> {
      File selectedFile = fileChooser.showOpenDialog(appWindow.getScene().getWindow());
      if (selectedFile != null) {
        logger.info("Selected file: " + selectedFile.getAbsolutePath());
        Switcher.readFirstLine(selectedFile.getAbsolutePath());
      }
    });

    var sqlButton = new Button("Execute SQL");
    sqlButton.getStyleClass().add("menu-item");
    hbox.getChildren().add(sqlButton);
    sqlButton.setOnAction(e -> {
      String[] result = SQLExecutor.executeSQL();
      System.out.println(Arrays.toString(result));
    });
  }
}
