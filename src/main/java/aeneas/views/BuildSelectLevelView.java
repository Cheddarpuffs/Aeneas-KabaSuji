package aeneas.views;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.effects.JFXDepthManager;

import aeneas.models.Model;
import aeneas.models.Level;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

public class BuildSelectLevelView extends BorderPane implements Initializable {

  @FXML
  private Label createNewLevelLabel;
  @FXML
  private JFXListView<Label> fileList;

  @FXML
  private JFXButton openFile;

  @FXML
  private JFXButton editLevel;

  private Model model;

  private MainView mainView;

  BuildSelectLevelView(MainView mainView, Model model) {
    this.model = model;
    this.mainView = mainView;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("BuildSelectLevel.fxml"));
      loader.setRoot(this);
      loader.setController(this);
      loader.load();
    }
    catch (IOException e){
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    fileList.setOnMouseClicked((e) -> {
      System.out.println("selected " + fileList.getSelectionModel().getSelectedItem());
    });

    editLevel.setOnMouseClicked((e) -> {
      mainView.switchToBuildLevelView();
    });

    openFile.setOnMouseClicked((e) -> {
      File loadFile = mainView.showOpenDialog();
      if (loadFile == null) return;
      try {
        Level loadLevel = Level.loadLevel(loadFile);
        mainView.getBuildLevelView().setLevel(loadLevel);
      } catch (IOException i) {
        System.out.println("Error occurred opening file.");
      }
    });

    createNewLevelLabel.setOnMouseClicked((e) -> {
      mainView.getBuildLevelView().setLevel(null);
      mainView.switchToBuildLevelView();
    });

    JFXDepthManager.setDepth(fileList, 1);

  }

}
