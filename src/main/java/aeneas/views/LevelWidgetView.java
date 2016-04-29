package aeneas.views;

import aeneas.controllers.IMove;
import aeneas.controllers.SetMovesMove;
import aeneas.models.Level;
import aeneas.models.Level.LevelWithMoves;
import aeneas.models.Model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;

public abstract class LevelWidgetView {

  public class SetMovesController implements ChangeListener<Integer> {
    Model model;
    LevelWithMoves level;
    public SetMovesController(LevelWithMoves level, Model model) {
      this.level = level;
      this.model = model;
    }

    public void changed(ObservableValue<? extends Integer> observable,
                        Integer old_value, Integer new_value) {
      IMove move = new SetMovesMove(level, new_value);
      if (move.execute()) this.model.addNewMove(move);
    }

  }

  protected VBox panel;
  private Level levelModel;

  public LevelWidgetView(Level levelModel){
    this.levelModel = levelModel;
    panel = new VBox();
    panel.setAlignment(Pos.TOP_LEFT);
    panel.setSpacing(10);
    panel.setMinWidth(125);
  }

  /**
   * @return the panel
   */
  public VBox getPanel() {
    return panel;
  }

  /**
   * @return the levelModel. If levelModel is already of the correct type, then
   *   levelModel will be returned.
   */
  public abstract Level resetLevelModel(Level levelModel);

  public Level getDefaultLevelModel() {
    return this.levelModel;
  }

  public abstract RadioButton getButton();

  public abstract void refresh();
}
