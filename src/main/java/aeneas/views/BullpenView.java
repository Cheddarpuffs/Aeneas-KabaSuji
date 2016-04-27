package aeneas.views;

import java.util.ArrayList;

import aeneas.controllers.ChildDraggedListener;
import aeneas.models.Bullpen;
import aeneas.models.Model;
import aeneas.models.Piece;
import aeneas.models.PieceFactory;
import aeneas.models.Square;
import aeneas.views.PieceView.PieceSource;
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
public class BullpenView implements ChildDraggedListener, PieceSource {

  VBox bullpenBox;
  Pane levelView;
  private Model model;
  Bullpen bullpen;

  static final int SQUARE_SIZE = 14;
  private String baseStyle = "-fx-padding:10px;";

  ArrayList<Pane> values = new ArrayList<Pane>();
  private PieceView pieceBeingDragged = null;

  public BullpenView(Model model, Bullpen bullpen, VBox bullpenBox, Pane levelView) {
    this.model = model;
    this.bullpen = bullpen;
    this.levelView = levelView;
    this.bullpenBox = bullpenBox;
    bullpenBox.setAlignment(Pos.TOP_CENTER);

    // This allows the bullpen to still be full height
    // when just a few pieces are added.
    bullpenBox.setMinHeight(550);

    // This handle the drop of a piece on the board
    bullpenBox.setOnDragDropped((DragEvent event) -> {
      Dragboard db = event.getDragboard();
      Piece pieceModel = (Piece) db.getContent(Piece.dataFormat);

      bullpen.addPiece(pieceModel);
      if(model.getLatestDragSource() != null && model.getLatestDragSource() != this) {
        model.getLatestDragSource().dragSuccess();
      }
      refresh();

      // this might change we we actually implement it,
      // such as if they drop it on a square that doesn't exist
      event.setDropCompleted(true);
      event.consume();

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

    for (int i = bullpen.getPieces().size() - 1; i >= 0; i--) {
      Piece piece = bullpen.getPieces().get(i);
      Pane piecePane = new Pane();
      PieceView pieceView = new PieceView(levelView, piece, model, SQUARE_SIZE);
      pieceView.setOnChildDraggedListener(this);
      piecePane.getChildren().add(pieceView);
      values.add(piecePane);
      bullpenBox.getChildren().add(piecePane);
    }
  }

  @Override
  public void onPieceDragged(PieceView pieceView) {
    pieceBeingDragged = pieceView;
    bullpen.removePiece(pieceView.pieceModel);
    refresh();
    model.setLatestDragSource(this);
  }

  @Override
  public void onSquareDragged(Square squareView) {
  }

  @Override
  public void returnPiece() {
    if(pieceBeingDragged != null) {
      bullpen.addPiece(pieceBeingDragged.pieceModel);
      refresh();
      pieceBeingDragged = null;
    }
  }

  @Override
  public void dragSuccess() {
    if(bullpen.getLogic().isRandom()) {
      int pieceIndex = (int)(Math.random()*35);
      bullpen.addPiece(PieceFactory.getPieces()[pieceIndex]);
    }
    refresh();
    pieceBeingDragged = null;
  }
}
