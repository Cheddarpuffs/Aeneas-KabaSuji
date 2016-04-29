package aeneas.models;

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
  public PuzzleLevel(Bullpen bullpen, PuzzleBoard board, boolean prebuilt){
    super(bullpen, prebuilt);
    this.board = board;
  }

  /**
   * Constructor
   * @param bullpen The bullpen to use for this level
   * @param board The board to use for this level
   */
  public PuzzleLevel(Bullpen bullpen, PuzzleBoard board){
    this(bullpen, board, true);
  }

  /**
   * Constructor. Will create a new, empty board for this level
   * @param bullpen The bullpen to use for this level
   */
  public PuzzleLevel(Bullpen bullpen) {
    this(bullpen, new PuzzleBoard(), true);
  }

  public PuzzleLevel(Bullpen bullpen, boolean prebuilt) {
    super(bullpen, prebuilt);
    board = new PuzzleBoard();
  }

  public PuzzleLevel(Level src) {
    super(src);
    this.bullpen.logic = BullpenLogic.puzzleLogic();
    this.board = new PuzzleBoard(src.getBoard());
  }


  @Override
  public int getStarsEarned() {
    return Math.max(0, 3 - board.numSquaresRemaining()/6);
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

  @Override
  public void setAllowedMoves(int movesAllowed) { this.movesAllowed = movesAllowed; }

  @Override
  public int getAllowedMoves() { return movesAllowed; }

  @Override
  public LevelWidgetView makeCorrespondingView(Model model) {
    return new PuzzleWidgetView(this, model);
  }

  @Override
  public RadioButton getButton() {
    return PuzzleWidgetView.button;
  }

  public String getIconName() {
    return "PUZZLE_PIECE";
  }

  @Override
  public Object clone() {
    PuzzleLevel newLevel =
      new PuzzleLevel((Bullpen)this.bullpen.clone(),
                         (PuzzleBoard)this.board.clone(), this.prebuilt);
    super.copy(this, newLevel);
    newLevel.movesAllowed = this.movesAllowed;
    return newLevel;
  }

  @Override
  public void reset() {
    super.reset();
    this.movesLeft = movesAllowed;
  }
}
