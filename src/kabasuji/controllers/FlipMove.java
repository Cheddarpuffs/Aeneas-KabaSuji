package kabasuji.controllers;
import kabasuji.models.Piece;

/**
 * Move action to flip a piece
 * 
 * @author Logan
 *
 */
public class FlipMove implements IMove {
  
  
  Piece piece;
  
  /**
   * Constructor
   * @param piece The piece that is to be flipped
   */
  public FlipMove(Piece piece) {
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
