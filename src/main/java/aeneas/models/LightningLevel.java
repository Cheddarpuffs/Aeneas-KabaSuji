package aeneas.models;

import aeneas.views.LevelView;
import aeneas.views.LightningView;

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
   * @param prebuilt is the level custom or prebuilt
   */
  public LightningLevel(Bullpen bullpen, int allowedTime, LightningBoard board, boolean prebuilt) {
    super(bullpen, prebuilt);
    this.allowedTime = allowedTime;
    this.board = board;
  }

  /**
   * Constructor
   * @param bullpen The Bullpen to use for this level
   * @param allowedTime The allowable time for this level
   * @param board The board to use for this level
   * 
   * assumes prebuilt
   */
  public LightningLevel(Bullpen bullpen, int allowedTime, LightningBoard board) {
    this(bullpen, allowedTime, board, true);
  }  


  /**
   * Constructor. Will create a new, empty board for this level
   * @param bullpen The Bullpen to use for this level
   * @param allowedTime The allowable time for this level
   * 
   * assumes prebuilt
   */
  public LightningLevel(Bullpen bullpen, int allowedTime) {
    this(bullpen, allowedTime, new LightningBoard(), true);
  }  


  @Override
  public int getStarsEarned() {
    int numSquaresUncovered = board.numValidSquares()-board.numCoveredSquares();
    // Divide by 6, rounding up, then subtract from 3, and restrict to >0.
    return Math.max(0, 3 - (numSquaresUncovered+5)/6);
  }

  public LightningLevel(Level src) {
    super(src);
    if (src instanceof LightningLevel) {
      this.board = ((LightningLevel)src).board;
      this.allowedTime = ((LightningLevel)src).allowedTime;
    }
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

  /**
   * Set the timer for the level.
   * @param seconds The time, in seconds, for the level timer.
   */
  public void setAllowedTime(int seconds) {
    allowedTime = seconds;
  }

  /**
   * Get the number of seconds the user has to complete the level.
   * @return The time allowed, in seconds.
   */
  public int getAllowedTime() { return allowedTime; }

  @Override
  public LevelView makeCorrespondingView() {
    return new LightningView(this);
  }
}
