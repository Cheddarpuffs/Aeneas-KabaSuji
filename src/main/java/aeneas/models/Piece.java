package aeneas.models;

import javafx.scene.paint.Color;
import javafx.scene.input.DataFormat;

/**
 *
 * @author Joseph Martin, Garrison
 * @author Logan
 */
public class Piece implements java.io.Serializable {
  public enum Axis {
    VERTICAL,
    HORIZONTAL,
  }

  public enum Dir {
    CLOCKWISE,
    COUNTERCLOCKWISE,
  }

  public static final DataFormat dataFormat = new DataFormat("aeneas.Piece");

  Square squares[];
  private int width;
  private int height;
  public boolean inBullpen;
  private boolean hint;
  private String color;


  /**
   * Constructor
   * @param squares squares to make the piece from
   */
  public Piece(Square[]squares){
    this(squares, Color.BLUE);
  }

  /**
   * Constructor
   * @param squares
   * @param color
   */
  public Piece(Square[] squares, Color color) {
    this.squares = squares;
    this.color = color.toString();
    width = 0;
    height = 0;
    for (Square s : squares){
      s.setColor(color);
      if(s.getCol() > width)
        width = s.getCol();
      if(s.getRow() > height)
        height = s.getRow();
    }
    width++;
    height++;
    //this should be removed once the actual adding of pieces is implemented
    inBullpen = true;
  }

  /**
   * flips the piece over the specified axis
   * @param axis axis to flip over
   */
  public void flip(Axis axis) {
    for(Square s : squares) {
      switch(axis) {
      case VERTICAL:
        s.setCol(-s.getCol()+getWidth()-1);
        break;
      case HORIZONTAL:
        s.setRow(-s.getRow()+getHeight()-1);
        break;
      }
    }
  }

  /**
   * rotates in  the given direction
   * @param direction direction to rotate
   */
  public void rotate(Dir direction) {
    for(Square s : squares) {
      int row = s.getRow();
      int col = s.getCol();
      switch(direction) {
      case CLOCKWISE:
        s.setCol(-row+getHeight()-1);
        s.setRow(col);
        break;
      case COUNTERCLOCKWISE:
        s.setCol(row);
        s.setRow(-col+getWidth()-1);
        break;
      }
    }
    int temp = height;
    height = width;
    width = temp;
  }

  @Override
  public String toString(){
    String str = "[";
    for (Square s : squares){
      str += s.toString() + ",";
    }
    return str + "]";
  }

  public Square[] getSquares() {
    return squares;
  }

  public int getWidth(){
    return width;
  }

  public int getHeight(){
    return height;
  }


  public Color getColor(){ return Color.web(color);}

  public void setColor(Color c) {
    for (Square s : squares){
      s.setColor(c);
    }
   this.color = c.toString();
  }


  @Override
  /**
   * deep copy
   */
  public Piece clone(){
    Square cloneSquares[] = new Square[this.squares.length];
    for (int i=0;i<this.squares.length;i++){
      Square s = this.squares[i];
      cloneSquares[i] = s.clone();
    }

    Piece clone = new Piece(cloneSquares);
    clone.width = this.width;
    clone.height = this.height;
    clone.inBullpen = this.inBullpen;
    return clone;
  }

  public boolean isHint() {
    return hint;
  }

  public void setHint(boolean hint) {
    this.hint = hint;
    this.setColor(Color.CORNSILK);
  }
}
