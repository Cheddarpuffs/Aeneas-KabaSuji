package aeneas.controllers;
import aeneas.models.Board;
import aeneas.models.Level;

/**
 * Move action to toggle the state of a tile on the board
 * For use in the Builder
 *
 * @author Logan
 * @author Joseph Martin
 */
public class ToggleTileMove implements IMove {
  Level level;

  int row;
  int col;

  /**
   * Constructor
   *
   * @param level The level that is currently being edited
   * @param row The row of tile to toggle
   * @param col The column of tile to toggle
   */
  public ToggleTileMove(Level level, int row, int col) {
    this.level = level;
    this.row = row;
    this.col = col;
  }

  @Override
  public boolean execute() {
    if(!isValid()) return false;
    boolean[][] squares = level.getBoard().getSquares();
    squares[row][col] = !squares[row][col];
    return true;
  }

  @Override
  public boolean undo() {
    return execute();
  }

  @Override
  public boolean isValid() {
    return row < Board.SIZE && col < Board.SIZE;
  }

}
