package aeneas.models;

import java.io.File;
import java.io.IOException;

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
  public ReleaseLevel(Bullpen bullpen, ReleaseBoard board){
    super(bullpen);
    this.board = board;
  }

  /**
   * Constructor. Will create a new empty board for this level
   * @param bullpen The bullpen to use for this level
   */
  public ReleaseLevel(Bullpen bullpen) {
    this(bullpen, new ReleaseBoard());
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
    this.board = new ReleaseBoard(src.getBoard());
    if (src.bullpen.logic.equals(BullpenLogic.editorLogic()))
      this.bullpen.logic = BullpenLogic.editorLogic();
    else
      this.bullpen.logic = BullpenLogic.releaseLogic();
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
  public int decMoves() { return --movesLeft; }

  @Override
  public String getCountdownText() {
    return "Moves remaining: " + movesLeft;
  }

  @Override
  public boolean isFinished() {
    return movesLeft <= 0;
  }

  @Override
  public void start() {
    super.start();
    this.movesLeft = this.movesAllowed;
  }

  @Override
  public LevelWidgetView makeCorrespondingView(Model model) {
    return new ReleaseWidgetView(this);
  }

  public String getIconName() {
    return "SORT_NUMERIC_ASC";
  }

  @Override
  public Object clone() {
    ReleaseLevel newLevel =
      new ReleaseLevel((Bullpen)this.bullpen.clone(),
                         (ReleaseBoard)this.board.clone());
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

  @Override
  public void save(File file) throws IOException {
    // Remember to set the appropriate logic before saving.
    super.save(file, BullpenLogic.releaseLogic());
  }
}
