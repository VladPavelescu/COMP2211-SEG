package uk.ac.soton.comp2211.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LineGraphController implements Initializable {

    @FXML
    private LineChart<String, Number> lineGraph;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private Button costBut;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    public void loadData(){


        //sample data
        XYChart.Series<String,Number> impressions = new XYChart.Series<>();
        impressions.setName("Impressions");
        impressions.getData().add(new XYChart.Data<>("2022-01-01", 10));
        impressions.getData().add(new XYChart.Data<>("2022-02-02", 3));
        impressions.getData().add(new XYChart.Data<>("2022-03-03", 14));
        impressions.getData().add(new XYChart.Data<>("2022-04-04", 222));
        impressions.getData().add(new XYChart.Data<>("2022-05-05", 4));

        XYChart.Series<String,Number> clicks = new XYChart.Series<>();
        clicks.setName("Clicks");
        clicks.getData().add(new XYChart.Data<>("2022-01-01", 40));
        clicks.getData().add(new XYChart.Data<>("2022-03-21", 25));
        clicks.getData().add(new XYChart.Data<>("2022-03-01", 45));
        clicks.getData().add(new XYChart.Data<>("2021-11-01", 66));
        clicks.getData().add(new XYChart.Data<>("2022-05-03", 1));

        lineGraph.getData().addAll(impressions,clicks);

    }

    @FXML
    private void buttonAction(ActionEvent event) {
        //Load total click cost histogram
        if(event.getSource().equals(costBut)){
            openHistogram();
        }
    }

    private void openHistogram() {

        try {
            FXMLLoader newPane = new FXMLLoader(getClass().getResource("Histogram.fxml"));
            Parent root1 = newPane.load();
            Stage stage = new Stage();
            stage.setTitle("Total Click Cost");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception ignored){

        }


    }


}