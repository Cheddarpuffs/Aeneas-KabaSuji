package aeneas.views;

import com.jfoenix.controls.JFXColorPicker;

import aeneas.models.ReleaseLevel;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ReleaseView extends LevelView {

  private static final RadioButton button = new RadioButton("Release");

  public ReleaseView(ReleaseLevel levelModel){
    super(levelModel);

    Spinner<Integer> movesSelect = new Spinner<Integer>(1, 20, 10);
    Label movesLabel = new Label("Moves");
    movesSelect.setPrefWidth(70);
    movesSelect.setEditable(true);
    movesSelect.getValueFactory().setValue(levelModel.getAllowedMoves());
    movesSelect.valueProperty().addListener((observer, old_value, new_value) -> {
      levelModel.setAllowedMoves(new_value);
    });


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
    return ReleaseView.button;
  }
}

