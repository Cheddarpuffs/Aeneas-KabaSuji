package aeneas.models;

import java.util.ArrayList;

import javafx.scene.paint.Color;

/**
 * A Subclass of board with functionality specific to lightning mode.
 * @author Joseph Martin
 */
public class LightningBoard extends Board implements java.io.Serializable {
  // TODO: Figure out some way to make this transient but still initialize
  // when the level is created.
  boolean coveredSquares[][];

  public LightningBoard() {
    super();
  }

  public LightningBoard(Board board) {
    super(board);
  }

  @Override
  public boolean addPiece(PlacedPiece piece) {
    if(super.addPiece(piece)) {
      // Mark the squares as covered
      for(Square s : piece.getSquaresInBoardFrame()) {
        getCoveredSquares()[s.getRow()][s.getCol()] = true;
      }
      // Remove the piece
      removePiece(piece);
      return true;
    } else {
      return false;
    }
  }

  public boolean[][] getCoveredSquares() {
    // This seems to be necessary because of serialization?
    if(coveredSquares == null) {
      coveredSquares = new boolean[SIZE][SIZE];
      for(int j = 0; j < SIZE; j++) {
        for(int i = 0; i < SIZE; i++) {
          coveredSquares[j][i] = false;
        }
      }
    }
    return coveredSquares;
  }

  public int numCoveredSquares() {
    int count = 0;
    for(int j = 0; j < SIZE; j++) {
      for(int i = 0; i < SIZE; i++) {
        count += getCoveredSquares()[j][i] ? 1 : 0;
      }
    }

    return count;
  }

  @Override
  public Square[][] assembleSquares() {
    Square[][] s = super.assembleSquares();
    for(int row = 0;row<getCoveredSquares().length;row++ ){
      for(int col = 0;col<getCoveredSquares().length;col++){
        if(getCoveredSquares()[row][col]){
          s[row][col]=new Square(row, col, Color.GREEN);
        }
      }
    }
    return s;
  }

  @Override
  public Object clone() {
    LightningBoard newBoard = new LightningBoard();
    super.copy(this, newBoard);
    for (int i = 0; i < SIZE; ++i) {
      for (int j = 0; j < SIZE; ++j) {
        newBoard.coveredSquares[i][j] = this.coveredSquares[i][j];
      }
    }
    return newBoard;
  }
}
