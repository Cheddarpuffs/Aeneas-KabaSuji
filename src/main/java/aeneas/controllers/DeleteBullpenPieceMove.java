package aeneas.controllers;
import aeneas.models.Level;
import aeneas.models.Level.LevelWithMoves;
import aeneas.models.Model;
import aeneas.models.Piece;
import aeneas.models.PlacedPiece;

/**
 * Move action to move a piece from the bullpen to the board
 * @author Garrison
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
