package aeneas.views;

import aeneas.models.Board;
import aeneas.models.Model;
import aeneas.models.Piece;
import aeneas.models.PlacedPiece;
import aeneas.models.Square;
import aeneas.views.PieceView.PieceSource;
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
public class BoardView extends GridPane implements PieceSource {

  /** Specifies how many pixels the squares of a piece on the board will be */
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
  
  public interface RefreshListener {
    public void refresh();
  }

  SquareView[][] grid = new SquareView[Board.SIZE][Board.SIZE];
  Board board;
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
   * @param board
   *          the board model object. Eventually this will model object will
   *          describe which squares are active
   */
  public BoardView(Pane levelPane, Model model, Board board) {
    clickListener = null;
    this.board = board;
    this.gameModel = model;

    initializeSquares();
    
    this.setOnDragDetected((event) -> {
      PlacedPiece draggedPiece = this.board.getPieceAtLocation(dragDropRow, dragDropCol);
     
      //check there's a piece at the location
      if (draggedPiece != null){
        Piece pieceModel = draggedPiece.getPiece();
        
        //remove the piece from the board
        this.board.removePiece(draggedPiece);
        this.pieceBeingDragged = draggedPiece;
        model.setLatestDragSource(this);

        refresh();
        
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
      }
    });
    
    // This handle the drop of a piece on the board
    this.setOnDragDropped((DragEvent event) -> {
      Dragboard db = event.getDragboard();

      // use this to draw the piece on the board
      Piece piece = (Piece) db.getContent(Piece.dataFormat);

      PlacedPiece placedPiece = new PlacedPiece(piece, dragDropRow, dragDropCol);
      boolean added = this.board.addPiece(placedPiece);
      
      refresh();
      
      if (!added){
        model.getLatestDragSource().returnPiece();
      } else {
        model.getLatestDragSource().dragSuccess();
      }
      
      if(refreshListener != null) {
        refreshListener.refresh();
      }

      // this might change we we actually implement it,
      // such as if they drop it on a square that doesn't exist
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
    Square[][] squares = board.assembleSquares();
    for (int row = 0; row < Board.SIZE; row++) {
      for (int col = 0; col < Board.SIZE; col++) {
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
          if (dropListener != null) {
            dragListener.squareDragged(r, c);
          }
        });

        grid[row][col].setOnDragOver((e) -> {
          e.acceptTransferModes(TransferMode.MOVE);
          e.consume();
        });

        grid[row][col].setOnDragDropped((e) -> {
          this.dragDropCol = c;
          this.dragDropRow = r;
          if (dropListener != null) {
            dropListener.squareDropped(r, c);
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
    Square[][] squares = board.assembleSquares();
    for (int row = 0; row < Board.SIZE; row++) {
      for (int col = 0; col < Board.SIZE; col++) {
        assert(squares[row][col] != null);
        grid[row][col].refresh(squares[row][col]);
      }
    }
  }

  @Override
  public void returnPiece() {
    if(pieceBeingDragged != null) {
      this.board.addPiece(pieceBeingDragged);
      pieceBeingDragged = null;
      refresh();
    }
  }

  @Override
  public void dragSuccess() {
    pieceBeingDragged = null;
  }
}
