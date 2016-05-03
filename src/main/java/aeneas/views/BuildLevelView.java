package aeneas.views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXListView;

import aeneas.controllers.AddPieceMove;
import aeneas.controllers.BoardSizeController;
import aeneas.controllers.ChangeLevelTypeMove;
import aeneas.controllers.ChildDraggedListener;
import aeneas.controllers.DeleteBoardPieceMove;
import aeneas.controllers.DeleteBullpenPieceMove;
import aeneas.controllers.IMove;
import aeneas.controllers.ToggleTileMove;
import aeneas.controllers.UndoRedoController;
import aeneas.models.Level;
import aeneas.models.Model;
import aeneas.models.Piece;
import aeneas.models.PieceFactory;
import aeneas.models.Square;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Display the level builder.
 *
 * @author pmitrano
 * @author jbkuszmaul
 * @author Joseph Martin
 */
public class BuildLevelView extends StackPane implements Initializable, RefreshListener {

  private static final int PIECE_PICKER_SQUARE_SIZE = 12;

  @FXML
  private JFXDialog piecePickerDialog;

  @FXML
  private FlowPane piecesPane;

  @FXML
  private JFXListView<Pane> bullpenListView;

  @FXML
  private Label levelLabel;

  @FXML
  private JFXButton addPiece;

  @FXML
  private FontAwesomeIconView levelTypeIcon;

  @FXML
  private VBox bullpenBox;

  @FXML
  private VBox centerBox;

  @FXML
  private JFXButton saveButton;

  @FXML
  private JFXButton undoButton;

  @FXML
  private JFXButton redoButton;

  @FXML
  private HBox settingsBox;

  @FXML
  private ToggleGroup levelType;

  @FXML
  private VBox togglesBox;

  @FXML
  private Spinner<Integer> rowSpinner;

  @FXML
  private Spinner<Integer> columnSpinner;

  @FXML
  private FontAwesomeIconView deletePiece;

  private BoardView boardView;
  private MainView mainView;
  private BullpenView bullpenView;
  private LevelWidgetView levelView;
  private ArrayList<LevelWidgetView> levelViews;
  private Model model;
  private UndoRedoController undoController;
  private boolean isRefreshing = false;

  BuildLevelView(MainView mainView, ArrayList<LevelWidgetView> levelViews, Level level, Model model) {
    this.levelView = level.makeCorrespondingView(model);
    this.levelViews = levelViews;
    this.model = model;
    model.setActiveLevel(level);
    this.mainView = mainView;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("BuildLevel.fxml"));
      loader.setRoot(this);
      loader.setController(this);
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.boardView = new BoardView((Pane)this, model);
    this.bullpenView = new BullpenView(model, bullpenBox, (Pane) this);
    bullpenView.refresh();
    undoController = new UndoRedoController(this, model);

    VBox.setMargin(boardView, new Insets(10, 10, 10, 10));
    centerBox.setAlignment(Pos.TOP_RIGHT);
    centerBox.getChildren().add(boardView);

    rowSpinner.valueProperty().addListener(new BoardSizeController(this));
    columnSpinner.valueProperty().addListener(new BoardSizeController(this));

    saveButton.setOnMouseClicked((e) -> {
      File saveFile = mainView.showSaveDialog(this.model.getActiveLevel().getLevelNumber());
      if (saveFile == null)
        return;
      try {
        // We retrieve the current level live, because the current
        // level will change over time.
        this.model.getActiveLevel().save(saveFile);
      } catch (IOException i) {
        System.out.println("Error occurred in saving file.");
      }
    });

    for (LevelWidgetView tempLevelView : levelViews) {
      tempLevelView.getButton().setToggleGroup(levelType);
      togglesBox.getChildren().add(tempLevelView.getButton());
    }

    // set the right settings got the given level type
    this.settingsBox.getChildren().add(1, this.levelView.getPanel());
    this.levelView.getButton().setSelected(true);

    // Handle changes in the level type.
    // TODO: Consider moving this to a separate class.
    levelType.selectedToggleProperty()
    .addListener((ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) -> {
      if (new_toggle != null && !toggle.equals(new_toggle)) {
        LevelWidgetView view = (LevelWidgetView) ((RadioButton) new_toggle).getUserData();
        Level newLevel = view.resetLevelModel(this.model.getActiveLevel());
        IMove move = new ChangeLevelTypeMove(this.model, newLevel);
        if (move.execute()) model.getActiveLevel().addNewMove(move);
        this.settingsBox.getChildren().set(1, view.getPanel());
        this.levelView = view;
        this.boardView.refresh();
      }
    });

    piecePickerDialog.setTransitionType(DialogTransition.CENTER);

    boardView.setSquareClickListener((row, col) -> {
      IMove m = new ToggleTileMove(model, row, col);
      if (m.isValid()) {
        m.execute();
        model.getActiveLevel().addNewMove(m);
        boardView.refresh();
      }
    });

    //if the user commits to dragging a piece out of the dialog then we close the dialog
    piecesPane.setOnDragExited((e) -> {
      piecePickerDialog.close();
    });

    addPiece.setOnMouseClicked((e) -> {
      piecePickerDialog.show(this);
      piecesPane.getChildren().clear();

      for (Piece pieceModel : PieceFactory.getPieces()) {
        PieceView pView = new PieceView((Pane) this, pieceModel, model.getActiveLevel(), PIECE_PICKER_SQUARE_SIZE);
        piecesPane.getChildren().add(pView);

        pView.setOnChildDraggedListener(new ChildDraggedListener() {
          @Override
          public void onSquareDragged(Square squareView) {
          }

          @Override
          public void onPieceDragged(PieceView pieceView) {
            model.setLatestDragSource(null);
          }
        });

        pView.setOnMouseClicked((click) -> {
          IMove move = new AddPieceMove(model.getActiveLevel().getBullpen(), pieceModel.clone());
          if (move.execute()){
            model.getActiveLevel().addNewMove(move);
            bullpenView.refresh();
          }
        });
      }
    });

    mainView.setOnKeyPressed(undoController );
    undoButton.setOnMouseClicked((e)-> {
      undoController.undoMove();
    });
    redoButton.setOnMouseClicked((e)-> {
      undoController.redoMove();
    });

    deletePiece.setOnDragDropped((DragEvent event) -> {
      Dragboard db = event.getDragboard();

      // use this to draw the piece on the board
      Piece piece = (Piece) db.getContent(Piece.dataFormat);

      DragSource source = model.getLatestDragSource();

      IMove move = null;

      if (source instanceof BoardView) {
        BoardView v = (BoardView)source;
        move = new DeleteBoardPieceMove(model, v.getLastDraggedPiece());
      }
      else if (source instanceof BullpenView){
        BullpenView v = (BullpenView)source;
        move = new DeleteBullpenPieceMove(model, piece);
      }
      if (move != null) {
        if (!move.execute()){
          model.getLatestDragSource().returnDraggableNode();
        } else {
          model.getLatestDragSource().dragSuccess();
          model.getActiveLevel().addNewMove(move);
        }
      }
      refresh();

      // this might change we we actually implement it,
      // such as if they drop it on a square that doesn't exist
      event.setDropCompleted(true);
      event.consume();

    });

    // this is absolutely nessecary
    deletePiece.setOnDragOver((DragEvent event) -> {
      event.acceptTransferModes(TransferMode.MOVE);
      event.consume();
    });
  }


  public void refresh() {
    isRefreshing = true;
    boardView.refresh();
    bullpenView.refresh();
    this.levelType.selectToggle(model.getActiveLevel().getButton());
    this.levelView = (LevelWidgetView)levelType.getSelectedToggle().getUserData();
    this.levelView.updateValues();
    this.rowSpinner.getValueFactory().setValue(model.getActiveLevel().getBoard().getRows());
    this.columnSpinner.getValueFactory().setValue(model.getActiveLevel().getBoard().getCols());
    isRefreshing = false;
  }

  public boolean isRefreshing(){return isRefreshing;}

  public Spinner<Integer> getRowSpinner() {
    return rowSpinner;
  }

  public Spinner<Integer> getColumnSpinner() {
    return columnSpinner;
  }

  public Level getLevelModel() {
    return model.getActiveLevel();
  }

  public void setLevelModel(Level level) {
    model.setActiveLevel(level);
  }

  public Model getModel(){
    return model;
  }
}
