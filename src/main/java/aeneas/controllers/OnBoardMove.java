package aeneas.controllers;
import aeneas.models.Level;
import aeneas.models.PlacedPiece;
import aeneas.models.Level.LevelWithMoves;

/**
 * Move action to move a piece from one location to another on the board
 *
 * @author Logan
 *
 */
public class OnBoardMove implements IMove {
  Level level;
  PlacedPiece oldPiece;
  PlacedPiece newPiece;

  /**
   * Constructor
   * @param board the board the piece is being played on
   * @param piece the piece being moved
   * @param row the row to move the piece too
   * @param col the column to move the piece to
   */
  public OnBoardMove(Level level, PlacedPiece piece, int row, int col) {
    this.level = level;
    this.oldPiece = piece;
    this.newPiece = new PlacedPiece(piece.getPiece(), row, col);
  }

  @Override
  public boolean execute() {
    if (level instanceof LevelWithMoves && level.isActive()) {
      ((LevelWithMoves)level).decMoves();
    }
    level.getBoard().removePiece(oldPiece);
    return level.getBoard().addPiece(newPiece);
  }

  @Override
  public boolean undo() {
    level.getBoard().removePiece(newPiece);
    return level.getBoard().addPiece(oldPiece);
  }

  @Override
  public boolean isValid() {
    // TODO Auto-generated method stub
    return false;
  }

}
