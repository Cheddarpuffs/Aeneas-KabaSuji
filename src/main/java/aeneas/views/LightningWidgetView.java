package aeneas.views;

import aeneas.controllers.IMove;
import aeneas.controllers.SetMovesMove;
import aeneas.controllers.SetTimeMove;
import aeneas.models.LightningLevel;
import aeneas.models.ReleaseLevel;
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
 // private SetTimeController controller;

//  public class SetTimeController implements ChangeListener<Integer> {
//    public void changed(ObservableValue<? extends Integer> observable,
//        Integer old_value, Integer new_value) {
//      IMove move = new SetTimeMove(level, new_value);
//      if (isUserInput  && move.execute()) level.addNewMove(move);
//    }
//  }


  Spinner<Integer> timeSelect;

  private boolean isUserInput = true;


  public LightningWidgetView(LightningLevel levelModel){
    super(levelModel);

    level = levelModel;

    VBox box = new VBox();
    box.setSpacing(4);

    Label timeLabel = new Label("Time in Seconds");
    timeSelect = new Spinner<Integer>(0, 600, 30);
    timeSelect.setPrefWidth(120);
    int seconds = levelModel.getAllowedTime();
    timeSelect.getValueFactory().setValue(seconds);
    timeSelect.valueProperty().addListener((observer, old_value, new_value) -> {
      if(!isUserInput) return;
      IMove move = new SetTimeMove(level, new_value);
      if(move.execute()){
        level.addNewMove(move);
      }
    });

    //controller = new SetTimeController();
    //timeSelect.valueProperty().addListener(controller);

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
//    timeSelect.valueProperty().removeListener(controller);
//    timeSelect.getValueFactory().setValue(this.level.getAllowedTime());
//    controller = new SetTimeController();
//    timeSelect.valueProperty().addListener(controller);
    updateValues();
    return this.level;
  }

  @Override
  public RadioButton getButton() {
    return LightningWidgetView.button;
  }

  @Override
  public void updateValues(){
    isUserInput = false;
    timeSelect.getValueFactory().setValue(level.getAllowedTime());
    getButton().setSelected(true);
    isUserInput = true;
  }
}
