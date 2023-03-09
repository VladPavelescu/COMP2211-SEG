package uk.ac.soton.comp2211.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import uk.ac.soton.comp2211.ui.AppWindow;
import uk.ac.soton.comp2211.ui.GamePane;

public class DashboardScene extends BaseScene {

    public DashboardScene(AppWindow appWindow) {
        super(appWindow);
    }

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