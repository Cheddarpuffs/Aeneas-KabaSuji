package aeneas.models;

public class PuzzleLevel extends Level {
  public static final String helpText = "";
  
  public PuzzleLevel(Bullpen bullpen) {
    super(bullpen);
  }
  
  PuzzleBoard board;

  @Override
  public boolean isComplete() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Board getBoard() {
    return board;
  }
}
