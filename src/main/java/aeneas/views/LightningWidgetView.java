package aeneas.views;

import aeneas.controllers.IMove;
import aeneas.controllers.SetTimeMove;
import aeneas.models.Level;
import aeneas.models.LightningLevel;
import aeneas.models.Model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;

public class LightningWidgetView extends LevelWidgetView {

  public static final RadioButton button = new RadioButton("Lightning");
  private Model model;
  private LightningLevel level;
  private SetTimeController controller;

  public class SetTimeController implements ChangeListener<Integer> {
    public void changed(ObservableValue<? extends Integer> observable,
                        Integer old_value, Integer new_value) {
      IMove move = new SetTimeMove(level, new_value);
      if (move.execute()) model.addNewMove(move);
    }
  }

  Spinner<Integer> timeSelect;

  public LightningWidgetView(LightningLevel levelModel, Model model) {
    super(levelModel);
    this.model = model;
    this.level = levelModel;

    VBox box = new VBox();
    box.setSpacing(4);

    Label timeLabel = new Label("Time in Seconds");
    timeSelect = new Spinner<Integer>(0, 600, 30);
    timeSelect.setPrefWidth(120);
    int seconds = levelModel.getAllowedTime();
    timeSelect.getValueFactory().setValue(seconds);

    controller = new SetTimeController();
    timeSelect.valueProperty().addListener(controller);

    box.getChildren().add(timeLabel);
    box.getChildren().add(timeSelect);

    panel.getChildren().add(box);

    button.setUserData(this);
  }

  public Level resetLevelModel(Level level) {
    if (level instanceof LightningLevel) {
      this.level = (LightningLevel)level;
      return level;
    }
    this.level = new LightningLevel(level);
    timeSelect.valueProperty().removeListener(controller);
    timeSelect.getValueFactory().setValue(this.level.getAllowedTime());
    controller = new SetTimeController();
    timeSelect.valueProperty().addListener(controller);
    return this.level;
  }

  @Override
  public RadioButton getButton() {
    return LightningWidgetView.button;
  }

  @Override
  public void refresh() {
    timeSelect.valueProperty().removeListener(controller);
    timeSelect.getValueFactory().setValue(level.getAllowedTime());
    timeSelect.valueProperty().addListener(controller);
    getButton().setSelected(true);
  }
}
