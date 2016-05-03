package aeneas.views;

import aeneas.controllers.IMove;
import aeneas.controllers.SetMovesMove;
import aeneas.models.Level;
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
  private PuzzleLevel level;
  private boolean isUserInput = true;

  public PuzzleWidgetView(PuzzleLevel levelModel) {
    super(levelModel);
    this.level = levelModel;

    level = levelModel;
    movesLabel = new Label("Moves");
    movesSelect = new Spinner<Integer>(1, 999, 10);
    movesSelect.setPrefWidth(70);
    movesSelect.setEditable(true);
    movesSelect.getValueFactory().setValue(levelModel.getAllowedMoves());
    movesSelect.valueProperty().addListener((observer, old_value, new_value) -> {
      if(!isUserInput) return;
      IMove move = new SetMovesMove(level, new_value);
      if(move.execute()){
        level.addNewMove(move);
      }
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
  public void updateValues(){
    isUserInput = false;
    getButton().setSelected(true);
    movesSelect.getValueFactory().setValue(level.getAllowedMoves());
    isUserInput = true;
  }

  public Level resetLevelModel(Level level) {
    if (level instanceof PuzzleLevel) {
      this.level = (PuzzleLevel)level;
      return level;
    }
    this.level = new PuzzleLevel(level);
    movesSelect.valueProperty().addListener((observer, old_value, new_value) -> {
      if(!isUserInput) return;
      IMove move = new SetMovesMove(this.level, new_value);
      if(move.execute()){
        level.addNewMove(move);
      }
    });
    updateValues();
    return this.level;
  }

}
