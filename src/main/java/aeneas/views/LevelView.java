package aeneas.views;

import aeneas.models.Level;

import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;

public abstract class LevelView {

  protected VBox panel;
  public Level levelModel;

  public LevelView(Level levelModel){
    this.levelModel = levelModel;
    panel = new VBox();
    panel.setAlignment(Pos.TOP_LEFT);
    panel.setSpacing(10);
    panel.setMinWidth(125);
  }

  /**
   * @return the panel
   */
  public VBox getPanel() {
    return panel;
  }
  
  public abstract RadioButton getButton();
}
