package aeneas.models;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import aeneas.controllers.IMove;
import aeneas.controllers.ToggleTileMove;
import aeneas.models.Bullpen.BullpenLogic;

public class TestBoard {
  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    Board b = new PuzzleBoard();
    Piece p = new Piece(new Square[] { new Square(0,0) });
    PlacedPiece pp = new PlacedPiece(p, 0, 0);
    boolean success = b.addPiece(pp);
    assertTrue(success);
    assertEquals(b.numValidSquares(), 144);
    assertEquals(b.pieces.size(), 1);
    assertTrue(b.intersects(pp));

    assertEquals(b.getPieceAtLocation(0, 0), pp);
  }

  @Test
  public void testLightning() {
    LightningBoard b = new LightningBoard();
    Piece p = new Piece(new Square[] { new Square(0,0) });
    PlacedPiece pp = new PlacedPiece(p, 0, 0);
    boolean success = b.addPiece(pp);
    assertTrue(success);
    assertEquals(b.numValidSquares(), 144);
    assertEquals(b.numCoveredSquares(), 1);
    assertEquals(b.pieces.size(), 0);
    assertFalse(b.intersects(pp));
  }

  @Test
  public void testRelease() {


    ReleaseLevel l = new ReleaseLevel(new Bullpen(BullpenLogic.releaseLogic()));
    ((ReleaseBoard)l.getBoard()).getNumbers().add(new ReleaseNumber(0, 0, ReleaseNumber.color1, 1));
    ((ReleaseBoard)l.getBoard()).getNumbers().add(new ReleaseNumber(1, 0, ReleaseNumber.color2, 1));
    ((ReleaseBoard)l.getBoard()).getNumbers().add(new ReleaseNumber(2, 0, ReleaseNumber.color3, 1));

    Piece p = new Piece(new Square[] { new Square(0,0) });
    PlacedPiece pp = new PlacedPiece(p, 0, 0);

    assertEquals(3, l.numUncoveredNumberSets());
    assertEquals(0, l.getStarsEarned());

    l.getBoard().addPiece(pp);

    assertEquals(2, l.numUncoveredNumberSets());
    assertEquals(1, l.getStarsEarned());



    Piece p2 = new Piece(new Square[] { new Square(0,0) });
    PlacedPiece pp2 = new PlacedPiece(p2, 1, 0);
    l.getBoard().addPiece(pp2);
    assertEquals(1, l.numUncoveredNumberSets());
    assertEquals(2, l.getStarsEarned());

    Piece p3 = new Piece(new Square[] { new Square(0,0) });
    PlacedPiece pp3 = new PlacedPiece(p3, 2, 0);
    l.getBoard().addPiece(pp3);
    assertEquals(0, l.numUncoveredNumberSets());
    assertEquals(3, l.getStarsEarned());

  }

  @Test
  public void testToggleSquare() {
    Bullpen b = new Bullpen(BullpenLogic.editorLogic());
    Model model = new Model();
    Level l = new PuzzleLevel(b);
    model.setActiveLevel(l);
    IMove m = new ToggleTileMove(model, 0, 2);
    boolean success = m.execute();
    assertTrue(success);
    assertFalse(l.getBoard().locationValid(new Square(0, 2)));
    success = m.undo();
    assertTrue(success);
    assertTrue(l.getBoard().locationValid(new Square(0, 2)));
    m.execute();
    success = m.execute();
    assertTrue(success);
    assertTrue(l.getBoard().locationValid(new Square(0, 2)));
  }
}
