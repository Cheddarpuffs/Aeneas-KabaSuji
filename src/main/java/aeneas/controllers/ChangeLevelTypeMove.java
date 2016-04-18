package aeneas.controllers;
import aeneas.models.Level;
import aeneas.models.Model;

/**
 * Move action to change the type of a level
 */
public class ChangeLevelTypeMove implements IMove {

  Level level;
  Level oldLevel;

  int type;

  /**
   * Constructor
   *
   * @param level The level that is currently being played
   * @param model The game model that holds the level being changed
   * @param type the type of the level to be changed to
   */
  public ChangeLevelTypeMove(Level level, Model model, int type) {
    this.level = level;
    this.type = type;
  }

  @Override
  public boolean execute() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean undo() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isValid() {
    // TODO Auto-generated method stub
    return false;
  }

}
