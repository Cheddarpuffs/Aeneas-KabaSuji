package aeneas.models;

public class PuzzleLevel extends Level implements java.io.Serializable {
  public static final String helpText = "";

  PuzzleBoard board;

  public PuzzleLevel(Bullpen bullpen) {
    super(bullpen);
  }

  public PuzzleLevel(Bullpen bullpen, boolean prebuilt) {
    super(bullpen, prebuilt);
  }

  public PuzzleLevel(Level src) {
    super(src);
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
  public LevelType getLevelType() { return LevelType.PUZZLE; }

  public void setAllowedMoves(int moves) { }
  public int getAllowedMoves() { return 3; }
}
