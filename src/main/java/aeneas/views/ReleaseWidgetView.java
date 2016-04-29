package aeneas.views;

import com.jfoenix.controls.JFXColorPicker;

import aeneas.models.Level;
import aeneas.models.Model;
import aeneas.models.ReleaseLevel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ReleaseWidgetView extends LevelWidgetView {

  public static final RadioButton button = new RadioButton("Release");
  Spinner<Integer> movesSelect;
  private Model model;
  private ReleaseLevel level;
  private SetMovesController controller;

  public ReleaseWidgetView(ReleaseLevel levelModel, Model model){
    super(levelModel);
    this.model = model;

    movesSelect = new Spinner<Integer>(1, 20, 10);
    Label movesLabel = new Label("Moves");
    movesSelect.setPrefWidth(70);
    movesSelect.setEditable(true);
    movesSelect.getValueFactory().setValue(levelModel.getAllowedMoves());
    controller = new SetMovesController(level, model);
    movesSelect.valueProperty().addListener(controller);


    VBox box = new VBox();
    box.setSpacing(4);

    HBox movesBox = new HBox();
    movesBox.setSpacing(5);
    movesBox.setAlignment(Pos.CENTER_LEFT);
    movesBox.getChildren().add(movesLabel);
    movesBox.getChildren().add(movesSelect);


    JFXColorPicker colorSelect = new JFXColorPicker();
    colorSelect.prefWidth(110);

    box.getChildren().add(movesBox);
    box.getChildren().add(colorSelect);

    panel.getChildren().add(box);

    button.setUserData(this);
  }

  @Override
  public RadioButton getButton() {
    return ReleaseWidgetView.button;
  }

  @Override
  public Level resetLevelModel(Level level) {
    if (level instanceof ReleaseLevel) {
      this.level = (ReleaseLevel)level;
      return level;
    }
    this.level = new ReleaseLevel(level);
    movesSelect.valueProperty().removeListener(controller);
    movesSelect.getValueFactory().setValue(this.level.getAllowedMoves());
    controller = new SetMovesController(this.level, model);
    movesSelect.valueProperty().addListener(controller);
    return this.level;
  }

  @Override
  public void refresh() {
    getButton().setSelected(true);
    movesSelect.valueProperty().removeListener(controller);
    movesSelect.getValueFactory().setValue(level.getAllowedMoves());
    movesSelect.valueProperty().addListener(controller);
  }
}

