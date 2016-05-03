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
public class DeleteBoardPieceMove implements IMove {
  Model model;
  PlacedPiece piece;

  /**
   * Constructor
   * @param model model
   * @param piece the piece you are deleting
   */
  public DeleteBoardPieceMove(Model model, PlacedPiece piece) {
    this.model=model;
    this.piece=piece;
  }

  @Override
  public boolean execute() {
    if (!isValid()) return false;
    model.getActiveLevel().getBoard().removePiece(piece);
    return true;

  }

  @Override
  public boolean undo() {
    model.getActiveLevel().getBoard().addPiece(piece);
    return true;
  }

  @Override
  public boolean isValid() {
    return true;
  }

}
