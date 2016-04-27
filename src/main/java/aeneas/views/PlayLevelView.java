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
import aeneas.views.BoardView.RefreshListener;
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

  private Timer timer = new Timer();
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
  private Level levelModel;
  private Model model;

  PlayLevelView(Level levelModel, Model model) {
    this.levelModel = levelModel;
    this.model = model;
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
    bullpenView = new BullpenView(model,levelModel.getBullpen(), bullpenBox, (Pane) this);

    resetLevelButton.setOnMouseClicked((e) -> {
      levelModel.reset();
    });

    bullpenView.refresh();
    levelLabel.setText("Level "+levelModel.getLevelNumber());
    
    levelTypeIcon.setGlyphName(levelModel.getIconName());

    boardView = new BoardView(this, model, levelModel.getBoard());
    boardView.setRefreshListener(this);

    VBox.setMargin(boardView, new Insets(10, 10, 10, 10));
    centerBox.setAlignment(Pos.TOP_RIGHT);
    centerBox.getChildren().add(boardView);
    
    model.setActiveLevel(levelModel);
    
    // This should be moved to a controller.
    if(levelModel instanceof LightningLevel) {
      LightningLevel l = (LightningLevel)levelModel;
      elapsedTime = 0;
      if(timer == null) timer = new Timer();
      timer.scheduleAtFixedRate(new TimerTask() {
        @Override
        public void run() {
          elapsedTime++;
          
          Platform.runLater(() -> {timeLabel.setText("Time Remaining: "+(l.getAllowedTime()-elapsedTime));});
          levelModel.stop();
          if(elapsedTime >= l.getAllowedTime()) {
            timer.cancel();
          }
        }
      }, 1000, 1000);
    }
  }
  
  public void refresh() {
    int stars = levelModel.getStarsEarned();
    model.setActiveLevel(levelModel);
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
  }

}
