package aeneas.views;

import aeneas.models.Level;
import aeneas.models.Level.LevelWithMoves;
import aeneas.controllers.IMove;
import aeneas.controllers.SetMovesMove;
import aeneas.models.Model;
import aeneas.models.PuzzleLevel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;

public class PuzzleWidgetView extends LevelWidgetView {

  static public final RadioButton button = new RadioButton("Puzzle");

  private Spinner<Integer> movesSelect;
  private SetMovesController controller;
  private Label movesLabel;
  private Model model;
  private PuzzleLevel level;

  public PuzzleWidgetView(PuzzleLevel levelModel, Model model) {
    super(levelModel);
    this.model = model;
    this.level = levelModel;

    movesLabel = new Label("Moves");
    movesSelect = new Spinner<Integer>(1, 20, 10);
    movesSelect.setPrefWidth(70);
    movesSelect.setEditable(true);
    movesSelect.getValueFactory().setValue(levelModel.getAllowedMoves());
    controller = new SetMovesController(level, model);
    movesSelect.valueProperty().addListener(controller);

    HBox hbox = new HBox();
    hbox.setSpacing(5);
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.getChildren().add(movesLabel);
    hbox.getChildren().add(movesSelect);

    panel.getChildren().add(hbox);

    button.setUserData(this);
  }

  @Override
  public RadioButton getButton() {
    return PuzzleWidgetView.button;
  }

  @Override
  public Level resetLevelModel(Level level) {
    if (level instanceof PuzzleLevel) {
      this.level = (PuzzleLevel)level;
      return level;
    }
    this.level = new PuzzleLevel(level);
    movesSelect.valueProperty().removeListener(controller);
    movesSelect.getValueFactory().setValue(this.level.getAllowedMoves());
    controller = new SetMovesController(this.level, model);
    movesSelect.valueProperty().addListener(controller);
    return this.level;
  }

  @Override
  public void refresh() {
    // Don't let the listener get called while refreshing.
    movesSelect.valueProperty().removeListener(controller);
    movesSelect.getValueFactory().setValue(level.getAllowedMoves());
    movesSelect.valueProperty().addListener(controller);
    getButton().setSelected(true);
  }
}
