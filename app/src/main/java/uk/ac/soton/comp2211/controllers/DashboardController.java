package uk.ac.soton.comp2211.controllers;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.logic.SQLExecutor;

public class DashboardController implements Initializable {

  @FXML
  private StackPane stackPane;

  @FXML
  private LineChart<String, Number> lineGraph;

  @FXML
  private Button costBut;

  @FXML
  private DatePicker start_date;

  @FXML
  private DatePicker end_date;

  @FXML
  public Button backButton;

  @FXML
  private VBox metricsVBox;

  private ArrayList<String> metricsSelected = new ArrayList<>();

  private ArrayList<CheckBox> allMetrics = new ArrayList<>();

  private String changedMetric;

  private boolean dateChanged;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    start_date.setValue(LocalDate.of(2015, 1, 1));
    end_date.setValue(LocalDate.of(2015, 1, 14));

    //Update graph when date is updated
    start_date.setOnAction(e -> {
      dateChanged = true;
      loadData();
    });
    end_date.setOnAction(e -> {
      dateChanged = true;
      loadData();
    });

    // Alternative in Histogram.fxml
    //scrollPane = new ScrollPane();
    //scrollPane.setFitToWidth(true);
    //scrollPane.setPrefViewportWidth(175); // Set the preferred width of the scrollpane
    //scrollPane.setContent(metricsVBox);
    //anchorPane = new AnchorPane();
    //anchorPane.getChildren().add(scrollPane);
    //AnchorPane.setBottomAnchor(scrollPane, 0.0);
    //AnchorPane.setLeftAnchor(scrollPane, 0.0);
    //AnchorPane.setBottomAnchor(scrollPane, 0.0);

    //stackPane.getChildren().add(anchorPane);

    //selects all the checkboxes that control the metrics
    for (Node node : metricsVBox.getChildren()) {
      if (node instanceof CheckBox checkBox) {
        allMetrics.add(checkBox);
        checkBox.getStyleClass().add("checkbox");
        Tooltip tooltip = new Tooltip();
        String text = switch (checkBox.getId()) {
          case "bounceCountCheckbox" -> "The number of single-page visits without any further action.";
          case "bounceRateCheckbox" -> "The percentage of single-page visits divided by the total number of sessions.";
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
    allMetrics.forEach(c -> c.setDisable(true));
    start_date.setDisable(true);
    end_date.setDisable(true);

    // Run the calculations on a background thread to keep the application responsive
    new Thread(() -> {

      var dates = SQLExecutor.getDates(start_date.getValue().toString(),
          end_date.getValue().toString());

      // Update all selected metrics if the date was changed
      if (dateChanged) {
        dateChanged = false;

        Platform.runLater(() -> lineGraph.getData().clear());

        for (String metric : metricsSelected) {
          Series<String, Number> series = new Series<>();
          series.setName(metric);

          for (LocalDate date : dates) {
            var value = SQLExecutor.executeSQL(date.toString(), metric)[0].trim();
            if (!value.equals("null")) {
              series.getData().add(new Data<>(date.toString(), Double.parseDouble(value)));
            }
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
      } else {

        // If the series updated is already on the graph, remove it and return
        for (Series<String, Number> series : lineGraph.getData()) {
          if (series.getName().equals(changedMetric)) {
            Platform.runLater(() -> {
              lineGraph.getData().remove(series);
              stackPane.getChildren().remove(progressIndicator);
              allMetrics.forEach(c -> c.setDisable(false));
              start_date.setDisable(false);
              end_date.setDisable(false);
            });
            return;
          }
        }

        //Otherwise, create the series and add it to the graph

        Series<String, Number> series = new Series<>();
        series.setName(changedMetric);

        for (LocalDate date : dates) {
          var value = SQLExecutor.executeSQL(date.toString(), changedMetric)[0].trim();
          if (!value.equals("null")) {
            series.getData().add(new Data<>(date.toString(), Double.parseDouble(value)));
          }
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
        allMetrics.forEach(c -> c.setDisable(false));
        start_date.setDisable(false);
        end_date.setDisable(false);
      });
    }).start();
  }

  @FXML
  private void buttonAction(ActionEvent event) {
    //Load total click cost histogram
    if (event.getSource().equals(costBut)) {
      openHistogram();
    }
  }

  private void openHistogram() {
    try {
      FXMLLoader newPane = new FXMLLoader(getClass().getResource("/fxml/Histogram.fxml"));
      Parent root1 = newPane.load();
      Stage stage = new Stage();
      stage.setTitle("Total Click Cost");
      stage.setScene(new Scene(root1));
      stage.show();
    } catch (Exception ignored) {
    }
  }


}