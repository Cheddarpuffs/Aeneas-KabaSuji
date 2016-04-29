package aeneas.controllers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import aeneas.models.Bullpen.BullpenLogic;
import aeneas.models.Bullpen;
import aeneas.models.Board;
import aeneas.models.Level;
import aeneas.models.Level.LevelWithMoves;
import aeneas.models.LightningLevel;
import aeneas.models.PuzzleLevel;
import aeneas.models.ReleaseLevel;

public class SetSizeMoveTest {

  Level level;

  @Before
  public void setUp() throws Exception {
    level = new PuzzleLevel(new Bullpen(BullpenLogic.editorLogic()));
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testSetSizeNormal() {
    int rows = 8, cols = 5;
    IMove move = new SetSizeMove(level, rows, cols);
    assertTrue(move.execute());

    boolean[][] squares = level.getBoard().getSquares();
    for (int i = 0; i < Board.SIZE; ++i) {
      for (int j = 0; j < Board.SIZE; ++j) {
        if (i < rows && j < cols) assertTrue("("+i+", "+j+") should be true", squares[i][j]);
        else assertFalse("("+i+", "+j+") should be false", squares[i][j]);
      }
    }

    move.undo();

    for (int i = 0; i < Board.SIZE; ++i) {
      for (int j = 0; j < Board.SIZE; ++j) {
        assertTrue(squares[i][j]);
      }
    }
  }

  @Test
  public void testBadInput() {
    IMove move = new SetSizeMove(level, -10, 5);
    assertFalse(move.isValid());
    assertFalse(move.execute());
  }

}

