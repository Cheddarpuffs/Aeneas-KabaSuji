package aeneas.models;

import aeneas.models.Bullpen.BullpenLogic;
import aeneas.views.LevelWidgetView;
import aeneas.views.ReleaseWidgetView;

import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;

/**
 *
 * @author Joseph Martin
 * @author Logan Tutt
 */
public class ReleaseLevel extends Level
implements java.io.Serializable, Level.LevelWithMoves {
  public static final String helpText = "";

  ReleaseBoard board;

  private int movesAllowed;
  private transient int movesLeft;

  /**
   * Constructor
   * @param bullpen The bullpen to use for this level
   * @param board The Board to use for this level
   */
  public ReleaseLevel(Bullpen bullpen, ReleaseBoard board, boolean prebuilt){
    super(bullpen, prebuilt);
    this.board = board;
  }

  /**
   * Constructor
   * @param bullpen The bullpen to use for this level
   * @param board The Board to use for this level
   */
  public ReleaseLevel(Bullpen bullpen, ReleaseBoard board){
    this(bullpen, board, true);
  }

  /**
   * Constructor. Will create a new empty board for this level
   * @param bullpen The bullpen to use for this level
   */
  public ReleaseLevel(Bullpen bullpen) {
    this(bullpen, new ReleaseBoard(), true);
  }

  private boolean numberSetIsCovered(Color color) {
    for(ReleaseNumber n : board.getNumbers()) {
      if(n.getColor().equals(color) && board.getPieceAtLocation(n.row, n.col) == null) {
        return false;
      }
    }

    return true;
  }

  int numCoveredNumberSets() {
    int count = 0;
    count += numberSetIsCovered(ReleaseNumber.color1) ? 1 : 0;
    count += numberSetIsCovered(ReleaseNumber.color2) ? 1 : 0;
    count += numberSetIsCovered(ReleaseNumber.color3) ? 1 : 0;

    return count;
  }

  @Override
  public int getStarsEarned() {
    // This would have to change if we added more than 3 sets of numbers
    return numCoveredNumberSets();
  }

  public ReleaseLevel(Level src) {
    super(src);
    this.bullpen.logic = BullpenLogic.releaseLogic();
    this.board = new ReleaseBoard(src.getBoard());
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
    return new ReleaseWidgetView(this, model);
  }

  public String getIconName() {
    return "SORT_NUMERIC_ASC";
  }

  @Override
  public Object clone() {
    ReleaseLevel newLevel =
      new ReleaseLevel((Bullpen)this.bullpen.clone(),
                         (ReleaseBoard)this.board.clone(), this.prebuilt);
    super.copy(this, newLevel);
    newLevel.movesAllowed = this.movesAllowed;
    return newLevel;
  }

  @Override
  public void reset() {
    super.reset();
    this.movesLeft = movesAllowed;
  }

  @Override
  public RadioButton getButton() {
    return ReleaseWidgetView.button;
  }
}
