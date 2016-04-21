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
  ArrayList<ReleaseNumber> numbers;

  private int moves;

  public ReleaseLevel(Bullpen bullpen, ArrayList<ReleaseNumber> numbers) {
    super(bullpen);
    this.numbers = numbers;
  }

  private boolean numberSetIsCovered(ReleaseNumber.Color color) {
    for(ReleaseNumber n : numbers) {
      if(n.color == color && board.getPieceAtLocation(n.row, n.col) != null) {
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
    return Math.max(0, 3-numCoveredNumberSets());
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
