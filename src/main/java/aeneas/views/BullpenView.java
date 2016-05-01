package aeneas.views;

import aeneas.controllers.ChildDraggedListener;
import aeneas.models.DragType;
import aeneas.models.DragType.Type;
import aeneas.models.Model;
import aeneas.models.Piece;
import aeneas.models.PieceFactory;
import aeneas.models.Square;

import javafx.geometry.Pos;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Joseph Martin
 */
public class BullpenView implements ChildDraggedListener, DragSource {

  VBox bullpenBox;
  Pane levelView;
  private Model model;

  static final int SQUARE_SIZE = 14;
  private String baseStyle = "-fx-padding:10px;";

  private PieceView pieceBeingDragged = null;

  public BullpenView(Model model, VBox bullpenBox, Pane levelView) {
    this.model = model;
    this.levelView = levelView;
    this.bullpenBox = bullpenBox;
    bullpenBox.setAlignment(Pos.TOP_CENTER);

    // This allows the bullpen to still be full height
    // when just a few pieces are added.
    bullpenBox.setMinHeight(550);

    // This handle the drop of a piece on the board
    bullpenBox.setOnDragDropped((DragEvent event) -> {
      Dragboard db = event.getDragboard();

      Type type = (Type) db.getContent(DragType.dataFormat);

      switch (type) {
      default:
      case Piece:
        Piece pieceModel = (Piece) db.getContent(Piece.dataFormat);
        model.getActiveLevel().getBullpen().addPiece(pieceModel);
        if(model.getLatestDragSource() != null && model.getLatestDragSource() != this) {
          model.getLatestDragSource().dragSuccess();
        }
        refresh();

        // this might change we we actually implement it,
        // such as if they drop it on a square that doesn't exist
        event.setDropCompleted(true);
        event.consume();
        break;
      case ReleaseNum:
        break;
      }


    });

    // this is absolutely nessecary
    bullpenBox.setOnDragOver((DragEvent event) -> {
      event.acceptTransferModes(TransferMode.MOVE);
      event.consume();
    });

    bullpenBox.setOnDragExited((e) -> {
      bullpenBox.setStyle(baseStyle);
    });

    bullpenBox.setOnDragEntered((e) -> {
      bullpenBox.setStyle(baseStyle + "-fx-background-color:#E2E2E2;");
      e.consume();
    });
  }

  public void refresh() {

    bullpenBox.getChildren().clear();

    for (int i = model.getActiveLevel().getBullpen().getPieces().size() - 1; i >= 0; i--) {
      Piece piece = model.getActiveLevel().getBullpen().getPieces().get(i);
      Pane piecePane = new Pane();
      PieceView pieceView = new PieceView(levelView, piece,  model.getActiveLevel(), SQUARE_SIZE);
      pieceView.setOnChildDraggedListener(this);
      piecePane.getChildren().add(pieceView);
      bullpenBox.getChildren().add(piecePane);
    }
  }

  @Override
  public void onPieceDragged(PieceView pieceView) {
    pieceBeingDragged = pieceView;
    model.getActiveLevel().getBullpen().removePiece(pieceView.pieceModel);
    refresh();
    model.setLatestDragSource(this);
  }

  @Override
  public void onSquareDragged(Square squareView) {
  }

  @Override
  public void returnNode() {
    if(pieceBeingDragged != null) {
      model.getActiveLevel().getBullpen().addPiece(pieceBeingDragged.pieceModel);
      refresh();
      pieceBeingDragged = null;
    }
  }

  @Override
  public void dragSuccess() {
    if( model.getActiveLevel().getBullpen().getLogic().isRandom()) {
      int pieceIndex = (int)(Math.random()*35);
      model.getActiveLevel().getBullpen().addPiece(PieceFactory.getPieces()[pieceIndex]);
    }
    refresh();
    pieceBeingDragged = null;
  }
}
