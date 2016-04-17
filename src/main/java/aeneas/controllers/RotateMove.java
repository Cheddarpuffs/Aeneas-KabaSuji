package aeneas.controllers;
import aeneas.models.Piece;
import aeneas.models.Piece.Dir;

/**
 * Move action to rotate a piece
 *
 * @author Logan
 *
 */
public class RotateMove implements IMove {


  Piece piece;
  Dir direction;

  /**
   * Constructor
   * @param piece The piece that is to be rotated
   */
  public RotateMove(Piece piece, Dir direction) {
    this.piece = piece;
    this.direction = direction;
  }

  @Override
  public boolean execute() {
    piece.rotate(direction);
    return true;
  }

  @Override
  public boolean undo() {
    if(direction == Dir.CLOCKWISE)
      piece.rotate(Dir.COUNTERCLOCKWISE);
    else
      piece.rotate(Dir.CLOCKWISE);
    return false;
  }

  @Override
  public boolean isValid() {
    return true;
  }

}
