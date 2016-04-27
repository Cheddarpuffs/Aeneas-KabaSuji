package aeneas.models;

/**
 *
 * @author Joseph Martin
 */
public class PuzzleBoard extends Board implements java.io.Serializable {
  PuzzleBoard() { 
    super();
  }
  
  PuzzleBoard(Board board) {
    super(board);
  }
}
