package aeneas.controllers;
import aeneas.models.Level;
import aeneas.models.PlacedPiece;

/**
 * Move action  to move a piece from the board to the bullpen
 *
 * @author Logan
 *
 */
public class BoardToBullpenMove implements IMove {
  Level level;

  PlacedPiece piece;

  /**
   * Constructor
   * @param level the level that is being played
   * @param piece the piece to move
   */
  public BoardToBullpenMove(Level level, PlacedPiece piece) {
    this.level = level;
    this.piece = piece;
  }

  @Override
  public boolean execute() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean undo() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isValid() {
    // TODO Auto-generated method stub
    return false;
  }

}
