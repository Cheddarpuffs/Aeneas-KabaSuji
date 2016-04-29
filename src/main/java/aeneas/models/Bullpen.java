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

  public static class BullpenLogic implements java.io.Serializable {
    private boolean canReturnPiece;
    private boolean canAddNewPiece;
    private boolean isRandom;

    public static BullpenLogic editorLogic() {
      BullpenLogic b = new BullpenLogic();
      b.canAddNewPiece = true;
      b.canReturnPiece = true;
      b.isRandom = false;
      return b;
    }

    public static BullpenLogic puzzleLogic() {
      BullpenLogic b = new BullpenLogic();
      b.canAddNewPiece = false;
      b.canReturnPiece = true;
      b.isRandom = false;
      return b;
    }

    public static BullpenLogic lightningLogic() {
      BullpenLogic b = new BullpenLogic();
      b.canAddNewPiece = false;
      b.canReturnPiece = false;
      b.isRandom = true;
      return b;
    }

    public static BullpenLogic releaseLogic() {
      BullpenLogic b = new BullpenLogic();
      b.canAddNewPiece = false;
      b.canReturnPiece = false;
      b.isRandom = false;
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
      // Just assume that this is always allowed...
      return true;
    }

    public boolean isRandom() {
      return isRandom;
    }
  }

  ArrayList<Piece> pieces;
  BullpenLogic logic;

  /**
   * Construct a bullpen with an empty list of pieces.
   * @param logic the type of logic
   */
  public Bullpen(BullpenLogic logic) {
    this.pieces = new ArrayList<Piece>();
    this.logic = logic;
  }

  /**
   * Construct a bullpen with the given list of pieces
   * @param pieces The initial list of pieces for the bullpen to contain.
   * @param logic the type of logic
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

  /**
   * Create a copy of the bullpen.
   */
  @Override
  public Object clone() {
    Bullpen newBullpen = new Bullpen(this.logic);
    for (Piece piece: this.pieces) {
      newBullpen.pieces.add(piece.clone());
    }
    return newBullpen;
  }
}
