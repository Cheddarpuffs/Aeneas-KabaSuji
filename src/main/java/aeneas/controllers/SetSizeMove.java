package aeneas.controllers;
import aeneas.models.Board;
import aeneas.models.Level;

/**
 * Set the number of rows/columns for the board.
 *
 * @author jbkuszmaul
 */
public class SetSizeMove implements IMove {
  Level level;

  int rows;
  int cols;
  int old_rows;
  int old_cols;

  boolean[][] old_squares;

  /**
   * Constructor
   *
   * @param level The level being edited.
   * @param rows The number of rows to set the board to have (1-Board.SIZE).
   * @param cols The number of columns to set the board to have (1-Board.SIZE).
   * @param old_rows The previous number of rows the board had.
   * @param old_cols The previous number of columns the board had.
   */
  public SetSizeMove(Level level, int rows, int cols, int old_rows, int old_cols) {
    this.level = level;
    this.rows = rows;
    this.cols = cols;
    this.old_rows = old_rows;
    this.old_cols = old_cols;
  }

  public boolean execute() {
    if (!isValid()) return false;
    // First, keep track of old configuration.
    // We can't just keep track of the old size, because
    // then we might miss the state of individual squares.
    boolean[][] squares = level.getBoard().getSquares();
    old_squares = new boolean[squares.length][squares[0].length];
    for (int i = 0; i < squares.length; ++i) {
      for (int j = 0; j < squares[0].length; ++j) {
        // Could use System.arraycopy, but honestly this looks nicer.
        // and System.arraycopy can only do 1-D arrays.
        old_squares[i][j] = squares[i][j];

        // Toggle any squares outside of the range off.
        if (i >= rows || j >= cols) squares[i][j] = false;
        else {
          // If old_rows or old_cols are less then rows/cols, then we need
          // to also toggle some squares on.
          if ((i >= old_rows && i < rows) || (j >= old_cols && j < cols)) {
            squares[i][j] = true;
          }
        }
      }
    }

    return true;
  }

  public boolean undo() {
    // Just go through and copy old_squares back in...
    boolean[][] squares = level.getBoard().getSquares();
    for (int i = 0; i < squares.length; ++i) {
      for (int j = 0; j < squares[0].length; ++j) {
        squares[i][j] = old_squares[i][j];
      }
    }
    return true;
  }

  public boolean isValid() {
    return rows > 0 && rows <= Board.SIZE &&
           cols > 0 && cols <= Board.SIZE;
  }

}
