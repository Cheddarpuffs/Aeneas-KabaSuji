package aeneas.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import aeneas.models.Bullpen.BullpenLogic;
import aeneas.views.LevelWidgetView;
import aeneas.views.RefreshListener;
import javafx.scene.control.RadioButton;

/**
 *
 * @author Joseph Martin
 */
public abstract class Level implements java.io.Serializable {

  RefreshListener listener;

  public void setRefreshListener(RefreshListener listener) {
    this.listener = listener;
  }

  Bullpen bullpen;

  transient int levelNumber;
  transient boolean active = false;

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

    public void setStarsEarned(int stars) { starsEarned = stars; }
    public void setLocked(boolean locked) { this.locked = locked; }
  }

  public interface LevelWithMoves {
    public int getAllowedMoves();
    public void setAllowedMoves(int moves);
    public int decMoves();
  }

  public Level(Bullpen bullpen) {
    this.bullpen = bullpen;
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
  }

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
    return levelNumber <= 15;
  }

  public void reset() {
    Board board = getBoard();
    Bullpen bullpen = getBullpen();
    for (PlacedPiece piece : board.getPieces()) {
      bullpen.addPiece(piece.piece);
    }
    board.getPieces().clear();
  }

  /**
   * Saves the level to a file.
   * @param file The file to save to. Should not be null
   * @param saveLogic The BullpenLogic to save with.
   * @throws IOException could fail to load file
   */
  public void save(File file, BullpenLogic saveLogic) throws IOException {
    BullpenLogic oldLogic = getBullpen().getLogic();
    getBullpen().setLogic(saveLogic);
    try (FileOutputStream saveFile = new FileOutputStream(file);
         ObjectOutputStream out = new ObjectOutputStream(saveFile);) {
      out.writeObject(this);
    } catch (IOException i) {
      throw i;
    }
    getBullpen().setLogic(oldLogic);
  }
  public void save(File file) throws IOException {
    save(file, getBullpen().getLogic());
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

  public abstract LevelWidgetView makeCorrespondingView(Model model);

  public abstract RadioButton getButton();

  public abstract String getIconName();

  public void start() { active = true; }
  public void stop() { active = false; }

  public boolean isActive() {
    return active;
  }

  /**
   * Get the text for counting down the user to finished.
   * @return The text to be displayed to the user, eg "Time Remaining: 50"
   */
  public abstract String getCountdownText();

  /**
   * Get whether the level is finished and should exit.
   * @return true if we should exit the level.
   */
  public abstract boolean isFinished();

  public void copy(Level src, Level dst) {
    dst.bullpen = (Bullpen)src.bullpen.clone();
    dst.active = src.active;
    dst.levelNumber = src.levelNumber;
  }

  @Override
  public abstract Object clone();
}
