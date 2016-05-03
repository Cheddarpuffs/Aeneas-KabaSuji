package aeneas.controllers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import aeneas.controllers.DeleteBullpenPieceMove;
import aeneas.controllers.DeleteBoardPieceMove;
import aeneas.models.Board;
import aeneas.models.Bullpen;
import aeneas.models.Bullpen.BullpenLogic;
import aeneas.models.Level;
import aeneas.models.LevelGenerator;
import aeneas.models.Model;
import aeneas.models.Piece;
import aeneas.models.PlacedPiece;
import aeneas.models.PuzzleBoard;
import aeneas.models.PuzzleLevel;
import aeneas.models.Square;
import aeneas.views.BullpenView;

import javafx.scene.layout.VBox;

public class DeletePiece {
  Model model;
  Piece testPiece;

  @Before
  public void setup() {
    model = new Model();
    testPiece = new Piece(new Square[] { new Square(0, 0) });
  }

  @Test
  public void testDeleteFromBullpen() {
    Bullpen bullpen = new Bullpen(BullpenLogic.puzzleLogic());
    PuzzleLevel level = new PuzzleLevel(bullpen);
    model.setActiveLevel(level);
    bullpen.addPiece(testPiece);
    IMove move = new DeleteBullpenPieceMove(model, testPiece);

    assertTrue(move.execute());
    assertEquals(0, bullpen.getPieces().size());
    assertTrue(move.undo());
    assertEquals(1, bullpen.getPieces().size());
  }

  @Test
  public void testDeleteFromBoard() {
    Bullpen bullpen = new Bullpen(BullpenLogic.puzzleLogic());
    PuzzleLevel level = new PuzzleLevel(bullpen);
    model.setActiveLevel(level);
    PlacedPiece placed = new PlacedPiece(testPiece, 1, 1);
    level.getBoard().addPiece(placed);
    IMove move = new DeleteBoardPieceMove(model, placed);
    Board board = level.getBoard();

    assertTrue(move.execute());
    assertEquals(0, board.getPieces().size());
    assertTrue(move.undo());
    assertEquals(1, board.getPieces().size());
  }
}
