package aeneas.controllers;
import aeneas.models.ReleaseBoard;
import aeneas.models.ReleaseLevel;
import aeneas.models.ReleaseNumber;

/**
 * Move action to add a number to a releae board
 * For use in the builder
 *
 * @author Logan
 *
 */
public class MoveNumMove implements IMove {


  ReleaseLevel level;

  ReleaseNumber num;
  int row, col;

  private ReleaseNumber new_num;

  /**
   * Constructor
   * @param level the level that is being played
   * @param num the ReleaseNumber to be added
   * @param row the row you're adding a number on
   * @param col the col you're adding a number on
   */
  public MoveNumMove(ReleaseLevel level, ReleaseNumber num, int row, int col) {
    this.level = level;
    this.num = num;
    this.new_num = new ReleaseNumber(row, col, num.getColor(), num.getValue());
    this.row = row;
    this.col = col;
  }

  @Override
  public boolean execute() {
    if (!isValid()) return false;
    ((ReleaseBoard) level.getBoard()).removeNumber(num);
    ((ReleaseBoard) level.getBoard()).addNumber(new_num);
    return true;
  }

  @Override
  public boolean undo() {
    ((ReleaseBoard) level.getBoard()).removeNumber(new_num);
    ((ReleaseBoard) level.getBoard()).addNumber(num);
    return true;
  }

  @Override
  public boolean isValid() {
    return true;
  }

}
