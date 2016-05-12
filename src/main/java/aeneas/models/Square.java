package aeneas.models;

import javafx.scene.paint.Color;

/**
 * Represents a square of a piece or board. A square
 * has a row and column, and may have a number and color
 *
 * @author Joseph Martin
 * @author Logan Tutt
 * @author Peter Mitrano
 */
public class Square implements java.io.Serializable {
  int dRow, dCol;
  ReleaseNumber num;
  private String color;

  /**
   * Default color of board squares
   */
  public static final Color DEFAULT_COLOR_1 = Color.web("#cecece");
  public static final Color DEFAULT_COLOR_2 = Color.web("#d2d2d2");

  /**
   * Create a new square
   * @param dRow
   * @param dCol
   * @param num
   * @param color
   */
  public Square(int dRow,int dCol,ReleaseNumber num, Color color){
    this.dRow = dRow;
    this.dCol = dCol;
    this.num = num;
    this.color = color.toString();
  }

  public Square(int dRow, int dCol, Color color){
    this(dRow, dCol,null, color);
  }

  public Square(int dRow, int dCol, ReleaseNumber num) {
    this(dRow, dCol, num, Color.GRAY);
  }

  public Square(int dRow, int dCol) {
    this(dRow, dCol,null, Color.GRAY);
  }

  public int getRow() { return dRow; }
  public int getCol() { return dCol; }
  public ReleaseNumber getNum() { return num; }
  public Color getColor() {return Color.web(color); }

  public void setRow(int row) { dRow = row; }
  public void setCol(int col) { dCol = col; }
  public void setNum(ReleaseNumber num) { this.num = num; }
  public void setColor(Color color) {this.color = color.toString(); }

  public Square clone(){
    return new Square(this.dRow, this.dCol, this.num, this.getColor());
  }


  @Override
  public String toString(){
    return "(" + dRow + "," + dCol + ")";
  }
}
