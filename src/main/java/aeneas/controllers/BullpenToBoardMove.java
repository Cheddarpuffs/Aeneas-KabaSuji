package aeneas.controllers;
import aeneas.models.Level;
import aeneas.models.Level.LevelWithMoves;
import aeneas.models.Piece;
import aeneas.models.PlacedPiece;

/**
 * Move action to move a piece from the bullpen to the board
 * @author Logan
 * @author jbkuszmaul
 */
public class BullpenToBoardMove implements IMove {
  Level level;

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
  public BullpenToBoardMove(Level level, Piece piece, int row, int col) {
    this.level = level;
    this.placedPiece = new PlacedPiece(piece, row, col);
    this.row = row;
    this.col = col;
  }

  @Override
  public boolean execute() {
    if (!isValid()) return false;
    if (level instanceof LevelWithMoves && level.isActive()) {
      ((LevelWithMoves)level).decMoves();
    }

    level.getBullpen().removePiece(placedPiece.getPiece());
    return level.getBoard().addPiece(placedPiece);
  }

  @Override
  public boolean undo() {
    level.getBoard().removePiece(placedPiece);
    level.getBullpen().addPiece(placedPiece.getPiece());
    return true;
  }

  @Override
  public boolean isValid() {
    return level.getBoard().canAddPiece(placedPiece);
  }
}
