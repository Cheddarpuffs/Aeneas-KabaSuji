package aeneas.views;

import aeneas.models.ReleaseNumber;
import aeneas.models.Square;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * View class to display a single square
 *
 * @author Logan
 * @author Joseph Martin
 */
public class SquareView extends StackPane {

  public int size = 16;// in pixels
  private Rectangle square;
  private Label l;

  // Square squareModel;

  public SquareView(int size, Square s) {
    this.size = size;
    square = new Rectangle();
    // this.squareModel = squareModel;
    square.setWidth(size);
    square.setHeight(size);

    square.setStroke(Color.BLACK);
    this.getChildren().add(square);
    if (s != null) {
      square.setFill(s.getColor());
      if (s.getNum() != null) {
        setNumber(s.getNum());
      }
    } else {
      square.setFill(Color.BLACK);
    }
  }

  public void setNumber(ReleaseNumber num) {
    // this.getChildren().clear();
    if (num != null) {
      l = new Label(Integer.toString(num.getValue()));
      l.setTextFill(num.getColor());
      l.setFont(new Font(20));
      l.setBackground(new Background(new BackgroundFill(Color.web("#ffffff00"),
          new CornerRadii(2, false), new Insets(0, 0, 0, 0))));
      this.getChildren().clear();
      this.getChildren().add(square);
      this.getChildren().add(l);
    }
    else {
      //TODO: Hax on hax on hax
      this.getChildren().clear();
      this.getChildren().add(square);
    }
  }

  public void setColor(Color color) {
    square.setFill(color);
  }

  public void refresh(Square square) {
    if (square != null) {
      setColor(square.getColor());
      setNumber(square.getNum());
    } else {
      setColor(Color.BLACK);
      setNumber(null);
    }
  }
}
