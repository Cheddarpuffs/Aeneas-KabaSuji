package kabasuji.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import kabasuji.controllers.SelectLevelController;
import kabasuji.models.Level;
import kabasuji.models.Model;

public class PlaySelectLevelView extends BorderPane implements Initializable {

  @FXML
  private GridPane levelGrid;

  private Model gameModel;

  @FXML
  private GridPane customLevelGrid;

  public MainView parentView;

  private final int numCols = 5;

  PlaySelectLevelView(MainView parentView, Model model) {
    this.gameModel = model;
    this.parentView = parentView;

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/PlaySelectLevel.fxml"));
      loader.setRoot(this);
      loader.setController(this);
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    for (Level level : gameModel.levels){
      int r = (level.levelNumber - 1) / numCols;
      int c = (level.levelNumber - 1) % numCols;
      JFXButton button = makeLevelButton(level.levelNumber, level.isLocked());
      button.setOnMouseClicked(new SelectLevelController(this, level));
      HBox stars = makeStars(level.starsEarned);

      if (level.isPrebuilt()) {
        levelGrid.add(button, c, r);
        levelGrid.add(stars, c, r);
      }
      else {
        customLevelGrid.add(button, c, r);
        customLevelGrid.add(stars, c, r);
      }
    }
  }

  private JFXButton makeLevelButton(int levelNumber, boolean locked){
      JFXButton button = new JFXButton();

      button.setPrefSize(100, 100);
      button.setButtonType(ButtonType.FLAT);

      if (locked){
        button.getStyleClass().add("locked-level-select-button");
        button.setText("?");
      }
      else {
        button.getStyleClass().add("level-select-button");
        button.setText(String.valueOf(levelNumber));
      }

      button.applyCss();

      return button;
  }

  private HBox makeStars(int starsEarned){
    HBox stars = new HBox();
    stars.setMouseTransparent(true);
    stars.setAlignment(Pos.BOTTOM_CENTER);
    stars.setSpacing(2);

    for (int i = 0; i < starsEarned; i++) {
      FontAwesomeIconView star = new FontAwesomeIconView();
      star.setGlyphName("STAR");
      star.setSize("2em");
      star.setGlyphStyle("-fx-fill:#FFC107");
      stars.getChildren().add(star);
    }

    return stars;
  }
}
