package aeneas.controllers;
import aeneas.models.Model;
import aeneas.views.BuildLevelView;
import aeneas.views.MainView;

import aeneas.models.Level;
import aeneas.models.Model;
import aeneas.views.BuildLevelView;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

// TODO: Figure out correct type of Event.
public class UndoRedoController implements EventHandler<KeyEvent> {

  Model model;
  BuildLevelView view;

  public UndoRedoController(BuildLevelView view, Model model){
    this.view = view;
    this.model = model;
  }

  @Override
  public void handle(KeyEvent event) {
    if(event.isControlDown() && event.getCode().equals(KeyCode.Z)){
      if(event.isShiftDown())
        redoMove();
      else 
        undoMove();
    }
  }

  /**
   * Undoes a single move if possible
   */
  public void undoMove(){
    if(model.getActiveLevel().undoLastMove()){
      view.refresh();
    }
  }

  /**
   * redoes a single move if possible
   */
  public void redoMove(){
    if(model.getActiveLevel().redoLastMove()){
      view.refresh();
    }

  }
  

}
