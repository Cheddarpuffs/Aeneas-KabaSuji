package aeneas.models;

import java.util.ArrayList;

import javafx.scene.paint.Color;

/**
 *
 * @author Joseph Martin
 * @author Logan Tutt
 */
public class ReleaseBoard extends Board implements java.io.Serializable {

  ArrayList<ReleaseNumber> numbers;


  public ReleaseBoard(){
    this.numbers = new ArrayList<ReleaseNumber>();
  }

  public ReleaseBoard(ArrayList<ReleaseNumber> numbers){
    this.numbers = numbers;

  }
  
  public ReleaseBoard(Board board) {
    super(board);
    this.numbers = new ArrayList<>();
  }


  /**
   * Gets all squares on the board, including release numbers.
   * Release numbers are only added to the squares if the square is valid and uncovered
   */
  @Override
  public Square[][] assembleSquares(){
    Square[][] squares = super.assembleSquares();
    for(ReleaseNumber num: numbers){
      if(squares[num.getCol()][num.getRow()] != null && getPieceAtLocation(num.getRow(), num.getCol()) == null)
        squares[num.getCol()][num.getRow()] = new Square(num.getRow(), num.getCol(), num, Color.GRAY);
    }
    return squares;
  }

  public ArrayList<ReleaseNumber> getNumbers() {
    return numbers;
  }
}
