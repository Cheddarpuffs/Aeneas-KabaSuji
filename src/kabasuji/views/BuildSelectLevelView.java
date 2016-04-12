package kabasuji.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.effects.JFXDepthManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import kabasuji.models.Model;

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

  private MainView parentView;

  BuildSelectLevelView(MainView parentView, Model model) {
    this.model = model;
    this.parentView = parentView;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/BuildSelectLevel.fxml"));
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
      fileChooser.setTitle("Open Existing Level");
      fileChooser.showOpenDialog(parentView.stage);
    });

    createNewLevelLabel.setOnMouseClicked((e) -> {
      parentView.switchToBuildLevelView();
    });

    JFXDepthManager.setDepth(fileList, 1);

  }

}
