package aeneas.models;

import static org.junit.Assert.*;

import aeneas.Lots;
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
  public void test() {
    Model m = new Model();
//    assertEquals("level count", m.levels.size(), 15);
    Lots.foo();
  }

}
