package aeneas.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Joseph Martin
 */
public abstract class Level implements java.io.Serializable {
  Bullpen bullpen;

  public int levelNumber;
  boolean prebuilt;

  private boolean locked;

  public interface LevelWithMoves {
    public int getAllowedMoves();
    public void setAllowedMoves(int moves);
  }

  public enum LevelType {
    PUZZLE ("Puzzle"),
    LIGHTNING ("Lightning"),
    RELEASE ("Release");

    private final String name;
    LevelType(String name) {
      this.name = name;
    }

    public String getName() { return name; }

    public static LevelType fromString(String name) {
      switch (name) {
        case "Puzzle":
          return PUZZLE;
        case "Lightning":
          return LIGHTNING;
        case "Release":
          return RELEASE;
      }
      return PUZZLE;
    }
  }

  public Level(Bullpen bullpen, boolean prebuilt) {
    this.bullpen = bullpen;
    this.prebuilt = prebuilt;
  }

  public Level(Bullpen bullpen) {
    this(bullpen, true);
  }

  @Override
  public int hashCode() {
    return levelNumber;
  }

  @Override
  public boolean equals(Object o) {
    if(o == null) return false;

    if(o instanceof Level) {
      Level other = (Level)o;
      if(other.levelNumber == levelNumber && other.prebuilt == prebuilt) {
        return true;
      }
    }

    return false;
  }

  /**
   * Copy constructor.
   * Does not actually copy the Bullpen, just passes along
   * the reference.
   */
  public Level(Level src) {
    this.bullpen = src.bullpen;
    this.levelNumber = src.levelNumber;
    this.prebuilt = src.prebuilt;
    this.locked = src.locked;
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

  public abstract int getStarsEarned();

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

  public void reset() {
  }

  /**
   * Saves the level to a file.
   * @param file The file to save to. Should not be null
   */
  public void save(File file) throws IOException {
    try (FileOutputStream saveFile = new FileOutputStream(file);
         ObjectOutputStream out = new ObjectOutputStream(saveFile);) {
      out.writeObject(this);
    } catch (IOException i) {
      throw i;
    }
  }

  /**
   * Constructs a level from a file.
   * @param file The file to load from.
   * @return The level that was read; null if the read failed.
   */
  public static Level loadLevel(File file) throws IOException {
    Level level;
    try (FileInputStream loadFile = new FileInputStream(file);
         ObjectInputStream in = new ObjectInputStream(loadFile);){
      level = (Level)in.readObject();
    } catch (IOException i) {
      throw i;
    } catch (ClassNotFoundException c) {
      // Something very bad has happened.
      // May as well fail silently, then.
      return null;
    }

    return level;
  }

  /**
   * Get the level type without doing lots of instanceof's.
   */
  public abstract LevelType getLevelType();
}
