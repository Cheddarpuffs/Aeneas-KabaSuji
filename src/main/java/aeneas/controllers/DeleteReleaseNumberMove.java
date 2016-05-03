package aeneas.controllers;
import aeneas.models.Level;
import aeneas.models.Level.LevelWithMoves;
import aeneas.models.Model;
import aeneas.models.Piece;
import aeneas.models.PlacedPiece;
import aeneas.models.ReleaseBoard;
import aeneas.models.ReleaseNumber;

/**
 * Move action to move a piece from the bullpen to the board
 * @author Garrison
 *
 */
public class DeleteReleaseNumberMove implements IMove {
  Model model;
  ReleaseNumber num;

  /**
   * Constructor
   * @param model model
   * @param num the release number you are deleting
   */
  public DeleteReleaseNumberMove(Model model, ReleaseNumber num) {
    this.model=model;
    this.num=num;
  }

  @Override
  public boolean execute() {
    if (!isValid()) return false;
    if(model.getActiveLevel().getBoard() instanceof ReleaseBoard){
    ((ReleaseBoard)model.getActiveLevel().getBoard()).removeNumber(num);
    return true;}
    else return false;

  }

  @Override
  public boolean undo() {
    ((ReleaseBoard)model.getActiveLevel().getBoard()).addNumber(num);
    return true;
  }

  @Override
  public boolean isValid() {
    return true;
  }

}
