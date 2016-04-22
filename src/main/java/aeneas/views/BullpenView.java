package aeneas.views;

import java.util.ArrayList;

import com.jfoenix.effects.JFXDepthManager;

import aeneas.models.Bullpen;
import aeneas.models.Model;
import aeneas.models.Piece;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BullpenView {

  VBox bullpenBox;
  Pane levelView ;

  Bullpen bullpen;

  static final int SQUARE_SIZE = 16;

  ArrayList<Pane> values = new ArrayList<Pane>();

  public BullpenView(VBox bullpenBox, Pane levelView){
    this.levelView = levelView;
    this.bullpen = bullpen;
    this.bullpenBox = bullpenBox;
    JFXDepthManager.setDepth(bullpenBox, 1);
    bullpenBox.setAlignment(Pos.TOP_CENTER);

    ArrayList<Piece> pieces = bullpen.getPieces();
    for (Piece p : pieces){
      addPiece(p);
    }
  }

  void addPiece(Piece piece, Model model){
    Pane piecePane = new Pane();
    PieceView pieceView = new PieceView(levelView, piece, model, SQUARE_SIZE);
    pieceView.setId(piece.toString()); //this relies on all instances having different to strings
    piecePane.getChildren().add(pieceView);
    values.add(piecePane);
    bullpenBox.getChildren().add(piecePane);
  }
}
