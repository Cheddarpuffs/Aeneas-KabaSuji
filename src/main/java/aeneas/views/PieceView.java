package aeneas.views;

import com.jfoenix.controls.JFXPopup;

import aeneas.controllers.ChildDraggedListener;
import aeneas.controllers.ManipulatePieceController;
import aeneas.models.Model;
import aeneas.models.Piece;
import aeneas.models.Piece.Axis;
import aeneas.models.Piece.Dir;
import aeneas.models.Square;

import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * View class for displaying a Piece
 *
 */
public class PieceView extends GridPane {

  private JFXPopup piecePopup;

  Piece pieceModel;
  Model model;
  int squareSize;
  Pane levelPane;
  ManipulatePieceController controller;

  private ChildDraggedListener childDraggedListener;

  /**
   * Constructor
   *
   * @param levelView
   *          the view for the level in which this piece is displayed
   * @param pieceModel
   *          The Piece that this view displays
   * @param model
   *          The model that is being used
   * @param squareSize
   *          The size of a single square in the piece
   */
  public PieceView(Pane levelPane, Piece pieceModel, Model model, int squareSize) {
    this.pieceModel = pieceModel;
    this.levelPane = levelPane;
    this.model = model;
    this.squareSize = squareSize;
    this.controller = new ManipulatePieceController(model, pieceModel, this);

    // callback for when drags are initiated
    this.setOnDragDetected((MouseEvent event) -> {
      Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
      ClipboardContent content = new ClipboardContent();
      content.put(Piece.dataFormat, pieceModel);
      db.setContent(content);

      SnapshotParameters snapshotParameters = new SnapshotParameters();
      snapshotParameters.setFill(Color.TRANSPARENT); // i3 doesn't handle this

      // create a new piece view just for the dragging so it can have a
      // different size
      PieceView fullSizedPieceView =
        new PieceView(levelPane, pieceModel, model, BoardView.SQUARE_SIZE);

      Image snapshotImage = fullSizedPieceView.snapshot(snapshotParameters, null);
      db.setDragView(snapshotImage);

      event.consume();

      if (childDraggedListener != null){
        childDraggedListener.onPieceDragged(this);
      }
    });

    this.setOnMouseClicked(controller);

    // create labels for popup
    VBox content = new VBox();
    content.setPadding(new Insets(5, 10, 5, 5));
    content.setSpacing(5);

    Label rotateCW = new Label("Rotate CW");
    rotateCW.setOnMouseClicked((MouseEvent event) -> {
      controller.doMove(Dir.CLOCKWISE);
    });
    content.getChildren().add(rotateCW);

    Label rotateCCW = new Label("Rotate CCW");
    rotateCCW.setOnMouseClicked((MouseEvent event) -> {
      controller.doMove(Dir.COUNTERCLOCKWISE);
    });
    content.getChildren().add(rotateCCW);

    Label flipVert = new Label("Flip Vert");
    flipVert.setOnMouseClicked((MouseEvent event) -> {
      controller.doMove(Axis.VERTICAL);
    });
    content.getChildren().add(flipVert);

    Label flipHorz = new Label("Flip Horz");
    flipHorz.setOnMouseClicked((MouseEvent event) -> {
      controller.doMove(Axis.HORIZONTAL);
    });
    content.getChildren().add(flipHorz);

    piecePopup = new JFXPopup();
    piecePopup.setSource(this);
    piecePopup.setContent(content);
    getChildren().add(piecePopup);

    // close popup when mouse leaves
    content.setOnMouseExited((e) -> {
      piecePopup.close();
    });

    refresh();

  }

  public void showPopup() {
    piecePopup.setPopupContainer(levelPane);
    piecePopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, squareSize * 1.5, squareSize * 1.5);
  }

  /**
   * Refreshes the view to match the stored piece
   */
  public void refresh() {
    this.getChildren().clear();
    for (Square s : pieceModel.getSquares()) {
      SquareView view = new SquareView(squareSize,s);
      this.add(view, s.getCol(), s.getRow());
    }
  }

  public void setOnChldDraggedListener(ChildDraggedListener listener) {
    this.childDraggedListener = listener;
  }

}
