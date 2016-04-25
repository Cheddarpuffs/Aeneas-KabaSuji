package aeneas.models;

/**
 * A subclass of level with functionality specific to lightning mode.
 * @author Joseph Martin
 */
public class LightningLevel extends Level implements java.io.Serializable {
  public static final String helpText = "";

  LightningBoard board;
  int allowedTime;

  /**
   * Constructor
   * @param bullpen The Bullpen to use for this level
   * @param allowedTime The allowable time for this level
   * @param board The board to use for this level
   */
  public LightningLevel(Bullpen bullpen, int allowedTime, LightningBoard board) {
    super(bullpen);
    this.allowedTime = allowedTime;
    this.board = board;
  }  


  /**
   * Constructor. Will create a new, empty board for this level
   * @param bullpen The Bullpen to use for this level
   * @param allowedTime The allowable time for this level
   */
  public LightningLevel(Bullpen bullpen, int allowedTime) {
    super(bullpen);
    this.allowedTime = allowedTime;
    this.board = new LightningBoard();
  }  

  @Override
  public int getStarsEarned() {
    int numSquaresUncovered = board.numValidSquares()-board.numCoveredSquares();
    // Divide by 6, rounding up, then subtract from 3, and restrict to >0.
    return Math.max(0, 3 - (numSquaresUncovered+5)/6);
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
}
