package aeneas.models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModelTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testLevels() {
    Model m = new Model();
    assertFalse(m.getMetadata(m.getLevel(1)).isLocked());
    assertEquals(0, m.getMetadata(m.getLevel(1)).getStarsEarned());
    for(int i = 2; i < 15; i++) {
      assertTrue(m.getMetadata(m.getLevel(i)).isLocked());
      assertEquals(0, m.getMetadata(m.getLevel(i)).getStarsEarned());
    }
  }

  @Test
  public void testStars() {
    Model m = new Model();
    Level l = new PuzzleLevel(new Bullpen());
    assertEquals(m.getMetadata(l).getStarsEarned(), 0);
    m.setActiveLevel(l);
    m.updateStats();
    assertEquals(m.getMetadata(l).getStarsEarned(), 0);
  }
}
