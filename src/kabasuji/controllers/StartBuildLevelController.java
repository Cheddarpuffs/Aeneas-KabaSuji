package kabasuji.controllers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import kabasuji.models.Level;
import kabasuji.views.MainView;

// @brief Handles a button press on one button in the build select view
public class StartBuildLevelController implements EventHandler<MouseEvent> {

  Level levelModel;
  MainView view;

  public StartBuildLevelController(MainView view, Level levelModel){
    this.levelModel = levelModel;
    this.view = view;
  }

  @Override
  public void handle(MouseEvent event) {
    view.switchToBuildLevelView();
  }

}
