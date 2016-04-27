package aeneas.views;

import aeneas.models.Level;
import aeneas.models.LightningLevel;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;

public class LightningWidgetView extends LevelWidgetView {

  private static final RadioButton button = new RadioButton("Lightning");

  Spinner<Integer> timeSelect;
  
  public LightningWidgetView(LightningLevel levelModel){
    super(levelModel);

    VBox box = new VBox();
    box.setSpacing(4);

    Label timeLabel = new Label("Time in Seconds");
    timeSelect = new Spinner<Integer>(0, 600, 30);
    timeSelect.setPrefWidth(120);
    int seconds = levelModel.getAllowedTime();
    timeSelect.getValueFactory().setValue(seconds);

    timeSelect.valueProperty().addListener((observer, old_seconds, new_seconds) -> {
      levelModel.setAllowedTime(new_seconds);
    });

    box.getChildren().add(timeLabel);
    box.getChildren().add(timeSelect);

    panel.getChildren().add(box);

    button.setUserData(this);
  }
  
  public Level getLevelModel(Level level) {
    LightningLevel l = new LightningLevel(level);
    timeSelect.getValueFactory().setValue(l.getAllowedTime());
    timeSelect.valueProperty().addListener((observer, old_seconds, new_seconds) -> {
      l.setAllowedTime(new_seconds);
    });
    return l;
  }

  @Override
  public RadioButton getButton() {
    return LightningWidgetView.button;
  }
}
