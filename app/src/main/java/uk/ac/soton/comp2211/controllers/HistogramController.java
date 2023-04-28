package uk.ac.soton.comp2211.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import uk.ac.soton.comp2211.logic.SQLExecutor;

public class HistogramController implements Initializable {

  @FXML
  BarChart costHistogram;

  @FXML
  private StackPane stackPane;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    loadData();
  }

  private void loadData() {
    new Thread(() -> {
      Integer maxValue = SQLExecutor.getMaxClickCost();

      //Make maxValue be an even number
      if (maxValue % 2 == 1) {
        maxValue++;
      }

      ArrayList<Integer> numberRanges = new ArrayList<>();
      for (int i = 0; i <= maxValue; i++) {
        numberRanges.add(i);
      }
      Integer lowerBound = 0;
      Integer upperBound = 1;

      Series<String, Number> series = new Series<>();
      series.setName("Click cost");

      while (upperBound < (numberRanges.size())) {
        String[] values = SQLExecutor.getHistogramData(lowerBound.toString(),
            upperBound.toString());
        series.getData().add(
            new Data<>(lowerBound + "-" + upperBound,
                Integer.parseInt(values[0].strip())));
        lowerBound++;
        upperBound++;
      }

      Platform.runLater(() -> costHistogram.getData().add(series));

      for (Data<String, Number> data : series.getData()) {
        // Platform.runLater() queues up tasks on the Application thread (GUI stuff)
        Platform.runLater(
            () -> data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
                event -> Tooltip.install(data.getNode(),
                    new Tooltip(data.getYValue().toString()))));
      }
    }).start();

  }

}
