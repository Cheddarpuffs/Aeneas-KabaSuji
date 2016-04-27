package aeneas.models;

import aeneas.models.Bullpen.BullpenLogic;
import aeneas.views.LevelWidgetView;
import aeneas.views.PuzzleWidgetView;

/**
 *
 * @author Joseph Martin
 */
public class PuzzleLevel extends Level
implements java.io.Serializable, Level.LevelWithMoves {
  public static final String helpText = "";

  PuzzleBoard board;

  private int moves;


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
  }


  @Override
  public int getStarsEarned() {
    return Math.max(0, 3 - board.numSquaresRemainig()/6);
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
  public void setAllowedMoves(int moves) { this.moves = moves; }

  @Override
  public int getAllowedMoves() { return moves; }

  @Override
  public LevelWidgetView makeCorrespondingView() {
    return new PuzzleWidgetView(this);
  }
  
  public String getIconName() {
    return "PUZZLE_PIECE";
  }
}
