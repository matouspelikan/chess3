package cvut.fel.pjv.pelikma3.controllers;

import cvut.fel.pjv.pelikma3.SceneManager;
import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import cvut.fel.pjv.pelikma3.elements.EditEventResponder;
import cvut.fel.pjv.pelikma3.elements.GameBoardFX;
import cvut.fel.pjv.pelikma3.elements.editHandlers.EditTileClickHandler;
import cvut.fel.pjv.pelikma3.elements.editHandlers.EditTileDragHandler;
import cvut.fel.pjv.pelikma3.elements.editHandlers.EditTilePressHandler;
import cvut.fel.pjv.pelikma3.elements.editHandlers.EditTileReleaseHandler;
import cvut.fel.pjv.pelikma3.elements.tilehandlers.TileDragReleaseHandler;
import cvut.fel.pjv.pelikma3.pgnUtils.PgnParser;
import cvut.fel.pjv.pelikma3.pieces.*;
import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import cvut.fel.pjv.pelikma3.utils.Position;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.controlsfx.control.ToggleSwitch;


import java.util.*;

public class ConfiguratorController implements BoardEventResponder, EditEventResponder {

    public SceneManager sceneManager;

    @FXML private AnchorPane pane;
    @FXML private HBox editBoxUp;
    @FXML private HBox editBoxDown;
    @FXML private VBox boardVbox;
    @FXML private VBox rightVbox;
    @FXML private ToggleSwitch deleteToggleSwitch;
    @FXML private Toggle deleteToggle;
    boolean lastTogglePosition = false;

    Stage stage;

//    List<Piece> editPiecesTop = new ArrayList<>();
//    List<Piece> editPiecesBottom = new ArrayList<>();
//    List<Piece> allPieces = new ArrayList<>();
//    HashMap<String, Image> identifierToImageMap = new HashMap<>();
//    HashMap<String, Piece> identifierToPieceMap = new HashMap<>();

    Color downColor;
    Color upColor;
    int whiteKingsCount;
    int blackKingsCount;
    boolean creatingNewPiece = false;
    Piece pieceBeingCreated = null;

    GameBoard gameBoard;
    GameBoardFX gameBoardFX;
    GameData gameData;

    public ConfiguratorController(){

    }

    public void setParams(GameData gameData, Stage stage){
        this.stage = stage;

        this.upColor = gameData.downColor.otherColor();
        this.downColor = gameData.downColor;
        whiteKingsCount = gameData.emptyBoard ? 0 : 1;
        blackKingsCount = gameData.emptyBoard ? 0 : 1;
        this.gameData = gameData;

//        populateImageMap("piecesoriginal");
        if(gameData.emptyBoard){
            gameBoard = new GameBoard(gameData.downColor.otherColor(), gameData.downColor, true);
        }
        else if(gameData.gameBoard != null){
            gameBoard = gameData.gameBoard;
        }
        else{
            gameBoard = new GameBoard(gameData.downColor.otherColor(), gameData.downColor, false);
        }
        gameBoardFX = new GameBoardFX(pane, boardVbox, stage, gameBoard,
                gameData.identifierToImageMap, gameData.identifierToPieceMap, this);
        createEditBoxes();
//        populatePieceMap();

        adjustDimensions();

        deleteToggleSwitch.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                boolean changed = deleteToggleSwitch.isSelected() ^ lastTogglePosition;
                if(!changed)
                    return;
                lastTogglePosition = deleteToggleSwitch.isSelected();
                if(modeEdit == ModeEdit.CREATING && deleteToggleSwitch.isSelected()){
                    clickedEditPieceWrap.getStyleClass().removeIf(p -> p.toString().equals("selected-tile"));
                    gameBoardFX.setStyleClassWrappers("remove-tile");
                    modeEdit = ModeEdit.DELETING;
                }
                else if(deleteToggleSwitch.isSelected()){
                    gameBoardFX.setStyleClassWrappers("remove-tile");
                    modeEdit = ModeEdit.DELETING;
                }
                else if(!deleteToggleSwitch.isSelected()){
                    gameBoardFX.removeStyleClassWrappers("remove-tile");
                    modeEdit = ModeEdit.NEUTRAL;
                }
            }
        });
    }


    public void adjustDimensions(){
        rightVbox.setMinWidth(250);


    }

    public void createEditBoxes(){
//        editPiecesTop.add(new PiecePawn(upColor));
//        editPiecesTop.add(new PieceKing(upColor));
//        editPiecesTop.add(new PieceRook(upColor));
//        editPiecesTop.add(new PieceBishop(upColor));
//        editPiecesTop.add(new PieceKnight(upColor));
//        editPiecesTop.add(new PieceQueen(upColor));
//
//        editPiecesBottom.add(new PiecePawn(downColor));
//        editPiecesBottom.add(new PieceKing(downColor));
//        editPiecesBottom.add(new PieceRook(downColor));
//        editPiecesBottom.add(new PieceBishop(downColor));
//        editPiecesBottom.add(new PieceKnight(downColor));
//        editPiecesBottom.add(new PieceQueen(downColor));
//
//        allPieces.addAll(editPiecesBottom);
//        allPieces.addAll(editPiecesTop);

        ImageView editDragImageView = new ImageView();
        pane.getChildren().add(editDragImageView);
        MutableBoolean editPiecePressed = new MutableBoolean(false);

        EditTileClickHandler editTileClickHandler = new EditTileClickHandler(this, gameData.getAllPieces());
        EditTilePressHandler editTilePressHandler = new EditTilePressHandler(this, editDragImageView, gameBoard, gameData.identifierToImageMap, editPiecePressed, gameBoardFX.getTileDragReleaseHandler());
        EditTileDragHandler editTileDragHandler = new EditTileDragHandler(this, editDragImageView, editPiecePressed);
        EditTileReleaseHandler editTileReleaseHandler = new EditTileReleaseHandler(this, editDragImageView, gameBoard, gameBoardFX, gameData.identifierToPieceMap,
                editPiecePressed);

        populateEditBox(editBoxDown, downColor.toString().toLowerCase(Locale.ROOT), gameData.getPieceslist(downColor), editTileClickHandler, editTilePressHandler, editTileDragHandler,editTileReleaseHandler);
        populateEditBox(editBoxUp, upColor.toString().toLowerCase(Locale.ROOT), gameData.getPieceslist(upColor), editTileClickHandler, editTilePressHandler, editTileDragHandler, editTileReleaseHandler);

    }

    public enum ModeEdit {
        CREATING, DELETING, NEUTRAL
    }
    ModeEdit modeEdit = ModeEdit.NEUTRAL;
    VBox clickedEditPieceWrap = null;

    public void populateEditBox(HBox editBox, String color, List<Piece> editPieces, EditTileClickHandler editTileClickHandler,
                                EditTilePressHandler editTilePressHandler, EditTileDragHandler editTileDragHandler, EditTileReleaseHandler editTileReleaseHandler){
        ImageView dragEditImageView = new ImageView();
        pane.getChildren().add(dragEditImageView);
        dragEditImageView.setVisible(false);

        for(Piece piece : editPieces){
            VBox pieceWrap = new VBox();
            editBox.getChildren().add(pieceWrap);
            System.out.println(piece.getImageIdentifier());
            ImageView newView = new ImageView(gameData.identifierToImageMap.get(piece.getImageIdentifier()));
//            newView.setFitHeight(50);
//            newView.setFitWidth(50);

            newView.fitHeightProperty().bind(gameBoardFX.tileSizeProperty.divide(3).multiply(2));
            newView.fitWidthProperty().bind(gameBoardFX.tileSizeProperty.divide(3).multiply(2));

            pieceWrap.getChildren().add(newView);
            pieceWrap.getStyleClass().add("promotionBox");
            pieceWrap.setId(piece.getImageIdentifier());
            pieceWrap.getStyleClass().add("edit-tile");
            pieceWrap.setOnMouseClicked(editTileClickHandler);
            pieceWrap.setOnMousePressed(editTilePressHandler);
            pieceWrap.setOnMouseDragged(editTileDragHandler);
            pieceWrap.setOnMouseReleased(editTileReleaseHandler);

            pieceWrap.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    dragEditImageView.startFullDrag();
                }
            });

        }
    }

    public void createPiece(int i, int j){
        String identifier = clickedEditPieceWrap.getId();
        Piece createPiece = gameData.getAllPieces().stream().filter(p -> p.getImageIdentifier().equals(identifier)).findFirst().get();
        Piece newPiece = createPiece.createSelf();
        newPiece.setColor(createPiece.getColor());
        newPiece.setPosition(new Position(i, j));
        this.gameBoard.setBoard(i, j, newPiece);
//        System.out.println(identifier + " " + i + " " + j);
        gameBoardFX.updateUI(i, j);
        createdShadow = null;
    }

    public void deletePiece(int i, int j){
        Piece piece = gameBoard.getPiece(i, j, false);
        if(piece != null){
            this.gameBoard.setBoard(i, j, null);
            gameBoardFX.updateUI(i, j);
        }
    }

    public void createNewPiece(Piece piece, VBox pieceWrap){
        if(piece.pieceType == PieceType.KING &&
                ((piece.getColor() == Color.WHITE && whiteKingsCount > 0) || (piece.getColor() == Color.BLACK && blackKingsCount > 0)))
            return;

        if(this.creatingNewPiece && piece.pieceType == pieceBeingCreated.pieceType && piece.getColor() == pieceBeingCreated.getColor()){
            pieceWrap.getStyleClass().removeIf(p -> p.equals("edit-clicked"));
            this.creatingNewPiece = false;
        }
    }

//    public void populateImageMap(String dirName){
//        String[] piecesNames = {"pawn-white", "pawn-black", "rook-black", "rook-white", "bishop-white", "bishop-black",
//                "king-white", "king-black", "queen-black", "queen-white", "knight-black", "knight-white"};
//        for (String s: piecesNames) {
//            String path = "/" + dirName + "/" + s + ".png";
//            Image newImage = new Image(getClass().getResourceAsStream(path));
//            identifierToImageMap.put(s, newImage);
//        }
//    }
//
//    public void populatePieceMap(){
//        for(Piece piece: allPieces){
//            identifierToPieceMap.put(piece.getImageIdentifier(), piece);
//        }
//    }


    @Override
    public void boardTileClicked(MouseEvent mouseEvent){
        VBox wrapper = (VBox) mouseEvent.getSource();
        String id[] = wrapper.getId().split("-");
//                        logger.info("tile clicked " + id[0] + " " + id[1]);
        int i = Integer.parseInt(id[0]);
        int j = Integer.parseInt(id[1]);

        System.out.println("Clicked " + wrapper.getId());

        if(modeEdit == ModeEdit.CREATING){
            createPiece(i, j);
        }
        else if(modeEdit == ModeEdit.DELETING){
            deletePiece(i, j);
        }
    }

    @Override
    public void boardTilePressed(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed, TileDragReleaseHandler tileDragReleaseHandler){
        if(modeEdit == ModeEdit.DELETING || modeEdit == ModeEdit.CREATING){
            piecePressed.setFalse();
            tileDragReleaseHandler.setCreatedPiece(null, null);
            return;
        }

        VBox wrapper = (VBox) mouseEvent.getSource();
        System.out.println("Pressed " + wrapper.getId());
        String id[] = wrapper.getId().split("-");
        int i = Integer.parseInt(id[0]);
        int j = Integer.parseInt(id[1]);
        Piece piece = gameBoard.getPiece(i, j, false);
        if(piece != null){
            Piece newPiece = piece.createSelf();
            newPiece.setColor(piece.getColor());
            tileDragReleaseHandler.setCreatedPiece(newPiece, new Position(i, j));

            dragImageView.setMouseTransparent(true);
            Image image = gameData.identifierToImageMap.get(piece.getImageIdentifier());
            dragImageView.setImage(image);
            dragImageView.setId(piece.getImageIdentifier());
            dragImageView.setVisible(true);

            dragImageView.setFitWidth(wrapper.getWidth());
            dragImageView.setFitHeight(wrapper.getHeight());
            dragImageView.setLayoutX(mouseEvent.getSceneX() - wrapper.getWidth()/2);
            dragImageView.setLayoutY(mouseEvent.getSceneY() - wrapper.getHeight()/2);
            piecePressed.setTrue();

            gameBoardFX.hideImage(i, j, true);
        }
        else{
            piecePressed.setFalse();
            tileDragReleaseHandler.setCreatedPiece(null, null);
        }
    }

    @Override
    public void boardTileDragged(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed){
        VBox wrapper = (VBox) mouseEvent.getSource();
//        System.out.println("Dragged " + wrapper.getId());
        if(piecePressed.isFalse())
            return;
        dragImageView.setLayoutX(mouseEvent.getSceneX() - dragImageView.getFitWidth()/2);
        dragImageView.setLayoutY(mouseEvent.getSceneY() - dragImageView.getFitHeight()/2);
    }

    @Override
    public void boardTileReleased(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed,
                                  TileDragReleaseHandler tileDragReleaseHandler){
        if(piecePressed.isFalse() || modeEdit == ModeEdit.DELETING)
            return;


        VBox wrapper = (VBox) mouseEvent.getSource();
        String id[] = wrapper.getId().split("-");
        int i = Integer.parseInt(id[0]);
        int j = Integer.parseInt(id[1]);
        System.out.println("Released " + wrapper.getId());

        dragImageView.setMouseTransparent(false);
        dragImageView.setVisible(false);
        dragImageView.setId("");
    }

    @Override
    public void boardTileDragReleased(MouseDragEvent mouseDragEvent, Piece createdPiece, Position draggedFromPosition){
        VBox wrapper = (VBox) mouseDragEvent.getSource();
        String id[] = wrapper.getId().split("-");
        System.out.println(wrapper.getId() + " DRAG RELEASED");

        int i = Integer.parseInt(id[0]);
        int j = Integer.parseInt(id[1]);

        if(createdPiece == null || ( draggedFromPosition != null && (i == draggedFromPosition.x && j == draggedFromPosition.y))){
            if(draggedFromPosition != null && (i == draggedFromPosition.x && j == draggedFromPosition.y))
                gameBoardFX.hideImage(i, j, false);
            return;
        }

        //CREATE PIECE ON NEW POSITION
        createdPiece.setPosition(new Position(i, j));
        gameBoard.setBoard(i, j, createdPiece);
        gameBoardFX.updateUI(i, j);

        //DELETE PIECE ON OLD POSITION
        if(draggedFromPosition != null){
            gameBoard.setBoard(draggedFromPosition.x, draggedFromPosition.y, null);
            gameBoardFX.updateUI(draggedFromPosition.x, draggedFromPosition.y);
        }

        createdPiece = null;
        draggedFromPosition = null;
    }


    @Override
    public void editTileClicked(MouseEvent mouseEvent, List<Piece> editPieces) {
        VBox selectedEditPieceWrap = (VBox) mouseEvent.getSource();
        if (modeEdit == ModeEdit.NEUTRAL){
            selectedEditPieceWrap.getStyleClass().add("selected-tile");
            clickedEditPieceWrap = selectedEditPieceWrap;
            modeEdit = ModeEdit.CREATING;
        }
        else if(modeEdit == ModeEdit.CREATING){
            clickedEditPieceWrap.getStyleClass().removeIf(c->c.equals("selected-tile"));
            if(clickedEditPieceWrap == selectedEditPieceWrap){
                modeEdit = ModeEdit.NEUTRAL;
                clickedEditPieceWrap = null;
            }
            else{
                selectedEditPieceWrap.getStyleClass().add("selected-tile");
                clickedEditPieceWrap = selectedEditPieceWrap;
            }
        }
        else if(modeEdit == ModeEdit.DELETING){
            selectedEditPieceWrap.getStyleClass().add("selected-tile");
            deleteToggleSwitch.setSelected(false);
            lastTogglePosition = false;
            gameBoardFX.removeStyleClassWrappers("remove-tile");
            clickedEditPieceWrap = selectedEditPieceWrap;
            modeEdit = ModeEdit.CREATING;
        }
        String[] id = selectedEditPieceWrap.getId().split("-");
        Piece newPiece = editPieces.stream().filter(p -> p.getName().equals(id[0])).findFirst().get();
        newPiece.setColor(id[1]);
    }

    @Override
    public void editTilePressed(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed, TileDragReleaseHandler tileDragReleaseHandler) {
        VBox editWrapper = (VBox) mouseEvent.getSource();
        System.out.println("Pressed edit box " + editWrapper.getId());

        Piece piece = gameData.identifierToPieceMap.get(editWrapper.getId());

        Piece newPiece = piece.createSelf();
        newPiece.setColor(piece.getColor());
        tileDragReleaseHandler.setCreatedPiece(newPiece, null);

        dragImageView.setMouseTransparent(true);
        Image newImage = gameData.identifierToImageMap.get(editWrapper.getId());
        dragImageView.setImage(newImage);
        dragImageView.setId(piece.getImageIdentifier());
        dragImageView.setVisible(true);

        dragImageView.setFitWidth(editWrapper.getWidth());
        dragImageView.setFitHeight(editWrapper.getHeight());
        dragImageView.setLayoutX(mouseEvent.getSceneX() - editWrapper.getWidth()/2);
        dragImageView.setLayoutY(mouseEvent.getSceneY() - editWrapper.getHeight()/2);
        piecePressed.setTrue();
    }

    @Override
    public void editTileDragged(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed) {
        if(piecePressed.isFalse())
            return;
        VBox editWrapper = (VBox) mouseEvent.getSource();
        dragImageView.setLayoutX(mouseEvent.getSceneX() - dragImageView.getFitWidth()/2);
        dragImageView.setLayoutY(mouseEvent.getSceneY() - dragImageView.getFitHeight()/2);
    }

    @Override
    public void editTileReleased(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed) {
        if(piecePressed.isFalse())
            return;

        VBox editWrapper = (VBox) mouseEvent.getSource();
        dragImageView.setMouseTransparent(false);
        dragImageView.setVisible(false);
        dragImageView.setId("");
    }

    @Override
    public void editTileMouseEntered(MouseEvent mouseEvent, Piece createdPiece) {

    }


    Position createdShadow = null;
    @Override
    public void boardTileHover(VBox wrapper, boolean oldVal, boolean newVal){
        if(modeEdit == ModeEdit.CREATING){
            String id[] = wrapper.getId().split("-");
            int i = Integer.parseInt(id[0]);
            int j = Integer.parseInt(id[1]);

            if(newVal){
                if(this.gameBoard.getPiece(i, j, false) == null){
                    String identifier = clickedEditPieceWrap.getId();
                    Piece createPiece = gameData.getAllPieces().stream().filter(p -> p.getImageIdentifier().equals(identifier)).findFirst().get();

                    this.gameBoardFX.createShadow(i, j, createPiece);
                    createdShadow = new Position(i, j);
                }
            }
            else{
                if(createdShadow != null && i == createdShadow.x && j == createdShadow.y)
                    this.gameBoardFX.deleteShadow(i, j);
                createdShadow = null;
            }
        }
    }


    @FXML private Label pgnFileName;
    @FXML
    public void LoadPgn(){
        FileController fileController = new FileController(stage);
        String data = fileController.getDataFromFile();
        pgnFileName.setText("No File Selected");
        if(data == null)
            return;
        pgnFileName.setText(fileController.fileName);
        PgnParser pgnParser = new PgnParser(data);
        if(!pgnParser.failed)
            return; //TODO
    }



    @FXML
    public void StopEditing(){
        this.gameData.gameBoard = this.gameBoard;
        sceneManager.setChessGameScene(this.gameData);
    }

    @FXML
    public void DeletePiece(){

    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

}
