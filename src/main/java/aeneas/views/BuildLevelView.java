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
import aeneas.controllers.ChildDraggedListener;
import aeneas.controllers.IMove;
import aeneas.controllers.UndoRedoController;
import aeneas.controllers.ToggleTileMove;
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
public class BuildLevelView extends StackPane implements Initializable {

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

  private BoardView boardView;
  private Level levelModel;
  private MainView mainView;
  private BullpenView bullpenView;
  private LevelWidgetView levelView;
  private ArrayList<LevelWidgetView> levelViews;
  private Model model;
  private UndoRedoController undoController;

  BuildLevelView(MainView mainView, ArrayList<LevelWidgetView> levelViews, LevelWidgetView levelView, Model model) {
    this.levelView = levelView;
    this.levelViews = levelViews;
    this.model = model;
    this.levelModel = levelView.getLevelModel();
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

  public void refreshAll(){
    boardView.refresh();
    bullpenView.refresh(levelModel.getBullpen());
    levelView.updateValues();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.boardView = new BoardView(this, model,levelModel, levelModel.getBoard());
    this.bullpenView = new BullpenView(model, bullpenBox, levelModel, (Pane) this);

    VBox.setMargin(boardView, new Insets(10, 10, 10, 10));
    centerBox.setAlignment(Pos.TOP_RIGHT);
    centerBox.getChildren().add(boardView);

    saveButton.setOnMouseClicked((e) -> {
      File saveFile = mainView.showSaveDialog();
      if (saveFile == null)
        return;
      try {
        // We retrieve the current level live, because the current
        // level will change over time.
        this.levelModel.save(saveFile);
      } catch (IOException i) {
        System.out.println("Error occurred in opening file.");
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
      if (new_toggle != null) {
        LevelWidgetView view = (LevelWidgetView) ((RadioButton) new_toggle).getUserData();
        this.levelModel = view.getLevelModel();
        this.settingsBox.getChildren().set(1, view.getPanel());
        undoController = new UndoRedoController(this, levelModel);
        mainView.setOnKeyPressed(undoController );
        undoButton.setOnMouseClicked((e)-> {
          undoController.undoMove();
        });
      }
    });

    piecePickerDialog.setTransitionType(DialogTransition.CENTER);

    boardView.setSquareClickListener((row, col) -> {
      IMove m = new ToggleTileMove(levelModel, row, col);
      if (m.isValid()) {
        m.execute();
        levelModel.addNewMove(m);
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
        PieceView pView = new PieceView((Pane) this, pieceModel, levelModel, PIECE_PICKER_SQUARE_SIZE);
        piecesPane.getChildren().add(pView);

        pView.setOnMouseClicked((click) -> {
          IMove move = new AddPieceMove(levelModel.getBullpen(), pieceModel.clone());
          if (move.execute()){
            levelModel.addNewMove(move);
            bullpenView.refresh(levelModel.getBullpen());
          }
        });
      }
    });
    undoController = new UndoRedoController(this, levelModel);
    mainView.setOnKeyPressed(undoController );
    undoButton.setOnMouseClicked((e)-> {
      undoController.undoMove();
    });
    redoButton.setOnMouseClicked((e)-> {
      undoController.redoMove();
    });

  }
}
