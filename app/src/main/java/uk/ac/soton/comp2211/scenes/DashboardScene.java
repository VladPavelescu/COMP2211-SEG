package uk.ac.soton.comp2211.scenes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.ui.AppWindow;
import uk.ac.soton.comp2211.ui.GamePane;

import java.io.IOException;

public class DashboardScene extends BaseScene {

    public DashboardScene(AppWindow appWindow) {
        super(appWindow);
    }

//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(DashboardScene.class.getResource("/fxml/LineGraph.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Ad Auction Dashboard");
//        stage.setScene(scene);
//        stage.show();
//    }

//    public static void main(String[] args) {
//        launch();
//    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {

        root = new GamePane(appWindow.getWidth(), appWindow.getHeight());

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(DashboardScene.class.getResource("/fxml/LineGraph.fxml"));
            Pane newPane = fxmlLoader.load();
            newPane.getStyleClass().add("menu-background");
            root.getChildren().add(newPane);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}