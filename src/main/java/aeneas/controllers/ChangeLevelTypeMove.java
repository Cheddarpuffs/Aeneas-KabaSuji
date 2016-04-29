package aeneas.controllers;
import aeneas.models.Level;
import aeneas.views.BuildLevelView;

import javafx.scene.control.RadioButton;

/**
 * Move action to change the type of a level
 * @author jbkuszmaul
 */
public class ChangeLevelTypeMove implements IMove {

  Level newLevel;
  Level oldLevel;
  BuildLevelView view;

  public ChangeLevelTypeMove(BuildLevelView view, Level newLevel) {
    this.newLevel = newLevel;
    this.view = view;
  }

  @Override
  public boolean execute() {
    if (!isValid()) return false;
    this.oldLevel = view.getLevelModel();
    this.view.setLevelModel(newLevel);

    return true;
  }

  @Override
  public boolean undo() {
    view.setLevelModel(oldLevel);
    return true;
  }

  /**
   * This move will be valid, unless oldLevel and newLevel are equal.
   */
  @Override
  public boolean isValid() {
    return !this.newLevel.equals(view.getLevelModel());
  }

}
