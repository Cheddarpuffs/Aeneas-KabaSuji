package kabasuji.controllers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import kabasuji.views.MainView;

// @brief Handles a button press on one button in the level select view
public class ViewAchievementsController implements EventHandler<MouseEvent> {
  
  class Achievement{}

  Achievement[] achievements;
  MainView view;

  public ViewAchievementsController(MainView view, Achievement[] achievements){
    this.achievements = achievements;
    this.view = view;
  }

  @Override
  public void handle(MouseEvent event) {
    view.switchToViewAchievementsView();
  }

}
