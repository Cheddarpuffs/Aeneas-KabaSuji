package aeneas.views;

import aeneas.Main;
import aeneas.controllers.AddNumMove;
import aeneas.controllers.BullpenToBoardMove;
import aeneas.controllers.IMove;
import aeneas.controllers.OnBoardMove;
import aeneas.models.Board;
import aeneas.models.DragType;
import aeneas.models.DragType.Type;
import aeneas.models.Model;
import aeneas.models.Piece;
import aeneas.models.PlacedPiece;
import aeneas.models.ReleaseLevel;
import aeneas.models.ReleaseNumber;
import aeneas.models.Square;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * View class to display a board
 *
 * @author Logan Tutt
 * @author Joseph Martin
 */
public class BoardView extends GridPane implements DragSource {

  /**
   * Specifies how many pixels the squares of a piece on the board will be
   */
  static final int SQUARE_SIZE = 40;

  public interface SquareClickListener {
    public void squareClicked(int row, int col);
  }

  public interface SquareDragListener {
    public void squareDragged(int row, int col);
  }

  public interface SquareDropListener {
    public void squareDropped(int row, int col);
  }

  SquareView[][] grid = new SquareView[Board.MAX_SIZE][Board.MAX_SIZE];
  Model gameModel;
  private int dragDropRow = 0, dragDropCol = 0;
  private SquareClickListener clickListener;
  private SquareDragListener dragListener;
  private SquareDropListener dropListener;
  private RefreshListener refreshListener;
  private PlacedPiece pieceBeingDragged = null;

  public void setRefreshListener(RefreshListener listener) {
    this.refreshListener = listener;
  }

  /**
   * Initialized the board with grey squares
   *
   * @param levelPane
   *          a passthrew for the pane you want the pieceview to render it's
   *          right-click menu in
   * @param model
   *          the model object for the game
   */
  public BoardView(Pane levelPane, Model model) {
    clickListener = null;
    this.gameModel = model;

    initializeSquares();

    this.setOnDragDetected((event) -> {
      Board tempBoard = this.gameModel.getActiveLevel().getBoard();
      PlacedPiece draggedPiece = tempBoard.getPieceAtLocation(dragDropRow,
          dragDropCol);

      // check there's a piece at the location
      if (draggedPiece != null) {
        Piece pieceModel = draggedPiece.getPiece();

        // remove the piece from the board
        if (this.gameModel.getActiveLevel().getBoard()
            .removePiece(draggedPiece)) {
          this.pieceBeingDragged = draggedPiece;
          model.setLatestDragSource(this);

          refresh();

          Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
          ClipboardContent content = new ClipboardContent();
          content.put(Piece.dataFormat, pieceModel);
          content.put(DragType.dataFormat, DragType.Type.Piece);
          db.setContent(content);

          SnapshotParameters snapshotParameters = new SnapshotParameters();
          snapshotParameters.setFill(Color.TRANSPARENT); // i3 doesn't handle
                                                         // this

          // create a new piece view just for the dragging so it can have a
          // different size
          PieceView fullSizedPieceView = new PieceView(levelPane, pieceModel,
              model.getActiveLevel(), BoardView.SQUARE_SIZE);

          Image snapshotImage = fullSizedPieceView.snapshot(snapshotParameters,
              null);
          if (Main.isRunningOnMac()) {
            db.setDragViewOffsetX(snapshotImage.getWidth() / 2);
            db.setDragViewOffsetY(-snapshotImage.getHeight() / 2);
          }
          db.setDragView(snapshotImage);
        }

        event.consume();
      }
    });

    // This handle the drop of a piece on the board
    this.setOnDragDropped((DragEvent event) -> {
      Dragboard db = event.getDragboard();

      int closestCol = -1;
      int closestRow = -1;
      double closestNodeDist2 = Double.MAX_VALUE;

      for (Node n : this.getChildren()) {
        Integer row = GridPane.getRowIndex(n);
        Integer col = GridPane.getColumnIndex(n);
        if (row != null && col != null) {
          double dist2 = Math.pow(event.getX() - n.getLayoutX(), 2)
              + Math.pow(event.getY() - n.getLayoutY(), 2);
          if (dist2 < closestNodeDist2) {
            closestNodeDist2 = dist2;
            closestCol = col;
            closestRow = row;
          }
        }
      }

      Type type = (Type) db.getContent(DragType.dataFormat);
      IMove move;

      switch (type) {
      default:
      case Piece:
        // use this to draw the piece on the board
        Piece piece = (Piece) db.getContent(Piece.dataFormat);

        DragSource source = model.getLatestDragSource();
        if (source instanceof BoardView) {
          BoardView v = (BoardView) source;
          IMove m = new OnBoardMove(gameModel.getActiveLevel(),
              v.getLastDraggedPiece(), closestRow, closestCol);
          if (!m.execute()) {
            model.returnDraggableNode();
          } else {
            model.dragSuccess();
            model.getActiveLevel().addNewMove(m);
          }
        } else if (source instanceof BullpenView) {
          IMove m = new BullpenToBoardMove(gameModel.getActiveLevel(), piece,
              closestRow, closestCol);
          if (!m.execute()) {
            model.returnDraggableNode();
          } else {
            model.dragSuccess();
            model.getActiveLevel().addNewMove(m);
          }
        }
        break;

      case ReleaseNum:
        // use this to draw the piece on the board
        ReleaseNumber releaseNum = (ReleaseNumber) db
            .getContent(ReleaseNumber.dataFormat);
        releaseNum.setRow(closestRow);
        releaseNum.setCol(closestCol);

        move = new AddNumMove((ReleaseLevel) gameModel.getActiveLevel(),
            releaseNum, closestRow, closestCol);
        if (!move.execute()) {
          model.returnDraggableNode();
        } else {
          model.dragSuccess();
          model.getActiveLevel().addNewMove(move);
        }
        break;
      }

      refresh();

      if (refreshListener != null) {
        refreshListener.refresh();
      }

      event.setDropCompleted(true);
      event.consume();
    });

    // this is absolutely nessecary
    this.setOnDragOver((DragEvent event) -> {
      event.acceptTransferModes(TransferMode.MOVE);
      event.consume();
    });
  }

  private void initializeSquares() {
    Square[][] squares = this.gameModel.getActiveLevel().getBoard()
        .assembleSquares();
    for (int row = 0; row < Board.MAX_SIZE; row++) {
      for (int col = 0; col < Board.MAX_SIZE; col++) {
        grid[row][col] = new SquareView(SQUARE_SIZE, squares[row][col]);
        final int r = row;
        final int c = col;

        grid[row][col].setOnMouseClicked((e) -> {
          if (clickListener != null) {
            clickListener.squareClicked(r, c);
          }
        });

        grid[row][col].setOnDragDetected((e) -> {
          this.dragDropCol = c;
          this.dragDropRow = r;
          if (dragListener != null) {
            dragListener.squareDragged(r, c);
          }
        });

        this.add(grid[row][col], col, row);
      }
    }

  }

  public void setSquareClickListener(SquareClickListener listener) {
    this.clickListener = listener;
  }

  public void setSquareDraggedListener(SquareDragListener listener) {
    this.dragListener = listener;
  }

  public void setSquareDroppedListener(SquareDropListener listener) {
    this.dropListener = listener;
  }

  /**
   * Refreshes the view to match the current state of the board
   */
  public void refresh() {
    Square[][] squares = this.gameModel.getActiveLevel().getBoard()
        .assembleSquares();
    for (int row = 0; row < Board.MAX_SIZE; row++) {
      for (int col = 0; col < Board.MAX_SIZE; col++) {
        grid[row][col].refresh(squares[row][col]);
      }
    }
  }

  public PlacedPiece getLastDraggedPiece() {
    return pieceBeingDragged;
  }

  @Override
  public void returnDraggableNode() {
    if (pieceBeingDragged != null) {
      this.gameModel.getActiveLevel().getBoard().addPiece(pieceBeingDragged);
      pieceBeingDragged = null;
      refresh();
    }
  }

  @Override
  public void dragSuccess() {
    pieceBeingDragged = null;
  }
}
