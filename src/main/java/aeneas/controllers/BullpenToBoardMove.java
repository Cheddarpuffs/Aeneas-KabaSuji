package aeneas.controllers;
import aeneas.models.Level;
import aeneas.models.Level.LevelWithMoves;
import aeneas.models.Model;
import aeneas.models.Piece;
import aeneas.models.PlacedPiece;

/**
 * Move action to move a piece from the bullpen to the board
 * @author Logan
 * @author jbkuszmaul
 */
public class BullpenToBoardMove implements IMove {
  Model model;

  PlacedPiece placedPiece;

  int row;
  int col;

  /**
   * Constructor
   *
   * @param level The level that is currently being played
   * @param piece The piece that is being moved
   * @param row The row to place the piece in
   * @param col The column to place the piece in
   */
  public BullpenToBoardMove(Model model, Piece piece, int row, int col) {
    this.model = model;
    this.placedPiece = new PlacedPiece(piece, row, col);
    this.row = row;
    this.col = col;
  }

  @Override
  public boolean execute() {
    if (!isValid()) return false;
    if (model instanceof LevelWithMoves && model.getActiveLevel().isActive()) {
      ((LevelWithMoves)model).decMoves();
    }

    model.getActiveLevel().getBullpen().removePiece(placedPiece.getPiece());
    return model.getActiveLevel().getBoard().addPiece(placedPiece);
  }

  @Override
  public boolean undo() {
    model.getActiveLevel().getBoard().removePiece(placedPiece);
    model.getActiveLevel().getBullpen().addPiece(placedPiece.getPiece());
    return true;
  }

  @Override
  public boolean isValid() {
    return model.getActiveLevel().getBoard().canAddPiece(placedPiece);
  }
}
