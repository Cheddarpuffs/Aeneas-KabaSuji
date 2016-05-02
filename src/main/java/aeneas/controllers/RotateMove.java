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
   * @param direction The direction to rotate in, either CW or CCW
   */
  public RotateMove(Piece piece, Dir direction) {
    this.piece = piece;
    this.direction = direction;
  }

  @Override
  public boolean execute() {
    if(isValid()){
      piece.rotate(direction);
      return true;
    }
    return false;
  }

  @Override
  public boolean undo() {
    if(direction == Dir.CLOCKWISE)
      piece.rotate(Dir.COUNTERCLOCKWISE);
    else
      piece.rotate(Dir.CLOCKWISE);
    return true;
  }

  @Override
  public boolean isValid() {
    return true;
  }

}
