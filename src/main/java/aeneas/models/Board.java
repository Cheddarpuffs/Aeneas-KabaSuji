package aeneas.models;

import java.util.ArrayList;

import javafx.scene.paint.Color;

/**
 * Represents a board (which keeps track of its pieces, hints, and the shape of
 * the board).
 *
 * @author Joseph Martin
 */
public abstract class Board implements java.io.Serializable {
  public static final int MAX_SIZE = 12;

  public static final Color DEFAULT_COLOR = Color.GRAY;

  int rows = MAX_SIZE;
  int cols = MAX_SIZE;

  boolean[][] squares = new boolean[MAX_SIZE][MAX_SIZE];
  ArrayList<PlacedPiece> pieces;
  ArrayList<PlacedPiece> hints;
  protected transient boolean isEditor = false;

  public Board() {
    pieces = new ArrayList<>();
    hints = new ArrayList<>();
    for (int i=0;i<MAX_SIZE;i++){
      for (int j=0;j<MAX_SIZE;j++){
        squares[i][j] = true;
      }
    }
  }

  public Board(Board board) {
    this.squares = board.squares;
    this.hints = board.hints;
    this.pieces = board.pieces;
    this.isEditor = board.isEditor;
  }

  /**
   * Count the number of valid squares on this board
   *
   * @return The number of valid squares.
   */
  int numValidSquares() {
    int count = 0;
    for (int j = 0; j < squares.length; j++) {
      for (int i = 0; i < squares[j].length; i++) {
        count += squares[j][i] ? 1 : 0;
      }
    }
    return count;
  }

  /**
   * Check if a square on the board is valid. If the row or col is < 0 or > 12,
   * the square is invalid, otherwise check if the square is valid in the
   * squares array.
   *
   * @return True if the square is valid, false otherwise.
   */
  boolean locationValid(Square s) {
    int row = s.getRow();
    int col = s.getCol();
    if (row >= 0 && row < squares.length && col >= 0 && col < squares[row].length) {
      return squares[row][col];
    } else {
      return false;
    }
  }

  /**
   * Adds the given piece to the board, if valid.
   *
   * @param piece
   *          The piece to add to the board.
   * @return true if the piece was added to the board, false otherwise.
   */
  public boolean addPiece(PlacedPiece piece) {
    if (!canAddPiece(piece)) return false;
    // Otherwise placement is valid
    if (piece.getPiece().isHint()) hints.add(piece);
    else pieces.add(piece);
    return true;
  }

  /**
   * Determines if the piece can be added to the board.
   * @param piece The piece to check the validity of.
   * @return true if the piece can be added.
   */
  public boolean canAddPiece(PlacedPiece piece) {
    // If any of the squares of the piece are at an invalid location,
    // piece placement not valid
    for (Square s : piece.getSquaresInBoardFrame()) {
      if (!locationValid(s))
        return false;
    }
    // If it is a hint, we don't care about intersections.
    if (piece.getPiece().isHint()) return true;
    // If the piece overlaps an existing piece, placement not valid
    if (intersects(piece))
      return false;
    // Otherwise placement is valid
    return true;
  }

  /**
   * Removes the given piece from the board, if possible
   *
   * @param piece
   *          The piece to be removed
   * @return true If the piece on the board and it was removed, false otherwise.
   */
  public boolean removePiece(PlacedPiece piece) {
    return pieces.remove(piece);
  }

  /**
   * Gets the piece at the specified position (if there is one)
   *
   * @param row
   *          The row coordinate
   * @param col
   *          The column coordinate
   * @return The piece at the given position if there is one, null otherwise
   */
  public PlacedPiece getPieceAtLocation(int row, int col) {
    for (PlacedPiece p : pieces) {
      if (p.intersects(row, col)) {
        return p;
      }
    }

    return null;
  }

  /**
   * Test if the given piece would intersect a piece on the board
   *
   * @param piece
   *          The piece to test intersection for
   * @return true if the piece intersects another piece on the board, false
   *         otherwise.
   */
  public boolean intersects(PlacedPiece piece) {
    for (PlacedPiece p : pieces) {
      if (piece.intersects(p)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Gets all the pieces currently on the board
   *
   * @return The list of pieces on the board.
   */
  public ArrayList<PlacedPiece> getPieces() {
    return pieces;
  }

  /**
   * Gets the list of hints currently on the board
   *
   * @return The list of hints on the board.
   */
  public ArrayList<PlacedPiece> getHints() {
    return hints;
  }

  /**
   * Gets the state of all squares in the board. A square will be null if no
   * square is there
   *
   * @return A two dimensional array representing the current state of the board
   */
  public Square[][] assembleSquares(){
    Square[][] squares = new Square[MAX_SIZE][MAX_SIZE];
    for (PlacedPiece piece : hints){
      for(Square s : piece.getSquaresInBoardFrame())
        squares[s.getRow()][s.getCol()] = s;
    }
    // Run normal pieces after hint in order to avoid hint displaying
    // over the normal pieces.
    for (PlacedPiece piece : pieces){
      for(Square s : piece.getSquaresInBoardFrame())
        squares[s.getRow()][s.getCol()] = s;
    }
    for(int row = 0;row<squares.length;row++ ){
      for(int col = 0;col<squares[0].length;col++){
        if (squares[row][col] == null && this.squares[row][col])
            squares[row][col]=new Square(row, col, Board.DEFAULT_COLOR);
      }
    }
    return squares;
  }

  public int numSquaresRemaining() {
    int count = 0;
    for(int j = 0; j < MAX_SIZE; j++) {
      for(int i = 0; i < MAX_SIZE; i++) {
        count += squares[j][i] && (getPieceAtLocation(j, i) == null) ? 1 : 0;
      }
    }

    return count;
  }

  public boolean[][] getSquares() {
    return squares;
  }

  /**
   * Copy common elements of two boards; used for clone() on the subclasses.
   * @param src the board to get the data from
   * @param dest the board to copy the data to
   */
  protected void copy(Board src, Board dest) {
    for (PlacedPiece piece : src.pieces) {
      dest.pieces.add(new PlacedPiece(piece.piece, piece.row, piece.col));
    }
    for (PlacedPiece hint : src.hints) {
      dest.hints.add(new PlacedPiece(hint.piece, hint.row, hint.col));
    }
    for (int i = 0; i < MAX_SIZE; ++i) {
      for (int j = 0; j < MAX_SIZE; ++j) {
        dest.squares[i][j] = src.squares[i][j];
      }
    }
    dest.rows = src.rows;
    dest.cols = src.cols;
  }

  /**
   * Resizes the board
   * @param rows number of rows
   * @param cols number of columns
   */
  public void resizeBoard(int rows, int cols){

    for (int row = 0; row < squares.length; row++) {
      for (int col = 0; col < squares[0].length; col++) {

        // Toggle any squares outside of the range off.
        if (row >= rows || col >= cols)
          squares[row][col] = false;
        else {
          // If old_rows or old_cols are less then rows/cols, then we need
          // to also toggle some squares on.
          if ((row >= this.rows && row < rows) || (col >= this.cols && col < cols)) {
            squares[row][col] = true;
          }
        }
      }
    }
    this.rows = rows;
    this.cols = cols;
  }

  public int getRows(){return rows;}

  public int getCols(){return cols;}


  public void setIsEditor(boolean is) {
    this.isEditor = is;
  }
}
