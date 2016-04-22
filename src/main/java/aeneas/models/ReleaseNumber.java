package aeneas.models;

import javafx.scene.paint.Color;

/**
 * 
 * @author Joseph Martin
 */
public class ReleaseNumber implements java.io.Serializable {

  public static Color color1 = Color.RED;
  public static Color color2 = Color.GREEN;
  public static Color color3 = Color.YELLOW;



  /** row coordinate */
  int row;
  /** Column coordinate */
  int col;
  /** Number color */
  Color color;
  /** Value of this number */
  int val;

  public ReleaseNumber(int row, int col, Color color, int val){
    this.row = row;
    this.col = col;
    this.color = color;
    this.val = val;
  }

  public int getRow() { return row; }
  public int getCol() { return col; }
  public Color getColor() { return color; }
  public int getValue() { return val; }


  public void setRow(int row) { this.row = row; }
  public void setCol(int col) { this.col = col; }
  public void setColor(Color color) { this.color = color; }
  public void setValue(int val) { this.val = val; }
}
