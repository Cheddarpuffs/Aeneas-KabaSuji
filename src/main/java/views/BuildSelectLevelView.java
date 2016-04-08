package main.java.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.effects.JFXDepthManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import main.java.models.Model;

public class BuildSelectLevelView extends BorderPane implements Initializable {

  @FXML
  private JFXListView fileList;

  @FXML
  private JFXButton openFile;

  @FXML
  private JFXButton editLevel;

  private Model model;

  private MainView parentView;

  BuildSelectLevelView(MainView parentView, Model model) {
    this.model = model;
    this.parentView = parentView;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/fxml/BuildSelectLevel.fxml"));
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
      parentView.switchToBuildLevelView();
    });

    openFile.setOnMouseClicked((e) -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Resource File");
      fileChooser.showOpenDialog(parentView.stage);
    });

    JFXDepthManager.setDepth(fileList, 1);

  }

}
