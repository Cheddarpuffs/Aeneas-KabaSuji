package aeneas.models;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import aeneas.models.Bullpen.BullpenLogic;
import aeneas.views.LevelWidgetView;
import aeneas.views.LightningWidgetView;

import javafx.scene.control.RadioButton;

/**
 * A subclass of level with functionality specific to lightning mode.
 * @author Joseph Martin
 */
public class LightningLevel extends Level implements java.io.Serializable {
  public static final String helpText = "";

  LightningBoard board;
  int allowedTime;
  private transient int elapsedTime = 0;
  private transient Timer timer;
  ArrayList<Piece> startPieces = new ArrayList<Piece>();
  private boolean started = false;

  /**
   * Constructor
   * @param bullpen The Bullpen to use for this level
   * @param allowedTime The allowable time for this level
   * @param board The board to use for this level
   */
  public LightningLevel(Bullpen bullpen, int allowedTime, LightningBoard board) {
    super(bullpen);
    this.allowedTime = allowedTime;
    this.board = board;
  }

  /**
   * Constructor. Will create a new, empty board for this level
   * @param bullpen The Bullpen to use for this level
   * @param allowedTime The allowable time for this level
   */
  public LightningLevel(Bullpen bullpen, int allowedTime) {
    this(bullpen, allowedTime, new LightningBoard());
  }

  @Override
  public int getStarsEarned() {
    int numSquaresUncovered = board.numValidSquares()-board.numCoveredSquares();
    // Divide by 6, rounding up, then subtract from 3, and restrict to >0.
    return Math.max(0, 3 - (numSquaresUncovered+5)/6);
  }

  public LightningLevel(Level src) {
    super(src);
    if (src instanceof LightningLevel) {
      this.board = ((LightningLevel)src).board;
      this.allowedTime = ((LightningLevel)src).allowedTime;
    } else {
      this.board = new LightningBoard(src.getBoard());
    }
    if (src.bullpen.logic.equals(BullpenLogic.editorLogic()))
      this.bullpen.logic = BullpenLogic.editorLogic();
    else
      this.bullpen.logic = BullpenLogic.lightningLogic();
  }

  @Override
  public Board getBoard() {
    return board;
  }

  /**
   * Set the timer for the level.
   * @param seconds The time, in seconds, for the level timer.
   */
  public void setAllowedTime(int seconds) {
    allowedTime = seconds;
  }

  /**
   * Get the number of seconds the user has to complete the level.
   * @return The time allowed, in seconds.
   */
  public int getAllowedTime() { return allowedTime; }

  @Override
  public LevelWidgetView makeCorrespondingView(Model model) {
    return new LightningWidgetView(this);
  }

  public String getIconName() {
    return "BOLT";
  }

  /**
   * Called when the level gets started.
   * In this case, starts the timer and keeps track of the
   * pieces that started in the Bullpen (for resetting the level).
   */
  @Override
  public void start() {
    super.start();
    elapsedTime = 0;
    if (timer != null) timer.cancel();
    timer = new Timer();
    this.started = true;
    startPieces = (ArrayList<Piece>)this.bullpen.getPieces().clone();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        if(!active) {
          timer.cancel();
          return;
        }
        
        elapsedTime++;

        if(elapsedTime >= allowedTime) {
          timer.cancel();
        }
        if(listener != null) {
          listener.refresh();
        }
      }
    }, 1000, 1000);
  }

  @Override
  public String getCountdownText() {
    return "Time Remaining: " + (allowedTime - elapsedTime);
  }

  @Override
  public boolean isFinished() {
    return elapsedTime >= allowedTime;
  }

  @Override
  public void stop() {
    if (timer != null) {
      timer.cancel();
    }
  }

  @Override
  public Object clone() {
    LightningLevel newLevel =
      new LightningLevel((Bullpen)this.bullpen.clone(), this.allowedTime,
                         (LightningBoard)this.board.clone());
    super.copy(this, newLevel);
    return newLevel;
  }

  @Override
  public void reset() {
    this.elapsedTime = 0;
    getBoard().getPieces().clear();
    this.board.coveredSquares = new boolean[Board.MAX_SIZE][Board.MAX_SIZE];
    if (started) {
      getBullpen().getPieces().clear();
      for (Piece piece : startPieces) {
        getBullpen().addPiece(piece);
      }
    }
    this.start();
  }

  public RadioButton getButton() {
    return LightningWidgetView.button;
  }


  @Override
  public void save(File file) throws IOException {
    // Remember to set the appropriate logic before saving.
    super.save(file, BullpenLogic.lightningLogic());
  }
}
