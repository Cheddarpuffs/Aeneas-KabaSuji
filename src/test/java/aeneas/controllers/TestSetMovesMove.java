package aeneas.controllers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import aeneas.models.Bullpen;
import aeneas.models.Bullpen.BullpenLogic;
import aeneas.models.Level;
import aeneas.models.Level.LevelWithMoves;
import aeneas.models.LightningLevel;
import aeneas.models.PuzzleLevel;
import aeneas.models.ReleaseLevel;

public class TestSetMovesMove {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testNullFails() {
    LevelWithMoves nullLevel = null;
    IMove move = new SetMovesMove(nullLevel, 100);
    assertFalse(move.isValid());
    assertFalse(move.execute());
  }

  // Note: testPuzzleLevel() and testReleasELevel() will
  // fail until the Level models are fully implemented.
  @Test
  public void testPuzzleLevel() {
    PuzzleLevel test = new PuzzleLevel(new Bullpen(BullpenLogic.editorLogic()));
    int oldMoves = 0, newMoves = 100;
    test.setAllowedMoves(0);
    assertEquals(oldMoves, test.getAllowedMoves());
    IMove move = new SetMovesMove(test, 100);
    assertTrue(move.isValid());
    assertTrue(move.execute());
    assertEquals(newMoves, test.getAllowedMoves());
    assertTrue(move.undo());
    assertEquals(oldMoves, test.getAllowedMoves());
  }

  @Test
  public void testReleaseLevel() {
    ReleaseLevel test = new ReleaseLevel(new Bullpen(BullpenLogic.editorLogic()), null);
    int oldMoves = 0, newMoves = 100;
    test.setAllowedMoves(oldMoves);
    assertEquals(oldMoves, test.getAllowedMoves());
    IMove move = new SetMovesMove(test, 100);
    assertTrue(move.isValid());
    assertTrue(move.execute());
    assertEquals(newMoves, test.getAllowedMoves());
    assertTrue(move.undo());
    assertEquals(oldMoves, test.getAllowedMoves());
  }

  @Test
  public void testNegativeMoves() {
    PuzzleLevel test = new PuzzleLevel(new Bullpen(BullpenLogic.editorLogic()));
    IMove move = new SetMovesMove(test, -100);
    assertFalse(move.isValid());
    assertFalse(move.execute());
  }
}
