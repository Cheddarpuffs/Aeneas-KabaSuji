package aeneas.models;

/**
 * Contains a piece and a location.
 * This represents a piece that has been placed (or is going to be placed) on the board.
 * @author Joseph Martin
 */
public class PlacedPiece {
  Piece piece;
  int row, col;

  public PlacedPiece(Piece piece, int row, int col) {
    this.piece = piece;
    this.row = row;
    this.col = col;
  }

  public int getRow() { return row; }
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
      placedSquares[i] = new Square(row+pieceSquares[i].getRow(), col+pieceSquares[i].getCol());
    }

    return placedSquares;
  }

  /**
   * Tests if the piece intersects a particular coordinate.
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
