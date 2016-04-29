package aeneas.models;

/**
 * Contains a piece and a location.
 * This represents a piece that has been placed (or is going to be placed) on the board.
 * @author Joseph Martin
 */
public class PlacedPiece implements java.io.Serializable {
  Piece piece;
  public Piece getPiece() {
    return piece;
  }

  int row, col;

  /**
   * @param piece the piece model
   * @param row the row on the board, starting at 0
   * @param col the col on the board, starting at 0
   */
  public PlacedPiece(Piece piece, int row, int col) {
    this.piece = piece;
    this.row = row;
    this.col = col;
  }

  /**
   * @return the row
   */
  public int getRow() { return row; }

  /**
   * @return the col
   */
  public int getCol() { return col; }

  /**
   * Get the list of squares of this piece, offset by the piece location.
   * For instance, if row = 5, col = 6, and pieces contains the square with row=3, col=4,
   * then this will return an array containing  a square with row=8, col=10.
   * @return The list of squares
   */
  public Square[] getSquaresInBoardFrame() {
    Square[] pieceSquares = piece.getSquares();
    Square[] placedSquares = new Square[pieceSquares.length];
    for(int i = 0; i < pieceSquares.length; i++) {
      placedSquares[i] = new Square(row+pieceSquares[i].getRow(), col+pieceSquares[i].getCol(),piece.getColor());
    }

    return placedSquares;
  }

  /**
   * Tests if the piece intersects a particular coordinate.
   * @param row the row on which intersection is tested, starting at 0
   * @param col the col on which intersection is tested, starting at 0
   * @return True if the piece intersects the specified coordinate, false otherwise.
   */
  public boolean intersects(int row, int col) {
    for(Square s : getSquaresInBoardFrame()) {
      if(s.getRow() == row && s.getCol() == col) {
        return true;
      }
    }

    return false;
  }

  /**
   * Tests if the piece intersects another piece.
   * @param other the piece with which we are checking for intersection
   * @return True if the two pieces intersect, false otherwise.
   */
  public boolean intersects(PlacedPiece other) {
    for(Square s : getSquaresInBoardFrame()) {
      if(other.intersects(s.getRow(), s.getCol())) {
        return true;
      }
    }

    return false;
  }
}
