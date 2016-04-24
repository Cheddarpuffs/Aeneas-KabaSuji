package aeneas.views;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class NavigationTest extends ApplicationTest {

  MainView mainView;

  @Override
  public void start(Stage stage) {
    mainView = new MainView(stage);
    Scene scene = new Scene(mainView, 800, 600);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  public void testNavigate() {
    clickOn("#playSelectLevelButton");
    clickOn("#back");
    clickOn("#viewAchievementsButton");
    clickOn("#back");
    clickOn("#buildSelectLevelButton");
    clickOn("#createNewLevelLabel");
    clickOn("#back");
    clickOn("#back");

    //hit back again to see if it breaks something
    clickOn("#back");

    //test help and about
    clickOn("#optionsBurger");

    //move off the stupid scrollbar...
    moveBy(-20,-10);
    clickOn(MouseButton.PRIMARY);

    //wait so humans can see stuff
    sleep(1, TimeUnit.SECONDS);
    clickOn("#accept");

    clickOn("#optionsBurger");
    moveBy(-20,25);

    //move off the stupid scrollbar...
    clickOn(MouseButton.PRIMARY);

    //wait so humans can see stuff
    sleep(1, TimeUnit.SECONDS);
    clickOn("#accept");
  }
}
