package kabasuji.views;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import kabasuji.models.Board;
import kabasuji.models.Square;

class BoardView extends Pane {

  static final int SQUARE_SIZE = 45;
  
  SquareView[][] grid = new SquareView[Board.SIZE][Board.SIZE];
  Board board;
  
  public BoardView(Board board) {
    this.board = board;
    for (int i = 0; i < Board.SIZE; i++) {
      for (int j = 0; j < Board.SIZE; j++) {
        grid[i][j] = new SquareView(SQUARE_SIZE);
        grid[i][j].setX(SQUARE_SIZE * i);
        grid[i][j].setY(SQUARE_SIZE * j);
        grid[i][j].setFill(Color.GRAY);
        this.getChildren().add(grid[i][j]);
      }
    }
  }
}