package aeneas.models;

import java.util.ArrayList;

import javafx.scene.paint.Color;

/**
 * 
 * @author Joseph Martin
 */
public class ReleaseLevel extends Level implements java.io.Serializable {
  public static final String helpText = "";

  ReleaseBoard board;
  ArrayList<ReleaseNumber> numbers;

  public ReleaseLevel(Bullpen bullpen, ArrayList<ReleaseNumber> numbers) {
    super(bullpen);
    this.numbers = numbers;
  }
  
  private boolean numberSetIsCovered(Color color) {
    for(ReleaseNumber n : numbers) {
      if(n.color == color && board.getPieceAtLocation(n.row, n.col) != null) {
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
    return Math.max(0, 3-numCoveredNumberSets());
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

}
