package aeneas.models;

import java.io.File;
import java.io.IOException;

import aeneas.models.Bullpen.BullpenLogic;
import aeneas.views.LevelWidgetView;
import aeneas.views.PuzzleWidgetView;

import javafx.scene.control.RadioButton;

/**
 *
 * @author Joseph Martin
 */
public class PuzzleLevel extends Level
implements java.io.Serializable, Level.LevelWithMoves {
  public static final String helpText = "";

  PuzzleBoard board;

  private int movesAllowed;
  private transient int movesLeft;

  /**
   * Constructor
   * @param bullpen The bullpen to use for this level
   * @param board The board to use for this level
   */
  public PuzzleLevel(Bullpen bullpen, PuzzleBoard board){
    super(bullpen);
    this.board = board;
  }

  /**
   * Constructor. Will create a new, empty board for this level
   * @param bullpen The bullpen to use for this level
   */
  public PuzzleLevel(Bullpen bullpen) {
    this(bullpen, new PuzzleBoard());
  }

  public PuzzleLevel(Bullpen bullpen, boolean prebuilt) {
    super(bullpen);
    board = new PuzzleBoard();
    movesAllowed = 1;
  }

  public PuzzleLevel(Level src) {
    super(src);
    
    if(src instanceof LevelWithMoves) {
      this.movesAllowed = ((LevelWithMoves) src).getAllowedMoves();
    }
    
    if (src.bullpen.logic.equals(BullpenLogic.editorLogic()))
      this.bullpen.logic = BullpenLogic.editorLogic();
    else
      this.bullpen.logic = BullpenLogic.puzzleLogic();
    this.board = new PuzzleBoard(src.getBoard());
  }

  @Override
  public int getStarsEarned() {
    return Math.max(0, 3 - board.numSquaresRemaining()/6);
  }

  @Override
  public Board getBoard() {
    return board;
  }

  @Override
  public void start() {
    super.start();
    this.movesLeft = this.movesAllowed;
  }

  @Override
  public void setAllowedMoves(int movesAllowed) { this.movesAllowed = movesAllowed; }

  @Override
  public int getAllowedMoves() { return movesAllowed; }

  @Override
  public int decMoves() { return --this.movesLeft; }

  @Override
  public LevelWidgetView makeCorrespondingView(Model model) {
    return new PuzzleWidgetView(this);
  }

  @Override
  public RadioButton getButton() {
    return PuzzleWidgetView.button;
  }

  public String getIconName() {
    return "PUZZLE_PIECE";
  }

  @Override
  public String getCountdownText() {
    return "Moves remaining: " + movesLeft;
  }

  @Override
  public boolean isFinished() {
    return movesLeft <= 0;
  }

  @Override
  public Object clone() {
    PuzzleLevel newLevel =
      new PuzzleLevel((Bullpen)this.bullpen.clone(),
                         (PuzzleBoard)this.board.clone());
    super.copy(this, newLevel);
    newLevel.movesAllowed = this.movesAllowed;
    return newLevel;
  }

  @Override
  public void reset() {
    super.reset();
    this.movesLeft = movesAllowed;
  }

  public void save(File file) throws IOException {
    // Remember to set the appropriate logic before saving.
    super.save(file, BullpenLogic.puzzleLogic());
  }
}
