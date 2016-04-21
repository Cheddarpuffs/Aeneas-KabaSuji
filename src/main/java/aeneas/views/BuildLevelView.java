package aeneas.views;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;

import aeneas.controllers.SaveLevelController;
import aeneas.controllers.SetMovesMove;
import aeneas.controllers.SetTimeMove;
import aeneas.models.Bullpen;
import aeneas.models.Level;
import aeneas.models.Level.LevelType;
import aeneas.models.Level.LevelWithMoves;
import aeneas.models.LightningLevel;
import aeneas.models.Model;
import aeneas.models.Piece;
import aeneas.models.PuzzleLevel;
import aeneas.models.ReleaseLevel;
import aeneas.models.Square;
import aeneas.controllers.ChangeLevelTypeMove;
import aeneas.controllers.IMove;
import aeneas.controllers.SaveLevelController;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Display the level builder.
 *
 * @author pmitrano
 * @author jbkuszmaul
 */
public class BuildLevelView extends BorderPane implements Initializable {

  @FXML
  private JFXListView<Pane> bullpenListView;

  @FXML
  private Label levelLabel;

  @FXML
  private FontAwesomeIconView levelTypeIcon;

  @FXML
  private VBox bullpenBox;

  @FXML
  private VBox centerBox;

  @FXML
  private JFXButton saveButton;

  @FXML
  private ToggleGroup levelType;

  @FXML
  private RadioButton puzzleRadio;
  @FXML
  private RadioButton lightningRadio;
  @FXML
  private RadioButton releaseRadio;

  @FXML
  private JFXDatePicker timerSelect;

  @FXML
  private Spinner<Double> movesSelect;

  @FXML
  private Label movesLabel;

  BoardView boardView;
  Model model;
  private Level levelModel;
  MainView mainView;
  BullpenView bullpenView;

  BuildLevelView(MainView mainView, Level levelModel, Model model) {
    this.model = model;
    this.levelModel = levelModel;
    this.mainView = mainView;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("BuildLevel.fxml"));
      loader.setRoot(this);
      loader.setController(this);
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Associate RadioButtons with puzzle types.
    puzzleRadio.setUserData(Level.LevelType.PUZZLE);
    releaseRadio.setUserData(Level.LevelType.RELEASE);
    lightningRadio.setUserData(Level.LevelType.LIGHTNING);

    bullpenView = new BullpenView(bullpenBox, this);

    Piece testPiece = new Piece(new Square[] {
        new Square(0, 0),
        new Square(1, 0),
        new Square(1, 1),
        new Square(1, 2),
        new Square(1, 3),
        new Square(1, 4),
    });

    bullpenView.addPiece(testPiece, model);

    boardView = new BoardView(levelModel.getBoard());
    VBox.setMargin(boardView, new Insets(10, 10, 10, 10));
    centerBox.setAlignment(Pos.TOP_RIGHT);
    centerBox.getChildren().add(boardView);

    saveButton.setOnMouseClicked(
        new SaveLevelController(mainView));

    // Handle changes in the level type.
    // TODO: Consider moving this to a separate class.
    levelType.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov,
                Toggle toggle, Toggle new_toggle) -> {
      LevelType type = (LevelType)((RadioButton)new_toggle).getUserData();
      IMove changeMove = new ChangeLevelTypeMove(this.getLevel(), this, type);
      if (changeMove.execute()) {
        model.addNewMove(changeMove);
      }

      IMove move;
      if (getLevel() instanceof LevelWithMoves) {
        move = new SetMovesMove((LevelWithMoves)getLevel(), allowedSeconds());
      } else {
        move = new SetTimeMove((LightningLevel)getLevel(), allowedSeconds());
      }
      if (move.execute()) {
        model.addNewMove(move);
      }
    });

    // Update the level options whenever the movesSelect spinner
    // is updated.
    movesSelect.valueProperty().addListener((obs, oldValue, newValue) -> {
      IMove move =
        new SetMovesMove((LevelWithMoves)getLevel(), getMovesAllowed());
      if (move.execute()) {
        model.addNewMove(move);
      }
    });

    // Update the level time whenever the timerSelect DatePicker is used.
    timerSelect.setOnAction((e) -> {
      IMove move = new SetTimeMove((LightningLevel)getLevel(), allowedSeconds());
      if (move.execute()) {
        model.addNewMove(move);
      }
    });
  }

  /**
   * Sets the current level for the builder.
   *
   * This will update the current options selectors based on the new level.
   * @param level The new level to set; if null, a new default level will be
   *    put in.
   */
  public void setLevel(Level level) {
    if (level != null) {
      levelModel = level;
    } else {
      // Reset with defaults, level should NEVER be null.
      levelModel = new LightningLevel(new Bullpen(), 0);
    }

    boolean showMoves = levelModel instanceof LevelWithMoves;
    timerSelect.setVisible(!showMoves);
    movesSelect.setVisible(showMoves);
    movesLabel.setVisible(showMoves);

    switch (levelModel.getLevelType()) {
      case LIGHTNING:
        int time = ((LightningLevel)levelModel).getAllowedTime();
        // TODO: Currently, the DatePicker will have the correct internal state
        // after this, however the time you see (eg, "12:00am") will be wrong.
        timerSelect.setTime(LocalTime.of(time / 60, time % 60));
        levelType.selectToggle(lightningRadio);
        break;
      case PUZZLE:
        movesSelect.valueFactoryProperty().getValue().setValue(
            new Double(((PuzzleLevel)levelModel).getAllowedMoves()));
        levelType.selectToggle(puzzleRadio);
        break;
      case RELEASE:
        movesSelect.valueFactoryProperty().getValue().setValue(
            new Double(((ReleaseLevel)levelModel).getAllowedMoves()));
        levelType.selectToggle(releaseRadio);
        break;
    }
  }

  public Level getLevel() {
    return levelModel;
  }

  /**
   * Retrieve the number of allowed moves from the Spinner.
   */
  public int getMovesAllowed() {
    return movesSelect.getValue().intValue();
  }

  /**
   * Retrieve the number of allowed seconds from the DatePicker.
   */
  public int allowedSeconds() {
    LocalTime time = timerSelect.getTime();
    return time.getHour() * 60 + time.getMinute();
  }
}
