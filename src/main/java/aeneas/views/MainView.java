package aeneas.views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;
import com.jfoenix.controls.JFXRippler;

import aeneas.controllers.ViewAboutController;
import aeneas.controllers.ViewHelpController;
import aeneas.models.Bullpen;
import aeneas.models.Level;
import aeneas.models.Model;
import aeneas.models.PuzzleLevel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

public class MainView extends StackPane implements Initializable {

  @FXML
  public StackPane root;

  @FXML
  private StackPane content;

  @FXML
  private StackPane optionsBurger;

  @FXML
  private JFXRippler optionsRippler;

  @FXML
  private Label about;

  @FXML
  private Label help;

  @FXML
  private JFXPopup toolbarPopup;

  @FXML
  private JFXButton back;

  @FXML
  private JFXDialog dialog;

  @FXML private JFXDialogLayout dialogLayout;

  @FXML
  private JFXButton accept;


  private ViewAchievementsView viewAchievementsView;
  private WelcomeView welcomeView;
  private PlaySelectLevelView playSelectLevelView;
  private BuildSelectLevelView buildSelectLevelView;
  private PlayLevelView playLevelView;
  private BuildLevelView buildLevelView;
  private Model model;

  private Stack<Node> paneStack;

  Stage stage;

  public MainView(Stage stage) {

    this.stage = stage;
    paneStack = new Stack<Node>();

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
      loader.setRoot(this);
      loader.setController(this);
      loader.load();
    } catch (IOException e){
      e.printStackTrace();
    }
  }

  public BuildLevelView getBuildLevelView() {
    return buildLevelView;
  }

  public void switchToWelcomeView() {
    paneStack.push(welcomeView);
    content.getChildren().clear();
    content.getChildren().add(welcomeView);
  }

  public void switchToViewAchievementsView() {
    paneStack.push(viewAchievementsView);
    content.getChildren().clear();
    content.getChildren().add(viewAchievementsView);
  }

  public void switchToBuildSelectLevelView() {
    paneStack.push(buildSelectLevelView);
    content.getChildren().clear();
    content.getChildren().add(buildSelectLevelView);
  }

  public void switchToBuildLevelView() {
    paneStack.push(buildLevelView);
    content.getChildren().clear();
    content.getChildren().add(buildLevelView);

  }

  public void switchToPlayLevelView() {
    paneStack.push(playLevelView);
    content.getChildren().clear();
    content.getChildren().add(playLevelView);
  }

  public void switchToPlaySelectLevelView() {
    paneStack.push(playSelectLevelView);
    content.getChildren().clear();
    content.getChildren().add(playSelectLevelView);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    model = new Model();

    welcomeView = new WelcomeView(this, model);
    playSelectLevelView= new PlaySelectLevelView(this, model);

    Bullpen bullpen = new Bullpen(new ArrayList<>());
    Level l = new PuzzleLevel(bullpen);
    playLevelView = new PlayLevelView(this, l);
    buildLevelView = new BuildLevelView(this, l);
    viewAchievementsView = new ViewAchievementsView(model);
    buildSelectLevelView= new BuildSelectLevelView(this, model);

    // init Popup
    toolbarPopup.setPopupContainer(root);
    toolbarPopup.setSource(optionsRippler);
    optionsBurger.setOnMouseClicked((e) -> {
      toolbarPopup.show(PopupVPosition.TOP, PopupHPosition.RIGHT, -12, 5);
    });
    back.setOnMouseClicked((e) -> {
      // unless we're out of places to go back, go at the last pane we
      // the current node should always be in the stack,
      // so only remove and go back if there's multiple things on the stack
      if (paneStack.size() > 1){
        paneStack.pop();
        content.getChildren().clear();
        content.getChildren().add(paneStack.peek());
      }
    });

    dialog.setTransitionType(DialogTransition.CENTER);

    help.setOnMouseClicked((e) -> {
    });

    accept.setOnMouseClicked((e) -> {
      dialog.close();
    });

    // we need to add these back eventually
    help.setOnMouseClicked(new ViewHelpController(this, dialog,
          dialogLayout, model.helpString));
    about.setOnMouseClicked(new ViewAboutController(this, dialog,
          dialogLayout, model.aboutString));

    switchToWelcomeView();
  }

  public File showSaveDialog() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Level");
    return fileChooser.showSaveDialog(stage);
  }

  public File showOpenDialog() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Existing Level");
    return fileChooser.showOpenDialog(stage);
  }

}
