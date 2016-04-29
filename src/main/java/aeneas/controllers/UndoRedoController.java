package aeneas.controllers;

import aeneas.models.Model;
import aeneas.views.BuildLevelView;
import aeneas.views.MainView;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

// TODO: Figure out correct type of Event.
public class UndoRedoController implements EventHandler<MouseEvent> {

  Model model;
  BuildLevelView view;

  public UndoRedoController(BuildLevelView view, Model model){
    this.model = model;
    this.view = view;
  }

  @Override
  public void handle(MouseEvent event) {
  }

  public void doUndo() {
    if (model.undoLastMove()) view.refresh();
  }

  public void doRedo() {
    if (model.redoLastMove()) view.refresh();
  }

}
