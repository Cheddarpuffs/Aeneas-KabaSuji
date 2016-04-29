package aeneas.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;

import aeneas.models.Level;
import aeneas.models.Model;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author Joseph Martin
 */
public class PlaySelectLevelView extends BorderPane implements Initializable {

  @FXML
  private GridPane levelGrid;

  private Model gameModel;

  @FXML
  private ScrollPane scrollpane;

  @FXML
  private GridPane customLevelGrid;

  @FXML
  private Label levelLabel;

  @FXML
  private Label customLevelLabel;

  public MainView mainView;

  private final int numCols = 5;

  PlaySelectLevelView(MainView mainView, Model model) {
    this.gameModel = model;
    this.mainView = mainView;

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("PlaySelectLevel.fxml"));
      loader.setRoot(this);
      loader.setController(this);
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param location the url to the fxml file loaded
   * @param resources resources associated with the fxml file
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    refresh();

    scrollpane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollpane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
  }

  private JFXButton makeLevelButton(int levelNumber, boolean locked) {
    JFXButton button = new JFXButton();

    button.setPrefSize(100, 100);
    button.setButtonType(ButtonType.FLAT);

    if (locked) {
      button.getStyleClass().add("locked-level-select-button");
      FontAwesomeIconView lockGraphic = new FontAwesomeIconView();
      lockGraphic.setGlyphName("LOCK");
      lockGraphic.setGlyphSize(24);
      lockGraphic.setGlyphStyle("-fx-fill:#eee");
      button.setGraphic(lockGraphic);
    } else {
      button.getStyleClass().add("level-select-button");
      button.setText(String.valueOf(levelNumber));
    }

    button.applyCss();

    return button;
  }

  private HBox makeStars(int starsEarned) {
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

  void refresh() {
    gameModel.refreshLevelIndex();
    customLevelGrid.getChildren().clear();
    levelGrid.getChildren().clear();
    for (Level level : gameModel.getLevels()) {
      int r = (level.getLevelNumber() - 1) / numCols;
      int c = (level.getLevelNumber() - 1) % numCols;
      JFXButton button = makeLevelButton(level.getLevelNumber(), gameModel.getMetadata(level).isLocked());
      button.setOnMouseClicked((e)->{
        if (!gameModel.getMetadata(level).isLocked()) {
          level.reset();
          mainView.switchToPlayLevelView(level);
        }
      });
      HBox stars = makeStars(gameModel.getMetadata(level).getStarsEarned());

      if (level.isPrebuilt()) {
        levelGrid.add(button, c, r);
        levelGrid.add(stars, c, r);
      } else {
        customLevelGrid.add(button, c, r);
        customLevelGrid.add(stars, c, r);
      }
    }
  }
}
