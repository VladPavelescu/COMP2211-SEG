package uk.ac.soton.comp2211.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.logic.SQLExecutor;
import uk.ac.soton.comp2211.utility.SettingsManager;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DashboardController implements Initializable {

  @FXML
  private StackPane stackPane;

  @FXML
  private LineChart<String, Number> lineGraph;

  @FXML
  private Button costBut;

  @FXML
  private Button newGraph;

  @FXML
  private DatePicker start_date;

  @FXML
  private DatePicker end_date;

  @FXML
  public Button backButton;

  @FXML
  Button snapshotButton;

  @FXML
  private VBox metricsVBox;

  @FXML
  private ScrollPane metricsScrollPane;

  @FXML
  private ComboBox<String> intervalBox;

  @FXML
  private ComboBox<String> bounceDefinition;

  @FXML
  private ComboBox<String> contextBox;

  @FXML
  private ComboBox<String> incomeBox;

  @FXML
  private ComboBox<String> ageBox;

  @FXML
  private ComboBox<String> genderBox;

  private ArrayList<String> metricsSelected = new ArrayList<>();

  private ArrayList<CheckBox> allMetrics = new ArrayList<>();

  private String changedMetric;

  private boolean dateChanged;

  private boolean intervalChanged;

  private boolean bounceChanged;

  private boolean contextChanged;

  private boolean incomeChanged;

  private boolean ageChanged;

  private boolean genderChanged;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    start_date.setValue(LocalDate.of(2015, 1, 1));
    end_date.setValue(LocalDate.of(2015, 1, 14));

    metricsScrollPane.getStyleClass().add("scroll-pane");
    metricsScrollPane.setMinViewportHeight(200);
    metricsScrollPane.fitToWidthProperty();

    //Update graph when date is updated
    start_date.setOnAction(e -> {
      dateChanged = true;
      loadData();
    });
    end_date.setOnAction(e -> {
      dateChanged = true;
      loadData();
    });

    //Update graph when interval is updated
    intervalBox.setOnAction(e -> {
      intervalChanged = true;
      loadData();
    });

    //Update graph when bounce definition is updated
    bounceDefinition.setOnAction(e -> {
      bounceChanged = true;
      loadData();
    });

    //Update graph when audience segment is updated
    contextBox.setOnAction(e -> {
      contextChanged = true;
      loadData();
    });

    //Update graph when income range is updated
    incomeBox.setOnAction(e -> {
      incomeChanged = true;
      loadData();
    });

    //Update graph when age range is updated
    ageBox.setOnAction(e -> {
      ageChanged = true;
      loadData();
    });

    //Update graph when age range is updated
    genderBox.setOnAction(e -> {
      genderChanged = true;
      loadData();
    });

    snapshotButton.setOnAction(e -> {

      //create snapshot directory
      String snapshotsPath = "\\" + System.getProperty("user.dir") + "\\snapshots";
      File dir = new File(snapshotsPath);
      if (!dir.exists()) {
        dir.mkdir();
      }

      // Get a reference to the FXML content displayed in the StackPane
      Node fxmlRootNode = stackPane.getChildren().get(0);

      // Create a new WritableImage with the same dimensions as the root node
      WritableImage image = new WritableImage((int) fxmlRootNode.getBoundsInLocal().getWidth(),
          (int) fxmlRootNode.getBoundsInLocal().getHeight());

      // Take a snapshot of the root node
      fxmlRootNode.snapshot(null, image);

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
      LocalDateTime now = LocalDateTime.now();

      // Choose a file to save the image
      File file = new File(snapshotsPath + "\\" + formatter.format(now) + ".png");

      // Save the image as a PNG file
      try {
        DialogPane dialog;
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Snapshot saved");
        alert.setContentText("The snapshot has been successfully saved to: " + snapshotsPath + "\\"
            + formatter.format(now) + ".png");
        dialog = alert.getDialogPane();
        dialog.getStylesheets().add(getClass().getResource("/css/alertPane.css").toString());
        dialog.getStyleClass().add("dialog-pane");
        alert.show();
      } catch (IOException ex) {
        System.out.println("Error saving snapshot: " + ex.getMessage());
      }
    });

    //selects all the checkboxes that control the metrics
    for (Node node : metricsVBox.getChildren()) {
      if (node instanceof CheckBox checkBox) {
        allMetrics.add(checkBox);
        checkBox.getStyleClass().add("checkbox");
        Tooltip tooltip = new Tooltip();
        String text = switch (checkBox.getId()) {
          case "bounceCountCheckbox" -> "The number of users who clicks on an ad, but then fail to interact with the website.";
          case "bounceRateCheckbox" -> "The percentage of single-page/short-time visits divided by the total number of sessions.";
          case "clickCountCheckbox" -> "The total number of times users clicked on an advertisement.";
          case "conversionCountCheckbox" -> "The total number of desired actions completed by users after clicking on an advertisement.";
          case "cpaCheckbox" -> "(Cost per Action): The average cost an advertiser pays for each desired action (e.g., a purchase or signup) completed.";
          case "cpcCheckbox" -> "(Cost per Click): The average cost an advertiser pays for each click on an advertisement.";
          case "cpmCheckbox" -> "(Cost per Mille): The cost an advertiser pays for one thousand views (impressions) of an advertisement.";
          case "ctrCheckbox" -> "(Click-Through Rate): The percentage of ad impressions that result in clicks, calculated as clicks divided by impressions.";
          case "impressionNumberCheckbox" -> "The total number of times an advertisement has been displayed.";
          case "totalCostCheckbox" -> "The cumulative cost of an advertising campaign.";
          case "uniquesCountCheckbox" -> "The number of unique visitors who have viewed an advertisement.";
          default -> ""; // Replace with the text you want to display
        };

        tooltip.setText(text);
        Tooltip.install(checkBox, tooltip);
      }
    }

    //Time interval combobox
    intervalBox.getItems().addAll("Hourly", "Daily", "Weekly");
    intervalBox.getSelectionModel().select(1);

    //Bounce definition ComboBox
    bounceDefinition.getItems().addAll("Short Time Spent", "Single Page Visits");
    bounceDefinition.getSelectionModel().select(0);

    //Context ComboBox
    contextBox.getItems()
        .addAll("", "Blog", "News", "Hobbies", "Travel", "Shopping", "Social Media");
    contextBox.getSelectionModel().select(0);

    //Income ComboBox
    incomeBox.getItems().addAll("", "Low", "Medium", "High");
    incomeBox.getSelectionModel().select(0);

    //Gender ComboBox
    genderBox.getItems().addAll("", "Male", "Female");
    genderBox.getSelectionModel().select(0);

    //Age ComboBox
    ageBox.getItems().addAll("", "<25", "25-34", "35-44", "45-54", ">54");
    ageBox.getSelectionModel().select(0);
  }

  @FXML
  private void updateMetrics(ActionEvent event) {

    if (event.getSource() instanceof CheckBox checkBox) {
      changedMetric = checkBox.getText();
    }

    metricsSelected.clear();

    for (CheckBox c : allMetrics) {
      if (c.selectedProperty().get()) {
        metricsSelected.add(c.textProperty().get());
      }
    }

    loadData();
  }

  private void loadData() {

    //Create loading indicator
    ProgressIndicator progressIndicator = new ProgressIndicator(-1);
    stackPane.getChildren().add(progressIndicator);
    progressIndicator.setMaxSize(stackPane.getWidth() / 4, stackPane.getWidth() / 4);

    //Disable the metrics and dates while the thread is still working in the background
    setUIDisable(true);

    // Run the calculations on a background thread to keep the application responsive
    new Thread(() -> {

      if (dateChanged || intervalChanged || bounceChanged || contextChanged || incomeChanged
          || ageChanged || genderChanged) {
        dateChanged = false;
        intervalChanged = false;
        bounceChanged = false;
        contextChanged = false;
        incomeChanged = false;
        ageChanged = false;
        genderChanged = false;

        Platform.runLater(() -> lineGraph.getData().clear());

        for (String metric : metricsSelected) {

          //Retrieves all data values
          String[] values = SQLExecutor.executeSQL(bounceDefinition.getValue(),
              intervalBox.getValue(), metric, start_date.getValue().toString(),
              end_date.getValue().toString(), contextBox.getValue(), incomeBox.getValue(),
              ageBox.getValue(), genderBox.getValue());

          Series<String, Number> series = new Series<>();
          series.setName(metric);

          //Adds the data values into a series
          for (String value : values) {
            String parts[] = value.split("\\t");
            Data<String, Number> data = new Data<>(parts[0], Double.parseDouble(parts[1]));
            series.getData().add(data);
          }
          Platform.runLater(() -> lineGraph.getData().add(series));

          for (Data<String, Number> data : series.getData()) {
            // Platform.runLater() queues up tasks on the Application thread (GUI stuff)
            Platform.runLater(
                () -> data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
                    event -> Tooltip.install(data.getNode(),
                        new Tooltip(data.getXValue() + ", " + data.getYValue().toString()))));
          }
        }
      } else {

        // If the series updated is already on the graph, remove it and return
        for (Series<String, Number> series : lineGraph.getData()) {
          if (series.getName().equals(changedMetric)) {
            Platform.runLater(() -> {
              lineGraph.getData().remove(series);
              stackPane.getChildren().remove(progressIndicator);
              setUIDisable(false);
            });
            return;
          }
        }

        //Otherwise, create the series and add it to the graph
        Series<String, Number> series = new Series<>();
        series.setName(changedMetric);

        String[] values = SQLExecutor.executeSQL(bounceDefinition.getValue(),
            intervalBox.getValue(), changedMetric, start_date.getValue().toString(),
            end_date.getValue().toString(), contextBox.getValue(), incomeBox.getValue(),
            ageBox.getValue(), genderBox.getValue());

        for (String value : values) {
          String parts[] = value.split("\\t");
          Data<String, Number> data = new Data<>(parts[0], Double.parseDouble(parts[1]));
          series.getData().add(data);
        }

        // Platform.runLater() queues up tasks on the Application thread (GUI stuff)
        Platform.runLater(() -> lineGraph.getData().add(series));

        for (Data<String, Number> data : series.getData()) {
          // Platform.runLater() queues up tasks on the Application thread (GUI stuff)
          Platform.runLater(
              () -> data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
                  event -> Tooltip.install(data.getNode(),
                      new Tooltip(data.getXValue() + ", " + data.getYValue().toString()))));
        }
      }

      // Platform.runLater() queues up tasks on the Application thread (GUI stuff)
      Platform.runLater(() -> {
        stackPane.getChildren().remove(progressIndicator);
        setUIDisable(false);
      });
    }).start();
  }
  
  private void setUIDisable(Boolean bool) {
    allMetrics.forEach(c -> c.setDisable(bool));
    start_date.setDisable(bool);
    end_date.setDisable(bool);
    intervalBox.setDisable(bool);
    bounceDefinition.setDisable(bool);
    contextBox.setDisable(bool);
    incomeBox.setDisable(bool);
    ageBox.setDisable(bool);
    genderBox.setDisable(bool);
    snapshotButton.setDisable(bool);
    newGraph.setDisable(bool);
    costBut.setDisable(bool);
    backButton.setDisable(bool);
  }

  @FXML
  private void buttonAction(ActionEvent event) {
    //Load total click cost histogram
    if (event.getSource().equals(costBut)) {
      openHistogram();
    }
    //Load new graph
    if (event.getSource().equals(newGraph)) {
      openNewGraphWindow();
    }
  }

  private void openHistogram() {
    try {
      FXMLLoader newPane = new FXMLLoader(getClass().getResource("/fxml/Histogram.fxml"));
      Pane root1 = newPane.load();
      root1.getStyleClass().add("menu-background");
      SettingsManager.setTheme(root1);
      Stage stage = new Stage();
      stage.setTitle("Line Graph");
      stage.setScene(new Scene(root1));
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void openNewGraphWindow() {
    try {
      FXMLLoader newPane = new FXMLLoader(getClass().getResource("/fxml/NewLineGraph.fxml"));
      Pane root1 = newPane.load();
      root1.getStyleClass().add("menu-background");
      SettingsManager.setTheme(root1);
      Stage stage = new Stage();
      stage.setTitle("Line Graph");
      stage.setResizable(false);
      stage.setScene(new Scene(root1));
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}