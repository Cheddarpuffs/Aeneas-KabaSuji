package aeneas.controllers;
import aeneas.models.Model;
import aeneas.models.Piece;

/**
 * Move action to move a piece from the bullpen to the board
 * @author Garrison
 * @author Peter Mitrano
 *
 */
public class DeleteBullpenPieceMove implements IMove {
  Model model;
  Piece piece;

  /**
   * Constructor
   * @param model model
   * @param piece the piece you are placing
   */
  public DeleteBullpenPieceMove(Model model, Piece piece) {
    this.model=model;
    this.piece=piece;
  }

  @Override
  public boolean execute() {
    if (!isValid()) return false;
    model.getActiveLevel().getBullpen().removePiece(piece);
    return true;

  }

  @Override
  public boolean undo() {
    model.getActiveLevel().getBullpen().addPiece(piece);
    return true;
  }

  @Override
  public boolean isValid() {
    return true;
  }

}
