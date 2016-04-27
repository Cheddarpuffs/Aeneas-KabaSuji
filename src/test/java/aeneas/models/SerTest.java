package aeneas.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import aeneas.models.Bullpen.BullpenLogic;

public class SerTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testPuzzleLevelSerialize() {
    PuzzleLevel s = new PuzzleLevel(new Bullpen(BullpenLogic.puzzleLogic()), false);
    s.setAllowedMoves(10);
    File file = new File("build/puzzle.ksb");

    try {
      s.save(file);
    } catch (IOException i) {
      i.printStackTrace();
      fail("Exception occurred on saving file.");
    }

    Level d = null;
    try {
      d = Level.loadLevel(file);
    } catch (IOException i) {
      i.printStackTrace();
      fail("Exception occurred on loading file.");
    }

    assertFalse(d instanceof LightningLevel);
    assertFalse(d instanceof ReleaseLevel);
    assertTrue(d instanceof PuzzleLevel);

    PuzzleLevel pl = (PuzzleLevel)d;

    assertEquals(pl.getAllowedMoves(), 10);
  }

  @Test
  public void testLightningSerialize() {
    Bullpen bullpen = new Bullpen(BullpenLogic.lightningLogic());
    bullpen.addPiece(new Piece(new Square[] {
        new Square(0,0),
        new Square(1,0),
        new Square(2,0),
        new Square(3,0),
        new Square(4,0),
        new Square(5,0),
    }));
    LightningLevel s = new LightningLevel(bullpen, 10);
    File file = new File("build/lightning.ksb");

    try {
      s.save(file);
    } catch (IOException i) {
      i.printStackTrace();
      fail("Exception occurred on saving file.");
    }

    Level d = null;
    try {
      d = Level.loadLevel(file);
    } catch (IOException i) {
      i.printStackTrace();
      fail("Exception occurred on loading file.");
    }

    assertTrue(d instanceof LightningLevel);
    assertFalse(d instanceof ReleaseLevel);
    assertFalse(d instanceof PuzzleLevel);

    LightningLevel ll = (LightningLevel)d;

    assertEquals(ll.allowedTime, 10);
    assertEquals(1, ll.getBullpen().getPieces().size());

    Piece p = ll.getPieces().get(0);
    for(int i = 0; i < 6; i++) {
      assertEquals(0, p.getSquares()[i].getCol());
      assertEquals(i, p.getSquares()[i].getRow());
    }
  }

  @Test
  public void testMetadata() {
    Model m = new Model();
    m.getMetadata(m.getLevel(2)).setLocked(false);
    assertFalse(m.getMetadata(m.getLevel(2)).isLocked());
    File file = new File("build/test_metadata.dat");

    try {
      m.saveLevelMetadata(file);
    } catch (IOException e) {
      e.printStackTrace();
      fail("Failed to write file");
    }

    m = new Model();
    try {
      m.loadLevelMetadata(file);
    } catch (IOException e) {
      e.printStackTrace();
      fail("Failed to read file");
    }

    assertFalse(m.getMetadata(m.getLevel(2)).isLocked());
  }
}
