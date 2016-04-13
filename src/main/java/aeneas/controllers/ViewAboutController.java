package aeneas.controllers;

import aeneas.views.MainView;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

// @brief Handles displaying help text on button text.
// Furthermore, will cause MainView to ignore mouse events.
public class ViewAboutController implements EventHandler<MouseEvent> {

  String aboutText;
  MainView view;

  public ViewAboutController(MainView view, String aboutText){
    this.aboutText = aboutText;
    this.view = view;
  }

  @Override
  public void handle(MouseEvent event) {
    System.out.println(aboutText);
    // Because who would *ever* want to _do_ anything?
    view.setMouseTransparent(true);
  }

}
