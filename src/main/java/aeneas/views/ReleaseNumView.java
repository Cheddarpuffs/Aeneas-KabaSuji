package aeneas.views;

import com.jfoenix.effects.JFXDepthManager;

import aeneas.models.ReleaseNumber;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class ReleaseNumView {
  private Label releaseNumLabel;
  private ReleaseNumber num;
  public static final int W = 30;

  public ReleaseNumView(ReleaseNumber num) {
    this.num = num;
    releaseNumLabel = new Label(String.valueOf(num.getValue()));
    releaseNumLabel.setPadding(new Insets(8, 8, 8, 8));
    releaseNumLabel.setTextAlignment(TextAlignment.CENTER);
    releaseNumLabel.setPrefSize(W, W);
    releaseNumLabel.setTextFill(num.getColor());
    releaseNumLabel.setBackground(new Background(new BackgroundFill(Color.GRAY,
        new CornerRadii(2, false), new Insets(0, 0, 0, 0))));
    JFXDepthManager.setDepth(releaseNumLabel, 1);
  }

  public int getValue() {
    return num.getValue();
  }

  public Color getColor() {
    return num.getColor();
  }

  public void setValue(int value) {
    num.setValue(value);
    releaseNumLabel.setText(String.valueOf(value));
  }

  public void setColor(Color color) {
    num.setColor(color);
    releaseNumLabel.setTextFill(color);
  }

  public Node getNode() {
    return releaseNumLabel;
  }

  public ReleaseNumber cloneNumber() {
    return num.clone();
  }
}