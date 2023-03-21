package uk.ac.soton.comp2211.scenes;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.ui.AppWindow;
import uk.ac.soton.comp2211.ui.GamePane;
import uk.ac.soton.comp2211.utility.SettingsManager;
import javafx.scene.layout.Border;
import javafx.geometry.Insets;
import uk.ac.soton.comp2211.App;

public class SettingsScene extends BaseScene{

  private static final Logger logger = LogManager.getLogger(SettingsScene.class);


  /**
   * Create a new scene, passing in the AppWindow the scene will be displayed in
   *
   * @param appWindow the game window
   */
  public SettingsScene(AppWindow appWindow) {
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
    var stackPane = new StackPane();
    stackPane.setMaxWidth(appWindow.getWidth());
    stackPane.setMaxHeight(appWindow.getHeight());
    stackPane.getStyleClass().add("menu-background");
    root.getChildren().add(stackPane);

    var menuButtons = new VBox();
    var menuButtonsBack = new VBox();

    // Label and buttons for the font size
    var fontSizeText = new Label("Set Font Size:");
    var defaultFontButton = new Button("Default");
    var bigFontButton = new Button("Big");
    var backButton = new Button("Back");
    var fontButtons = new HBox();
    fontSizeText.setTextAlignment(TextAlignment.CENTER);
    fontButtons.getChildren().addAll(defaultFontButton, bigFontButton);
    fontButtons.setAlignment(Pos.CENTER);
    fontButtons.setSpacing(10);
    
    
    // Label and buttons for the theme
    var themeLabel = new Label("Set Theme:");
    var defaultTheme = new Button("Default");
    var darkTheme = new Button("Dark");
    var lightTheme = new Button("Light");
    var themeButtons = new HBox();
    themeLabel.getStyleClass().add("heading");
    themeLabel.setTextAlignment(TextAlignment.CENTER);
    themeButtons.getChildren().addAll(defaultTheme, darkTheme, lightTheme);
    themeButtons.setAlignment(Pos.CENTER);
    themeButtons.setSpacing(10);

    //Label and button for the information popup
    var infoLabel = new Label("View information");
    var infoButton = new Button("Info");
    var infoBox = new HBox();
    infoLabel.getStyleClass().add("heading");
    infoLabel.setTextAlignment(TextAlignment.CENTER);
    infoBox.getChildren().add(infoButton);
    infoBox.setAlignment(Pos.CENTER);


    //Display buttons
    menuButtons.getChildren().addAll(fontSizeText, fontButtons, themeLabel, themeButtons, infoLabel, infoBox);
    menuButtons.setAlignment(Pos.CENTER);
    menuButtons.setSpacing(10);

    menuButtonsBack.getChildren().addAll(menuButtons, backButton);
    menuButtonsBack.setAlignment(Pos.CENTER);
    menuButtonsBack.setMaxWidth(appWindow.getWidth());
    menuButtonsBack.setMaxHeight(appWindow.getHeight());
    menuButtonsBack.setSpacing(150);

    stackPane.getChildren().addAll(menuButtonsBack);

    backButton.setOnAction(event -> {
      appWindow.startMenu();
    });

    defaultFontButton.setOnAction(event -> {
      if(SettingsManager.isBigFontEnabled()) {
        SettingsManager.enableDefaultFont(scene);
        logger.info("User set default font");
      }
    });

    bigFontButton.setOnAction(event -> {
      if(!SettingsManager.isBigFontEnabled()) {
        SettingsManager.enableBigFont(scene);
        logger.info("User set big font");
      }
    });

    defaultTheme.setOnAction(event -> {
      if(!SettingsManager.isDefaultThemeEnabled()) {
        SettingsManager.enableDefaultTheme(scene);
        logger.info("User set default theme");
      }
    });

    darkTheme.setOnAction(event -> {
      if(!SettingsManager.isDarkThemeEnabled()) {
        SettingsManager.enableDarkTheme(scene);
        logger.info("User set dark theme");
      }
    });

    lightTheme.setOnAction(event -> {
      if(!SettingsManager.isLightThemeEnabled()) {
        SettingsManager.enableLightTheme(scene);
        logger.info("User set light theme");
      }
    });

    infoButton.setOnAction(event -> {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Metric Information");
      alert.setContentText("This popup intends to give detail to the terms used in the program to aid the user in their understanding.\n" +
              "\n" +
              "bounceCount: The Bounce Count is the tally of how many users clicked on an Advert and then did not interact with the landing website. The user either stayed on the landing page for only a short time, or only visited a single page\n" +
              "\n" +
              "bounceRate: How many bounces happen per click\n" +
              "\n" +
              "clickCount: Tally of how many times users clicked on an Advert\n" +
              "\n" +
              "conversionCount: Tally of how many users clicked on an Advert and then performed a desired action. The desired action depends on the campaign but it could include making a purchase, or signing up for a newsletter.\n" +
              "\n" +
              "CPA: CPA stands for Cost per Acquisition, which is calculated by taking the average amount spent per successful conversion\n" +
              "\n" +
              "CPC: CPC stands for Cost per Click, calculated by taking the average amount spent per successful click\n" +
              "\n" +
              "CPM: CPM stands for Cost per Thousand Impressions. An impression occurs when a user is shown an Advert, and is counted regardless of whether it is clicked or not. The CPM is calculated by taking the average amount spent per 1000 impressions\n" +
              "\n" +
              "CTR: CTR stands for Click Through Rate. This is the average number of clicks per impression\n" +
              "\n" +
              "impressionNum: Amount of impressions\n" +
              "\n" +
              "totalCost: Total amount spent\n" +
              "\n" +
              "uniquesCount: Tally of how many unique users perform a click\n" +
              "\n" +
              "Total Click Cost: Cost of all clicks in a campaign");
      alert.show();
    });

  }

}
