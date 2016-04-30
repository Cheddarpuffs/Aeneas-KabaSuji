package aeneas.models;

import java.util.ArrayList;

import javafx.scene.paint.Color;

/**
 *
 * @author Joseph Martin
 * @author Logan Tutt
 */
public class ReleaseBoard extends Board implements java.io.Serializable, Level.LevelWithMoves {
  ArrayList<ReleaseNumber> numbers;
  private int movesAllowed;
  private transient int movesLeft;

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

  @Override
  public Object clone() {
    ReleaseBoard newBoard = new ReleaseBoard();
    super.copy(this, newBoard);
    for (ReleaseNumber num : numbers) {
      newBoard.numbers.add((ReleaseNumber)num.clone());
    }
    return newBoard;
  }

  @Override
  public void setAllowedMoves(int movesAllowed) { this.movesAllowed = movesAllowed; }

  @Override
  public int getAllowedMoves() { return movesAllowed; }

  @Override
  public int decMoves() { return this.movesLeft; }
}
