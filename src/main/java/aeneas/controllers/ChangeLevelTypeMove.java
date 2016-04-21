package aeneas.controllers;
import aeneas.models.Level;
import aeneas.models.Level.LevelType;
import aeneas.models.LightningLevel;
import aeneas.models.PuzzleLevel;
import aeneas.models.ReleaseLevel;
import aeneas.views.BuildLevelView;

/**
 * Move action to change the type of a level
 */
public class ChangeLevelTypeMove implements IMove {

  Level oldLevel;

  LevelType type;

  BuildLevelView view;

  /**
   * Constructor
   *
   * @param level The level that is currently being played
   * @param view The build level view.
   * @param type the type of the level to be changed to
   */
  public ChangeLevelTypeMove(Level level, BuildLevelView view, LevelType type) {
    this.oldLevel = level;
    this.type = type;
    this.view = view;
  }

  @Override
  public boolean execute() {
    if (!isValid()) return false;

    Level newLevel = null;
    switch (type) {
      case PUZZLE:
        newLevel = new PuzzleLevel(oldLevel);
        break;
      case LIGHTNING:
        newLevel = new LightningLevel(oldLevel);
        break;
      case RELEASE:
        newLevel = new ReleaseLevel(oldLevel);
        break;
    }

    view.setLevel(newLevel);

    return true;
  }

  @Override
  public boolean undo() {
    view.setLevel(oldLevel);
    return true;
  }

  /**
   * This move will always be valid.
   */
  @Override
  public boolean isValid() {
    return true;
  }

}
