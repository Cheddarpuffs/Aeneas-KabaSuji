package aeneas.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import aeneas.controllers.IMove;


/**
 * Top level entity class for KabaSuji.
 * @author Joseph Martin
 */
public class Model {

  public static final String aboutText = "KabaSuji is a brilliant new puzzle game. "
      + "It is the visionary work of Dr. George T Heineman, "
      + "and has been judiciously implemented by Peter Mitrano, "
      + "James Kuszmaul, Joseph Martin, Logan Tutt, and Garrison Hefter.";
  public static final String helpText = "To begin playing the game, select a level that is unlocked. "
      + "To begin editing a level, select and existing level from the list or create a new one. "
      + "Achievements can also be viewed with the 'View Achievements' button.";

  public final String helpString = "HELP";
  public final String aboutString = "ABOUT";

  public int numLevels = 15;
  public ArrayList<Level> levels;

  /** Mapping from levels to how many stars have been earned for that level. */
  public HashMap<Level, Integer> starsEarned;

  Level activeLevel;
  ArrayList<Achievement> achievements;
  Stack<IMove> undoStack;
  Stack<IMove> redoStack;

  public Model() {
    levels = new ArrayList<>();
    starsEarned = new HashMap<>();
    achievements = new ArrayList<>();
    for (int i = 0; i < numLevels; i++) {
      Bullpen b = new Bullpen(new ArrayList<>());
      Level l = new PuzzleLevel(b);
      l.levelNumber = i + 1;
      l.lock();
      levels.add(l);
    }

    //add custom level for example
    for (int i=0;i<20;i++){
      Bullpen b = new Bullpen(new ArrayList<>());
      Level l = new PuzzleLevel(b, false);
      l.prebuilt = false;
      l.levelNumber = i+16;
      levels.add(l);
    }

    //of course level 1 stars unlocked
    levels.get(0).unlock();
  }

  /**
   * Not sure why we have this *and* setActiveLevel
   * @param level the level to be selected
   */
  public void selectLevel(Level level) {
  }

  /**
   * Sets the current active level
   * @param level the new level
   */
  public void setActiveLevel(Level level) {
  }

  /**
   * Switch to the given screen
   */
  public void changeScreen(/*take argument for screen to go to*/) {
  }

  /**
   * Gets the number of stars earned for a particular level.
   *
   * @param level The level to get the current number of stars for.
   * @return The number of stars earned for the specified level, or 0 if the level wasn't found
   */
  public int getStarsForLevel(Level level) {
    Integer stars = starsEarned.get(level);
    if(stars == null) {
      return 0;
    }
    return stars;
  }

  /**
   * Called to notify that some game state may have changed,
   * so achievements, etc. can be checked and updated.
   */
  public void updateStats() {
    // Check each achievement for completion
    for(Achievement a : achievements) {
      if(a.checkUnlocked(this)) {
        // Update achievements screen here
      }
    }

    if(activeLevel != null) {
      int stars = activeLevel.getStarsEarned();
      if(getStarsForLevel(activeLevel) < stars) {
        starsEarned.put(activeLevel, stars);
      }
    }
  }

  /**
   * Undoes the most recently made move, if possible
   * @return true if undo was successful, false otherwise
   */
  public boolean undoLastMove() {
    if(undoStack.size() > 0) {
      IMove m = undoStack.peek();
      boolean success = m.undo();
      if(success) {
        undoStack.pop();
        redoStack.add(m);
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * Redoes the most recently undone move, if possible
   * @return true if redo was successful, false otherwise
   */
  public boolean redoLastMove() {
    if(redoStack.size() > 0) {
      IMove m = redoStack.peek();
      boolean success = m.undo();
      if(success) {
        redoStack.pop();
        undoStack.add(m);
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * Add new move to the undo stack.
   */
  public void addNewMove(IMove move){

  }
}
