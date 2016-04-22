package aeneas.models;

import java.util.ArrayList;

/**
 * A bullpen is a list of pieces available to the player.
 *
 * @author Joseph Martin
 */
public class Bullpen implements java.io.Serializable {

  /**
   * @return the logic
   */
  public BullpenLogic getLogic() {
    return logic;
  }

  public static class BullpenLogic {
    private boolean canReturnPiece;
    private boolean canAddNewPiece;

    public static BullpenLogic editorLogic() {
      BullpenLogic b = new BullpenLogic();
      b.canAddNewPiece = true;
      b.canReturnPiece = true;
      return b;
    }

    public static BullpenLogic puzzleLogic() {
      BullpenLogic b = new BullpenLogic();
      b.canAddNewPiece = false;
      b.canReturnPiece = true;
      return b;
    }

    public static BullpenLogic lightningLogic() {
      BullpenLogic b = new BullpenLogic();
      b.canAddNewPiece = false;
      b.canReturnPiece = false;
      return b;
    }

    public static BullpenLogic releaseLogic() {
      BullpenLogic b = new BullpenLogic();
      b.canAddNewPiece = false;
      b.canReturnPiece = false;
      return b;
    }

    /**
     * @return the canReturnPiece
     */
    public boolean isCanReturnPiece() {
      return canReturnPiece;
    }

    /**
     * @return the canAddNewPiece
     */
    public boolean isCanAddNewPiece() {
      return canAddNewPiece;
    }
  }

  ArrayList<Piece> pieces;
  BullpenLogic logic;

  /**
   * Construct a bullpen with an empty list of pieces.
   */
  public Bullpen(BullpenLogic logic) {
    this.pieces = new ArrayList<Piece>();
    this.logic = logic;
  }

  /**
   * Construct a bullpen with the given list of pieces
   * @param pieces The initial list of pieces for the bullpen to contain.
   */
  public Bullpen(BullpenLogic logic, ArrayList<Piece> pieces) {
    this.pieces = pieces;
    this.logic = logic;
  }

  /**
   * Adds a piece to he bullpen
   * @param piece the piece to add
   */
  public void addPiece(Piece piece) {
    this.pieces.add(piece);
  }

  /**
   * Removes the given piece from the bullpen, if present.
   * @param piece the piece to remove.
   * @return true if piece was found and removed, false otherwise.
   */
  public boolean removePiece(Piece piece) {
    return this.pieces.remove(piece);
  }

  /**
   * @return the pieces
   */
  public ArrayList<Piece> getPieces() {
    return pieces;
  }
}
