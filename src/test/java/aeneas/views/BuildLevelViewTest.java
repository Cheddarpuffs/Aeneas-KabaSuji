package aeneas.views;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.testfx.framework.junit.ApplicationTest;

import aeneas.models.Level.LevelWithMoves;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class BuildLevelViewTest extends ApplicationTest {
  MainView mainView;
  @Override
  public void start(Stage stage) {
    mainView = new MainView(stage);
    Scene scene = new Scene(mainView, 800, 600);
    stage.setScene(scene);
    stage.show();
    mainView.switchToBuildLevelView();
  }

  @Test
  public void testIncrementMoves() {
    // given:
    clickOn("#puzzleRadio");
    clickOn("#movesSelect").type(KeyCode.DIGIT4).type(KeyCode.ENTER);

    // when:

    // then:
    assertEquals(4, ((LevelWithMoves)mainView.getBuildLevelView().getLevel()).getAllowedMoves());
  }
}
