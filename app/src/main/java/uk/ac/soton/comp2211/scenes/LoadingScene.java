package uk.ac.soton.comp2211.scenes;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoadingScene {

  private Stage stage;
  private Label logLabel;
  private Handler logHandler;

  public LoadingScene(Stage stage, Logger logger) {
    this.stage = stage;

    // Create a label to display the log messages
    logLabel = new Label();
    logLabel.setAlignment(Pos.CENTER);

    // Add a log handler to capture the log messages and display them on the label
    logHandler = new LogHandler();
    logger.addHandler(logHandler);

    // Create a stack pane to hold the label
    StackPane root = new StackPane(logLabel);

    // Create a scene with the stack pane as the root
    Scene scene = new Scene(root, 400, 300);

    // Set the scene on the stage
    stage.setScene(scene);
  }

  public void show() {
    // Show the stage on the JavaFX application thread
    Platform.runLater(() -> stage.show());
  }

  public void hide() {
    // Hide the stage on the JavaFX application thread
    Platform.runLater(() -> stage.hide());

    // Remove the log handler from the logger
    Logger.getLogger("").removeHandler(logHandler);
  }

  private class LogHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
      // Update the label with the log message
      Platform.runLater(() -> {
        String message = record.getMessage() + "\n";
        if (record.getLevel() == Level.SEVERE) {
          logLabel.setStyle("-fx-text-fill: red;");
        } else if (record.getLevel() == Level.WARNING) {
          logLabel.setStyle("-fx-text-fill: orange;");
        } else {
          logLabel.setStyle("-fx-text-fill: black;");
        }
        logLabel.setText(logLabel.getText() + message);
      });
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}
  }
}
