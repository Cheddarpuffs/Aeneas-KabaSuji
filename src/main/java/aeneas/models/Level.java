package aeneas.models;

public abstract class Level {
  Bullpen bullpen;

  public int levelNumber;
  public int starsEarned = 2;
  boolean prebuilt;

  private boolean locked;

  public Level(Bullpen bullpen, boolean prebuilt) {
    this.bullpen = bullpen;
    this.prebuilt = prebuilt;
  }

  public Level(Bullpen bullpen) {
    this(bullpen, true);
  }

  /**
   * Check if the level is done
   * @return true if the level is complete, false otherwise.
   */
  public abstract boolean isComplete();

  /**
   * Get the board for this level
   * @return The board used by this level.
   */
  public abstract Board getBoard();

  /**
   * @return the prebuilt
   */
  public boolean isPrebuilt() {
    return prebuilt;
  }

  /**
   * @return the locked
   */
  public boolean isLocked() {
    return locked;
  }

  public void unlock() {
    this.locked = false ;
  }

  public void lock() {
    this.locked = true;
  }
}
