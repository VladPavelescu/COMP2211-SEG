package uk.ac.soton.comp2211;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.scenes.MenuScene;
import uk.ac.soton.comp2211.ui.AppWindow;


/**
 * JavaFX App
 */
public class App extends Application {

    /**
     * Base resolution width
     */
    private final int width = 800;

    /**
     * Base resolution height
     */
    private final int height = 600;

    private static App instance;
    private static final Logger logger = LogManager.getLogger(App.class);
    private Stage stage;

    /**
     * Start the game
     * @param args commandline arguments
     */
    public static void main(String[] args) {
        logger.info("Starting client");
        launch();
    }

    /**
     * Called by JavaFX with the primary stage as a parameter. Begins the game by opening the Game Window
     * @param stage the default stage, main window
     */
    @Override
    public void start(Stage stage) {
        instance = this;
        this.stage = stage;

        //Application icon
        Image image = new Image(App.class.getResource("/img/logo1.jpg").toExternalForm());
        stage.getIcons().add(image);

        //Open game window
        openApp();
    }

    /**
     * Create the AppWindow with the specified width and height
     */
    public void openApp() {
        logger.info("Opening app window");

        //Change the width and height in this class to change the base rendering resolution for all game parts
        var appWindow = new AppWindow(stage,width,height);
        //Display the AppWindow
        stage.show();
    }

    /**
     * Shutdown the game
     */
    public void shutdown() {
        logger.info("Shutting down");
        System.exit(0);
    }

    /**
     * Get the singleton App instance
     * @return the app
     */
    public static App getInstance() {
        return instance;
    }

}