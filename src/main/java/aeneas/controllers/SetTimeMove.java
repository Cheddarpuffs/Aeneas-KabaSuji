package aeneas.controllers;
import aeneas.models.LightningLevel;

/**
 * Move action to set the allotted time for a lightning level
 * For use in the builder.
 *
 * @author Logan
 * @author jbkuszmaul
 */
public class SetTimeMove implements IMove {

  LightningLevel level;

  int newTime;
  int oldTime;

  /**
   * Constructor
   * @param level the level that is being played
   * @param time the amount of time to set the level too
   */
  public SetTimeMove(LightningLevel level, int time) {
    this.level = level;
    this.newTime = time;
  }

  @Override
  public boolean execute() {
    if (!isValid()) return false;
    oldTime = level.getAllowedTime();
    level.setAllowedTime(newTime);
    return true;
  }

  @Override
  public boolean undo() {
    level.setAllowedTime(oldTime);
    return true;
  }

  @Override
  public boolean isValid() {
    return level != null && newTime != level.getAllowedTime();
  }

}
