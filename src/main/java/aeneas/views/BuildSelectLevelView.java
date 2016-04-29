package aeneas.views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.effects.JFXDepthManager;

import aeneas.models.Bullpen;
import aeneas.models.Bullpen.BullpenLogic;
import aeneas.models.Level;
import aeneas.models.PuzzleLevel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class BuildSelectLevelView extends BorderPane implements Initializable {

  @FXML
  private Label createNewLevelLabel;

  @FXML
  private JFXListView<Label> fileList;

  @FXML
  private JFXButton openFile;

  @FXML
  private JFXButton editLevel;

  private MainView mainView;
  private Level levelToSwitchTo;
  private HashMap<String, Level> levelMap = new HashMap<>();

  BuildSelectLevelView(MainView mainView) {
    this.mainView = mainView;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("BuildSelectLevel.fxml"));
      loader.setRoot(this);
      loader.setController(this);
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private LevelWidgetView createDefaultLevelView() {
    Bullpen defaultBullpen  = new Bullpen(BullpenLogic.puzzleLogic());
    PuzzleLevel defaultLevel = new PuzzleLevel(defaultBullpen);
    return new PuzzleWidgetView(defaultLevel, mainView.getModel());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    fileList.setOnMouseClicked((e) -> {
      Label l = fileList.getSelectionModel().getSelectedItem();

      if (l != null) {
        String path = l.getText();

        if (path.equals(createNewLevelLabel.getText())) {
          levelToSwitchTo = createDefaultLevelView().getDefaultLevelModel();
          mainView.switchToBuildLevelView(levelToSwitchTo);
        } else {
          levelToSwitchTo = levelMap.get(path);

          if (levelToSwitchTo == null) {
            System.out.println("couldn't find file name " + path);
          }
        }
      }
    });

    editLevel.setOnMouseClicked((e) -> {
      mainView.switchToBuildLevelView(levelToSwitchTo);
    });

    openFile.setOnMouseClicked((e) -> {
      File loadFile = mainView.showOpenDialog();
      if (loadFile == null)
        return;
      try {
        levelToSwitchTo = Level.loadLevel(loadFile);
        String path = loadFile.getAbsolutePath();
        levelMap.put(path, levelToSwitchTo);
        this.fileList.getItems().add(new Label(path));
      } catch (IOException i) {
        i.printStackTrace(System.out);
        System.out.println("Error occurred opening file.");
      }
    });

    JFXDepthManager.setDepth(fileList, 1);

  }

}
