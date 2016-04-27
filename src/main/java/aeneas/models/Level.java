package aeneas.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import aeneas.views.LevelWidgetView;

/**
 *
 * @author Joseph Martin
 */
public abstract class Level implements java.io.Serializable {
  Bullpen bullpen;

  transient int levelNumber;
  transient boolean active = false;
  boolean prebuilt;

  public int getLevelNumber() {
    return levelNumber;
  }

  public static class Metadata implements java.io.Serializable {
    int starsEarned;
    boolean locked;

    public Metadata() { this.starsEarned = 0; this.locked = true; }

    public Metadata(int starsEarned, boolean locked) {
      this.starsEarned = starsEarned;
      this.locked = locked;
    }

    public int getStarsEarned() { return starsEarned; }
    public boolean isLocked() { return locked; }

    void setStarsEarned(int stars) { starsEarned = stars; }
    void setLocked(boolean locked) { this.locked = locked; }
  }

  public interface LevelWithMoves {
    public int getAllowedMoves();
    public void setAllowedMoves(int moves);
  }

  public Level(Bullpen bullpen, boolean prebuilt) {
    this.bullpen = bullpen;
    this.prebuilt = prebuilt;
  }

  /**
   * Copy constructor.
   * @param src the level you are copying from
   * Does not actually copy the Bullpen, just passes along
   * the reference.
   */
  public Level(Level src) {
    this.bullpen = src.bullpen;
    this.levelNumber = src.levelNumber;
    this.prebuilt = src.prebuilt;
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
   * @return the bullpen
   */
  public Bullpen getBullpen() {
    return bullpen;
  }

  /**
   * @return the prebuilt
   */
  public boolean isPrebuilt() {
    return prebuilt;
  }

  public void reset() {
  }

  /**
   * Saves the level to a file.
   * @param file The file to save to. Should not be null
   * @throws IOException could fail to load file
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
   * @throws IOException could fail to load file
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

  public ArrayList<Piece> getPieces() {
    return bullpen.pieces;
  }

  public abstract LevelWidgetView makeCorrespondingView();

  public abstract String getIconName();

  public void start() { active = true; }
  public void stop() { active = false; }
  
  public boolean isActive() {
    return active;
  }
}
