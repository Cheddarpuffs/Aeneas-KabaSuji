package aeneas.views;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.effects.JFXDepthManager;

import aeneas.controllers.IMove;
import aeneas.controllers.SetMovesMove;
import aeneas.models.DragType;
import aeneas.models.Level;
import aeneas.models.Model;
import aeneas.models.ReleaseLevel;
import aeneas.models.ReleaseNumber;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Joseph Martin
 */
public class ReleaseWidgetView extends LevelWidgetView implements DragSource {

  public static final RadioButton button = new RadioButton("Release");

  Spinner<Integer> movesSelect;
  private ReleaseLevel level;
  private boolean isUserInput = true;
  private ReleaseNumView releaseNumView;

  public ReleaseWidgetView(ReleaseLevel levelModel, Model model) {
    super(levelModel);
    this.level = levelModel;

    movesSelect = new Spinner<Integer>(1, 999, 10);
    Label movesLabel = new Label("Moves");

    releaseNumView = new ReleaseNumView(new ReleaseNumber(0,0, Color.BLACK, 1));

    FontAwesomeIconView upArrowGlyph = new FontAwesomeIconView();
    upArrowGlyph.setGlyphName("ARROW_RIGHT");
    upArrowGlyph.setGlyphSize(14);

    FontAwesomeIconView downArrowGlyph = new FontAwesomeIconView();
    downArrowGlyph.setGlyphName("ARROW_LEFT");
    downArrowGlyph.setGlyphSize(14);

    Button upReleaseNumButton = new Button();
    upReleaseNumButton.setPrefSize(25, 25);
    upReleaseNumButton.setGraphic(upArrowGlyph);
    upReleaseNumButton.setPadding(new Insets(0, 0, 0, 0));
    upReleaseNumButton.setStyle("-fx-background-color:#9b59b6");
    JFXDepthManager.setDepth(upReleaseNumButton, 1);

    Button downReleaseNumButton = new Button();
    downReleaseNumButton.setPrefSize(25, 25);
    downReleaseNumButton.setGraphic(downArrowGlyph);
    downReleaseNumButton.setPadding(new Insets(0, 0, 0, 0));
    downReleaseNumButton.setStyle("-fx-background-color:#9b59b6");
    JFXDepthManager.setDepth(downReleaseNumButton, 1);

    upReleaseNumButton.setOnMouseClicked((e) -> {
      int i = releaseNumView.getValue();
      if (i < 6) {
        releaseNumView.setValue(i + 1);
        upReleaseNumButton.disableProperty().set(false);
        downReleaseNumButton.disableProperty().set(false);
      } else {
        upReleaseNumButton.disableProperty().set(true);
      }
    });

    downReleaseNumButton.setOnMouseClicked((e) -> {
      int i = releaseNumView.getValue();
      if (i > 1) {
        releaseNumView.setValue(i - 1);
        downReleaseNumButton.disableProperty().set(false);
        upReleaseNumButton.disableProperty().set(false);
      } else {
        downReleaseNumButton.disableProperty().set(true);
      }
    });

    movesSelect.setPrefWidth(70);
    movesSelect.setEditable(true);
    movesSelect.getValueFactory().setValue(levelModel.getAllowedMoves());
    movesSelect.valueProperty()
        .addListener((observer, old_value, new_value) -> {
          if (!isUserInput)
            return;
          IMove move = new SetMovesMove(level, new_value);
          if (move.execute()) {
            level.addNewMove(move);
          }
        });

    VBox box = new VBox();
    box.setSpacing(4);

    HBox movesBox = new HBox();
    movesBox.setSpacing(5);
    movesBox.setAlignment(Pos.CENTER_LEFT);
    movesBox.getChildren().add(movesLabel);
    movesBox.getChildren().add(movesSelect);

    JFXColorPicker colorSelect = new JFXColorPicker();
    colorSelect.prefWidth(100);

    HBox releaseNumBox = new HBox();
    releaseNumBox.setAlignment(Pos.CENTER_LEFT);
    releaseNumBox.setSpacing(6);
    releaseNumBox.getChildren().add(downReleaseNumButton);
    releaseNumBox.getChildren().add(releaseNumView.getNode());
    releaseNumBox.getChildren().add(upReleaseNumButton);

    box.getChildren().add(movesBox);
    box.getChildren().add(colorSelect);
    box.getChildren().add(releaseNumBox);

    panel.getChildren().add(box);

    button.setUserData(this);

    colorSelect.valueProperty()
        .addListener((observer, old_color, new_color) -> {
          releaseNumView.setColor(new_color);
        });

    releaseNumView.getNode().setOnDragDetected((MouseEvent event) -> {
      Dragboard db = releaseNumView.getNode().startDragAndDrop(TransferMode.MOVE);
      ClipboardContent content = new ClipboardContent();

      content.put(ReleaseNumber.dataFormat, releaseNumView.cloneNumber());
      content.put(DragType.dataFormat, DragType.Type.ReleaseNum);

      db.setContent(content);

      // allows the drop to check where this came from
      model.setLatestDragSource(this);

      SnapshotParameters snapshotParameters = new SnapshotParameters();
      snapshotParameters.setFill(Color.TRANSPARENT); // i3 doesn't handle this

      Image snapshotImage = releaseNumView.getNode().snapshot(snapshotParameters, null);
      db.setDragView(snapshotImage);

      event.consume();
    });

  }

  @Override
  public RadioButton getButton() {
    return ReleaseWidgetView.button;
  }

  @Override
  public void updateValues() {
    isUserInput = false;
    getButton().setSelected(true);
    movesSelect.getValueFactory().setValue(level.getAllowedMoves());
    isUserInput = true;
  }

  @Override
  public Level resetLevelModel(Level level) {
    if (level instanceof ReleaseLevel) {
      this.level = (ReleaseLevel) level;
      return level;
    }
    this.level = new ReleaseLevel(level);
    movesSelect.valueProperty()
        .addListener((observer, old_value, new_value) -> {
          if (!isUserInput)
            return;
          IMove move = new SetMovesMove(this.level, new_value);
          if (move.execute()) {
            level.addNewMove(move);
          }
        });
    updateValues();
    return this.level;
  }

  @Override
  public void returnDraggableNode() {
  }

  @Override
  public void dragSuccess() {
  }
}
