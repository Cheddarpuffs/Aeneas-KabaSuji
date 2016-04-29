package aeneas.models;

import java.util.ArrayList;
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
   * @param prebuilt is the level custom or prebuilt
   */
  public LightningLevel(Bullpen bullpen, int allowedTime, LightningBoard board, boolean prebuilt) {
    super(bullpen, prebuilt);
    this.allowedTime = allowedTime;
    this.board = board;
  }

  /**
   * Constructor
   * @param bullpen The Bullpen to use for this level
   * @param allowedTime The allowable time for this level
   * @param board The board to use for this level
   *
   * assumes prebuilt
   */
  public LightningLevel(Bullpen bullpen, int allowedTime, LightningBoard board) {
    this(bullpen, allowedTime, board, true);
  }


  /**
   * Constructor. Will create a new, empty board for this level
   * @param bullpen The Bullpen to use for this level
   * @param allowedTime The allowable time for this level
   *
   * assumes prebuilt
   */
  public LightningLevel(Bullpen bullpen, int allowedTime) {
    this(bullpen, allowedTime, new LightningBoard(), true);
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
    this.bullpen.logic = BullpenLogic.lightningLogic();
  }

  @Override
  public boolean isComplete() {
    // TODO Auto-generated method stub
    return false;
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
    return new LightningWidgetView(this, model);
  }

  public String getIconName() {
    return "BOLT";
  }

  @Override
  public void start() {
    timer = new Timer();
    this.started = true;
    startPieces = (ArrayList<Piece>)this.bullpen.getPieces().clone();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        elapsedTime++;
        System.out.println("tick "+elapsedTime+"/"+allowedTime);
        if(elapsedTime >= allowedTime) {
          System.out.println("Times up");
          timer.cancel();
        }
      }
    }, 1000, 1000);
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
                         (LightningBoard)this.board.clone(), this.prebuilt);
    super.copy(this, newLevel);
    return newLevel;
  }

  @Override
  public void reset() {
    this.elapsedTime = this.allowedTime;
    getBoard().getPieces().clear();
    this.board.coveredSquares = new boolean[Board.SIZE][Board.SIZE];
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
}
