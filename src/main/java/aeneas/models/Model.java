package aeneas.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import aeneas.controllers.IMove;
import aeneas.models.Level.Metadata;
import aeneas.views.PieceView.PieceSource;

/**
 * Top level entity class for KabaSuji.
 *
 * @author Joseph Martin
 */
public class Model {

  public static final String aboutText = "KabaSuji is a brilliant new puzzle game. "
      + "It is the visionary work of Dr. George T Heineman, "
      + "and has been judiciously implemented by Peter Mitrano, "
      + "James Kuszmaul, Joseph Martin, Logan Tutt, and Garrison Hefter.";

  public static final String helpText = "The editor allows you to build you're own levels."
    + " You can save your levels, and play them back later.\n"
    + "\n"
    + "Stars are earned on your performance, and you must earn one star to proceed to the next level."
    + " Pieces can be dragged on to and around the board, depending on the level type."
    + " You can rotate and flip pieces by right clicking on a piece."
    + " You can also use the following key-bindings:\n"
    + "\n"
    + "\tclick: rotate clockwise\n"
    + "\tshift + click: rotate counter-clockwise\n"
    + "\tctrl + click: flip horizontally\n"
    + "\tctrl + shift + click: flip vertically\n"
    + "\n"
    + "Have fun!"
    ;

  private PieceSource latestDragSource;

  public final String helpString = "HELP";
  public final String aboutString = "ABOUT";

  /** Mapping from level id to level metadata */
  HashMap<Integer, Level.Metadata> levelMetadata;
  LevelIndex index;

  Level activeLevel;
  ArrayList<Achievement> achievements;
  Stack<IMove> undoStack;
  Stack<IMove> redoStack;

  public Model() {
    levelMetadata = new HashMap<>();
    achievements = new ArrayList<>();
    index = new LevelIndex();

    undoStack = new Stack<IMove>();
    redoStack = new Stack<IMove>();

    levelMetadata.put(1, new Level.Metadata(0, false));
  }

  /**
   * Gets the metadata associated with a level.
   *
   * @param level
   *          The level to get metadata for.
   * @return The metadata for the requested level, or the default metadata if
   *         there wasn't an entry for the level. (default metadata is locked
   *         and has 0 stars earned).
   */
  public Level.Metadata getMetadata(Level level) {
    Level.Metadata m = levelMetadata.get(level.getLevelNumber());
    if (m == null) {
      m = new Level.Metadata();
      levelMetadata.put(level.getLevelNumber(), m);
    }
    return m;
  }

  public Level getLevel(int idx) {
    return index.getLevel(idx);
  }

  public Iterable<Level> getLevels() {
    return index.getLevels();
  }

  /**
   * Called to notify that some game state may have changed, so achievements,
   * etc. can be checked and updated.
   */
  public void updateStats() {
    // Check each achievement for completion
    for (Achievement a : achievements) {
      if (a.checkUnlocked(this)) {
        // Update achievements screen here
      }
    }

    if (activeLevel != null) {
      int stars = activeLevel.getStarsEarned();
      if (getMetadata(activeLevel).getStarsEarned() < stars) {
        Level.Metadata m = levelMetadata
            .getOrDefault(activeLevel.getLevelNumber(), new Level.Metadata());
        m.setStarsEarned(stars);
        if (stars > 0) {
          Level.Metadata nextMetadata = levelMetadata.getOrDefault(
              activeLevel.getLevelNumber() + 1, new Level.Metadata());
          nextMetadata.setLocked(false);
          levelMetadata.put(activeLevel.getLevelNumber() + 1, nextMetadata);
        }
        levelMetadata.put(activeLevel.getLevelNumber(), m);
      }
    }
  }

  /**
   * Undoes the most recently made move, if possible
   *
   * @return true if undo was successful, false otherwise
   */
  public boolean undoLastMove() {
    if (undoStack.size() > 0) {
      IMove m = undoStack.peek();
      boolean success = m.undo();
      if (success) {
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
   *
   * @return true if redo was successful, false otherwise
   */
  public boolean redoLastMove() {
    if (redoStack.size() > 0) {
      IMove m = redoStack.peek();
      boolean success = m.execute();
      if (success) {
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
   *
   * @param move
   *          the move to add
   */
  public void addNewMove(IMove move) {
    undoStack.add(move);
  }

  public void saveLevelMetadata(File file) throws IOException {
    try (FileOutputStream saveFile = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(saveFile);) {
      out.writeObject(levelMetadata);
    } catch (IOException i) {
      throw i;
    }
  }

  public void loadLevelMetadata(File file) throws IOException {
    try (FileInputStream loadFile = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(loadFile);) {
      Object o = in.readObject();
      if (o instanceof HashMap) {
        this.levelMetadata = (HashMap<Integer, Metadata>) o;
      }
    } catch (IOException i) {
      throw i;
    } catch (ClassNotFoundException c) {
    } catch (ClassCastException c) {
    }
  }

  public void setLatestDragSource(PieceSource latestDragSource) {
    this.latestDragSource = latestDragSource;
  }

  public PieceSource getLatestDragSource() {
    return latestDragSource;
  }

  public void setActiveLevel(Level levelModel) {
    activeLevel = levelModel;
  }

  public Level getActiveLevel() {
    return activeLevel;
  }

  public LevelIndex getLevelIndex() {
    return index;
  }

  public void refreshLevelIndex() {
    index.reindex();
  }
}
