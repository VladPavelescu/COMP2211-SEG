package uk.ac.soton.comp2211.graphs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LineGraph extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LineGraph.class.getResource("LineGraph.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Ad Auction Dashboard");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}