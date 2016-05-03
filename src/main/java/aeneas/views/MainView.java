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

  private Stack<RefreshListener> paneStack;

  Stage stage;

  public MainView(Stage stage) {
    this.stage = stage;
    paneStack = new Stack<RefreshListener>();

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
      loader.setRoot(this);
      loader.setController(this);
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    //create the different types of levels
    levelViews.add(new PuzzleWidgetView(new PuzzleLevel(new Bullpen(BullpenLogic.puzzleLogic()))));
    levelViews.add(new LightningWidgetView(new LightningLevel(new Bullpen(BullpenLogic.lightningLogic()), 0)));
    levelViews.add(new ReleaseWidgetView(new ReleaseLevel(new Bullpen(BullpenLogic.releaseLogic()), new ReleaseBoard()), model));
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
    buildSelectLevelView.refresh();
    paneStack.push(buildSelectLevelView);
    content.getChildren().clear();
    content.getChildren().add(buildSelectLevelView);
  }

  public void switchToBuildLevelView(Level level) {
    BuildLevelView buildLevelView = new BuildLevelView(this, levelViews, level, model);
    paneStack.push(buildLevelView);
    content.getChildren().clear();
    content.getChildren().add(buildLevelView);

  }

  public void switchToPlayLevelView(Level level) {
    PlayLevelView playLevelView = new PlayLevelView(level, model, this);
    paneStack.push(playLevelView);
    content.getChildren().clear();
    content.getChildren().add(playLevelView);
  }

  public void switchToPlaySelectLevelView() {
    playSelectLevelView.refresh();
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
    buildSelectLevelView = new BuildSelectLevelView(this, model);

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

    this.setOnDragDropped((e) -> {
      model.getLatestDragSource().returnDraggableNode();
    });
    this.setOnDragExited((e) -> {
    });

    // yes, we need this
    this.setOnDragOver((DragEvent event) -> {
      event.acceptTransferModes(TransferMode.MOVE);
      event.consume();
    });

    switchToWelcomeView();
  }

  public File showSaveDialog(int levelNumber) {
    FileChooser fileChooser = new FileChooser();
    if(levelNumber > 0) {
      fileChooser.setInitialFileName(levelNumber+".kbs");
    }
    fileChooser.setInitialDirectory(model.getLevelIndex().defaultLevelPath.toFile());
    fileChooser.setTitle("Save Level");
    return fileChooser.showSaveDialog(stage);
  }

  public File showOpenDialog() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(model.getLevelIndex().defaultLevelPath.toFile());
    fileChooser.setTitle("Open Existing Level");
    return fileChooser.showOpenDialog(stage);
  }

  void navigateBack() {
    // unless we're out of places to go back, go at the last pane we
    // the current node should always be in the stack,
    // so only remove and go back if there's multiple things on the stack
    model.setActiveLevel(null);
    if (paneStack.size() > 1) {
      paneStack.pop();
      RefreshListener pane = paneStack.peek();
      pane.refresh();
      content.getChildren().clear();
      content.getChildren().add((Node)pane);
    }
  }

  public Model getModel() { return model; }

}
