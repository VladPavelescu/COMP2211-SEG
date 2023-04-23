package uk.ac.soton.comp2211.scenes;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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

    final ImageView selectedImage = new ImageView();
    selectedImage.setFitHeight(200);
    selectedImage.setFitWidth(200);
    Image image = new Image(MenuScene.class.getResource("/img/logo2.JPG").toExternalForm());
    selectedImage.setImage(image);

    //placeholder label for the app
    //var label = new Label("Ad Auction Dashboard");
    //mainPane.setTop(label);
    //BorderPane.setAlignment(label, Pos.CENTER);

    //HBox for the buttons displayed on the menu
    var hbox = new HBox();
    mainPane.setCenter(hbox);
    BorderPane.setAlignment(hbox, Pos.CENTER);
    hbox.setAlignment(Pos.CENTER);
    hbox.setSpacing(10);

    //VBox for the logo and buttons
    var vbox = new VBox();
    mainPane.setCenter(vbox);
    BorderPane.setAlignment(vbox, Pos.CENTER);
    vbox.setAlignment(Pos.CENTER);
    vbox.setSpacing(100);
    
    //VBox to display the file names
    var fileBox = new HBox();
    mainPane.setBottom(fileBox);
    BorderPane.setAlignment(fileBox, Pos.BASELINE_LEFT);
    fileBox.setAlignment(Pos.BASELINE_LEFT);
    fileBox.setSpacing(10);
    var importLabel = new Label("Imported Files:");
    importLabel.setUnderline(true);
    fileBox.getChildren().add(importLabel);
    fileBox.getStyleClass().add("hbox");

    vbox.getChildren().addAll(selectedImage, hbox);

    //
    var loadFileButton = new Button("Upload log files");
    hbox.getChildren().add(loadFileButton);
    loadFileButton.setOnAction(e -> {
      File selectedFile = fileChooser.showOpenDialog(appWindow.getScene().getWindow());
      if (selectedFile != null) {

        //Create loading indicator
        ProgressIndicator progressIndicator = new ProgressIndicator(-1);

        // Add progress indicator to stackPane and set size
        stackPane.getChildren().add(progressIndicator);
        progressIndicator.setMaxSize(appWindow.getWidth()/4,appWindow.getWidth()/4);
        
        // Add the selected file to the list of files imported
        fileBox.getChildren().add(new Label(selectedFile.getName()));

        // Run the calculations on a background thread to keep the application responsive
        new Thread(() -> {
          logger.info("Selected file: " + selectedFile.getAbsolutePath());
          Switcher.readFirstLine(selectedFile.getAbsolutePath());

          // Platform.runLater() queues up tasks on the Application thread (GUI stuff)
          Platform.runLater(() -> {
            stackPane.getChildren().remove(progressIndicator);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("File upload");
            alert.setContentText("The file has been successfully uploaded!");
            alert.show();
          });
        }).start();
      }
    });

    var graphButton = new Button("Go to Graph");
    hbox.getChildren().add(graphButton);
    graphButton.setOnAction(e -> {
      appWindow.startDashboard();
    });

    var settingsButton = new Button("Settings");
    hbox.getChildren().add(settingsButton);
    settingsButton.setOnAction(e -> {
      appWindow.startSettingsScene();
    });

  }
}
