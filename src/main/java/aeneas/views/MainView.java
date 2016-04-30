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

import aeneas.models.Bullpen;
import aeneas.models.Bullpen.BullpenLogic;
import aeneas.models.Level;
import aeneas.models.LightningLevel;
import aeneas.models.Model;
import aeneas.models.PuzzleLevel;
import aeneas.models.ReleaseBoard;
import aeneas.models.ReleaseLevel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Joseph Martin
 */
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

  @FXML
  private JFXDialogLayout dialogLayout;

  @FXML
  private JFXButton accept;

  private ViewAchievementsView viewAchievementsView;
  private WelcomeView welcomeView;
  private PlaySelectLevelView playSelectLevelView;
  private BuildSelectLevelView buildSelectLevelView;
  private Model model;
  private ArrayList<LevelWidgetView> levelViews = new ArrayList<LevelWidgetView>();

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
    } catch (IOException e) {
      e.printStackTrace();
    }

    // create the different types of levels
    levelViews.add(new PuzzleWidgetView(new PuzzleLevel(new Bullpen(BullpenLogic.puzzleLogic()))));
    levelViews.add(new LightningWidgetView(new LightningLevel(new Bullpen(BullpenLogic.lightningLogic()), 0)));
    levelViews.add(new ReleaseWidgetView(new ReleaseLevel(new Bullpen(BullpenLogic.releaseLogic()), new ReleaseBoard(null))));
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

  public void switchToBuildLevelView(LevelWidgetView levelView) {
    BuildLevelView buildLevelView = new BuildLevelView(this, levelViews, levelView, model);
    paneStack.push(buildLevelView);
    content.getChildren().clear();
    content.getChildren().add(buildLevelView);

  }

  public void switchToPlayLevelView(LevelWidgetView levelView) {
    PlayLevelView playLevelView = new PlayLevelView(levelView, model);
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
    playSelectLevelView = new PlaySelectLevelView(this, model);
    viewAchievementsView = new ViewAchievementsView(model);
    buildSelectLevelView = new BuildSelectLevelView(this);

    // init Popup
    toolbarPopup.setPopupContainer(root);
    toolbarPopup.setSource(optionsRippler);
    optionsBurger.setOnMouseClicked((e) -> {
      toolbarPopup.show(PopupVPosition.TOP, PopupHPosition.RIGHT, -12, 5);
    });

    back.setOnMouseClicked((e) -> {
      navigateBack();
    });

    dialog.setTransitionType(DialogTransition.CENTER);

    accept.setOnMouseClicked((e) -> {
      dialog.close();
    });

    // we need to add these back eventually
    help.setOnMouseClicked((e) -> {
      dialogLayout.getHeading().clear();
      dialogLayout.getBody().clear();
      dialogLayout.setHeading(new Label("Help"));
      dialogLayout.setBody(new Label(Model.helpText));
      dialog.show(this);
    });
    about.setOnMouseClicked((e) -> {
      dialogLayout.getHeading().clear();
      dialogLayout.getBody().clear();
      dialogLayout.setHeading(new Label("About"));
      dialogLayout.setBody(new Label(Model.aboutText));
      dialog.show(this);
    });

    content.setOnDragExited((e) -> {
      // eventually return a piece to where it came
      // this case is tough because this also fires on valid drops
    });

    this.setOnDragDropped((e) -> {
        model.getLatestDragSource().returnPiece();
    });

    // yes, we need this
    this.setOnDragOver((DragEvent event) -> {
      event.acceptTransferModes(TransferMode.MOVE);
      event.consume();
    });

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

  void navigateBack() {
    // unless we're out of places to go back, go at the last pane we
    // the current node should always be in the stack,
    // so only remove and go back if there's multiple things on the stack
    if (paneStack.size() > 1) {
      paneStack.pop();
      content.getChildren().clear();
      content.getChildren().add(paneStack.peek());
    }
  }

}
