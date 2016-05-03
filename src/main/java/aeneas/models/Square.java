package aeneas.models;

import javafx.scene.paint.Color;

/**
 *
 * @author Joseph Martin
 * @author Logan Tutt
 */
public class Square implements java.io.Serializable {
  int dRow, dCol;
  ReleaseNumber num;
  private String color;

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
