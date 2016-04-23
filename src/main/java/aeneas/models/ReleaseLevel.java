package aeneas.models;

import java.util.ArrayList;

/**
 *
 * @author Joseph Martin
 */
public class ReleaseLevel extends Level
    implements java.io.Serializable, Level.LevelWithMoves {
  public static final String helpText = "";

  ReleaseBoard board;

  private int moves;

  public ReleaseLevel(Bullpen bullpen, ReleaseBoard board) {
    super(bullpen);
    this.board = board;
  }

  private boolean numberSetIsCovered(ReleaseNumber.Color color) {
    for(ReleaseNumber n : board.getNumbers()) {
      if(n.color == color && board.getPieceAtLocation(n.row, n.col) == null) {
        return false;
      }
    }

    return true;
  }

  int numCoveredNumberSets() {
    int count = 0;
    count += numberSetIsCovered(ReleaseNumber.Color.RED) ? 1 : 0;
    count += numberSetIsCovered(ReleaseNumber.Color.GREEN) ? 1 : 0;
    count += numberSetIsCovered(ReleaseNumber.Color.BLUE) ? 1 : 0;

    return count;
  }

  @Override
  public int getStarsEarned() {
    // This would have to change if we added more than 3 sets of numbers
    return numCoveredNumberSets();
  }

  public ReleaseLevel(Level src) {
    super(src);
  }

  @Override
  public boolean isComplete() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Board getBoard() {
    return board;
  }

  @Override
  public LevelType getLevelType() { return LevelType.RELEASE; }

  @Override
  public void setAllowedMoves(int moves) { this.moves = moves; }

  @Override
  public int getAllowedMoves() { return moves; }
}
