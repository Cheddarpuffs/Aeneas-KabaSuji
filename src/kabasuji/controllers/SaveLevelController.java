package kabasuji.controllers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import kabasuji.models.Level;
import kabasuji.views.BuildLevelView;

// Not sure what sort of event this ends up taking.
public class SaveLevelController implements EventHandler<MouseEvent> {

  Level levelModel;
  BuildLevelView view;

  public SaveLevelController(BuildLevelView view, Level levelModel){
    this.levelModel = levelModel;
    this.view = view;
  }

  @Override
  public void handle(MouseEvent event) {
  }

}