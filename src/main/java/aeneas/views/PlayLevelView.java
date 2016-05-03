package aeneas.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXButton;

import aeneas.models.Level;
import aeneas.models.LightningLevel;
import aeneas.models.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Joseph Martin
 */
public class PlayLevelView extends BorderPane implements Initializable, RefreshListener {
  int elapsedTime = 0;
  @FXML
  private JFXButton resetLevelButton;

  @FXML
  private VBox bullpenBox;

  @FXML
  private Label levelLabel;

  @FXML
  private Label timeLabel;

  @FXML
  private VBox centerBox;

  @FXML
  private FontAwesomeIconView levelTypeIcon;

  @FXML
  private FontAwesomeIconView star1;
  @FXML
  private FontAwesomeIconView star2;
  @FXML
  private FontAwesomeIconView star3;

  private BullpenView bullpenView;
  private BoardView boardView;
  private MainView mainView;
  private Level levelModel;
  private Model model;

  PlayLevelView(Level levelModel, Model model, MainView mainView){
    this.levelModel = levelModel;
    levelModel.setRefreshListener(this);
    this.model = model;
    this.mainView = mainView;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayLevel.fxml"));
      loader.setRoot(this);
      loader.setController(this);
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    model.setActiveLevel(levelModel);
    levelModel.start();
    bullpenView = new BullpenView(model, bullpenBox, (Pane) this);
    resetLevelButton.setOnMouseClicked((e) -> {
      this.levelModel.reset();
      refresh();
    });

    bullpenView.refresh();
    bullpenView.setRefreshListener(this);
    levelLabel.setText("Level "+levelModel.getLevelNumber());

    levelTypeIcon.setGlyphName(levelModel.getIconName());

    boardView = new BoardView(this, model);
    boardView.setRefreshListener(this);

    VBox.setMargin(boardView, new Insets(10, 10, 10, 10));
    centerBox.setAlignment(Pos.TOP_RIGHT);
    centerBox.getChildren().add(boardView);

    levelModel.start();
    refresh();
  }

  public void refresh() {
    Platform.runLater(() -> {
      int stars = levelModel.getStarsEarned();
      switch(stars) {
      case 0:
        star1.setGlyphName("STAR_ALT");
        star2.setGlyphName("STAR_ALT");
        star3.setGlyphName("STAR_ALT");
        break;
      case 1:
        star1.setGlyphName("STAR");
        star2.setGlyphName("STAR_ALT");
        star3.setGlyphName("STAR_ALT");
        break;
      case 2:
        star1.setGlyphName("STAR");
        star2.setGlyphName("STAR");
        star3.setGlyphName("STAR_ALT");
        break;
      case 3:
        star1.setGlyphName("STAR");
        star2.setGlyphName("STAR");
        star3.setGlyphName("STAR");
        break;
      }
      model.updateStats();
      bullpenView.refresh();
      boardView.refresh();
      timeLabel.setText(levelModel.getCountdownText());
      if (levelModel.isFinished() || stars == 3 && levelModel.isActive()) {
        levelModel.stop();
        mainView.navigateBack();
      }
    });
  }

}
