package uk.ac.soton.comp2211.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import uk.ac.soton.comp2211.controllers.DashboardController;
import uk.ac.soton.comp2211.ui.AppWindow;
import uk.ac.soton.comp2211.ui.GamePane;

public class TotalCostScene extends BaseScene {

    public TotalCostScene(AppWindow appWindow) {
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

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(DashboardScene.class.getResource("/fxml/Histogram.fxml"));
            Pane newPane = fxmlLoader.load();
            newPane.getStyleClass().add("menu-background");
            root.getChildren().add(newPane);

            //Add functionality to the back button
            DashboardController controller = fxmlLoader.getController();
            controller.backButton.setOnAction(e -> appWindow.startMenu());

        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}