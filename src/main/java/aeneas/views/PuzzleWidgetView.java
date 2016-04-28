package aeneas.views;

import aeneas.models.Level;
import aeneas.controllers.IMove;
import aeneas.controllers.SetMovesMove;
import aeneas.models.Model;
import aeneas.models.PuzzleLevel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;

public class PuzzleWidgetView extends LevelWidgetView {

  static public final RadioButton button = new RadioButton("Puzzle");

  private Spinner<Integer> movesSelect;
  private Label movesLabel;
  private Model model;

  public PuzzleWidgetView(PuzzleLevel levelModel, Model model) {
    super(levelModel);
    this.model = model;

    movesLabel = new Label("Moves");
    movesSelect = new Spinner<Integer>(1, 20, 10);
    movesSelect.setPrefWidth(70);
    movesSelect.setEditable(true);
    movesSelect.getValueFactory().setValue(levelModel.getAllowedMoves());
    movesSelect.valueProperty().addListener((observer, old_value, new_value) -> {
      IMove move = new SetMovesMove(levelModel, new_value);
      if (move.execute()) model.addNewMove(move);
    });

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
  public Level getLevelModel(Level level) {
    PuzzleLevel l = new PuzzleLevel(level);
    movesSelect.getValueFactory().setValue(l.getAllowedMoves());
    movesSelect.valueProperty().addListener((observer, old_value, new_value) -> {
      l.setAllowedMoves(new_value);
    });
    return l;
  }
}
