package cvut.fel.pjv.pelikma3.elements;

import cvut.fel.pjv.pelikma3.controllers.ConfiguratorController;
import cvut.fel.pjv.pelikma3.elements.editHandlers.EditTilePressHandler;
import cvut.fel.pjv.pelikma3.elements.editHandlers.EditTileReleaseHandler;
import cvut.fel.pjv.pelikma3.elements.tilehandlers.*;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import cvut.fel.pjv.pelikma3.utils.Position;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class GameBoardFX {
    private final static Logger logger = Logger.getLogger(GameBoardFX.class.getName());

    public DoubleProperty tileSizeProperty = new SimpleDoubleProperty(2.0);

    AnchorPane pane;
    Stage stage;
    VBox boardVbox;
    BoardEventResponder responder;
    GameBoard gameBoard;
    ConfiguratorController configuratorController;

    List<VBox> pieceWrappers = new ArrayList<>();
    VBox pieceWrappersArray[][] = new VBox[8][8];
    ImageView images[][] = new ImageView[8][8];
    List<Piece> editPieces = new ArrayList<>();
    HashMap<String, Image> identifierToImageMap;
    HashMap<String, Piece> identifierToPieceMap;

    TileDragReleaseHandler _tileDragReleaseHandler;

    private EditTileReleaseHandler editTileReleaseHandler;

    public GameBoardFX(AnchorPane pane, VBox boardVbox, Stage stage, GameBoard gameBoard,
                       HashMap<String, Image> identifierToImageMap,
                       HashMap<String, Piece> identifierToPieceMap,
                       BoardEventResponder responder){
        this.pane = pane;
        this.boardVbox = boardVbox;
        this.stage = stage;
        this.responder = responder;
        this.gameBoard = gameBoard;
        this.identifierToImageMap = identifierToImageMap;
        this.identifierToPieceMap = identifierToPieceMap;

        generateGameBoardElements();
        drawPieces(gameBoard);
    }

    public void generateGameBoardElements(){
        ImageView dragImageView = new ImageView();
        pane.getChildren().add(dragImageView);

        MutableBoolean piecePressed = new MutableBoolean(false);
        MutableBoolean pieceReleased = new MutableBoolean(false);

        TileClickHandler tileClickHandler = new TileClickHandler(responder);
        TileDragHandler tileDragHandler = new TileDragHandler(responder, dragImageView, piecePressed);
//        TileMouseEnteredHandler tileMouseEnteredHandler = new TileMouseEnteredHandler(responder, gameBoard, this, pieceReleased);
        TileDragReleaseHandler tileDragReleaseHandler = new TileDragReleaseHandler(responder, gameBoard, this, pieceReleased);
        this._tileDragReleaseHandler = tileDragReleaseHandler;
        TileReleasedHandler tileReleasedHandler = new TileReleasedHandler(responder, dragImageView, gameBoard, this, identifierToPieceMap, piecePressed, pieceReleased, tileDragReleaseHandler);
        TilePressHandler tilePressHandler = new TilePressHandler(responder, dragImageView, gameBoard, identifierToImageMap, piecePressed, tileDragReleaseHandler);


        for (int i = 0; i < 8; i++) {
            HBox newRow = new HBox();
            boardVbox.getChildren().add(newRow);
//            boardVbox.getStyleClass().add("board");   //Deleted because of locating release position
            boardVbox.maxWidthProperty().bind(boardVbox.heightProperty());

            for (int j = 0; j < 8; j++) {
                VBox pieceWrapper = new VBox();
                pieceWrappers.add(pieceWrapper);
                pieceWrappersArray[i][j] = pieceWrapper;
                pieceWrapper.setId((j)+"-"+(7-i));
                newRow.getChildren().add(pieceWrapper);
                pieceWrapper.getStyleClass().add("tile");

                if(((i + j) % 2) == 0)
                    pieceWrapper.getStyleClass().add("tile-light");
                else
                    pieceWrapper.getStyleClass().add("tile-dark");

                ImageView imageView = new ImageView();
                pieceWrapper.getChildren().add(imageView);
                images[i][j] = imageView;

                imageView.fitHeightProperty().bind(stage.heightProperty().subtract(100).divide(8));
                imageView.fitWidthProperty().bind(imageView.fitHeightProperty());
                tileSizeProperty.bind(imageView.fitWidthProperty());

                pieceWrapper.setOnMouseClicked(tileClickHandler);
                pieceWrapper.setOnMousePressed(tilePressHandler);
                pieceWrapper.setOnMouseDragged(tileDragHandler);
                pieceWrapper.setOnMouseReleased(tileReleasedHandler);
                pieceWrapper.setOnMouseDragReleased(tileDragReleaseHandler);
                pieceWrapper.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
                    @Override
                    public void handle(MouseDragEvent mouseDragEvent) {
                        System.out.println("mouseDragEntered");
                        //highlighting to do
                    }
                });
                pieceWrapper.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        VBox draggedOver = (VBox) mouseEvent.getSource();
                        System.out.println("drag detected " + draggedOver.getId());
                        dragImageView.startFullDrag();
                    }
                });
                pieceWrapper.hoverProperty().addListener((observable, oldValue, newValue) ->{
                    responder.boardTileHover(pieceWrapper, oldValue, newValue);
                });

            }
        }
//        for (int i = 0; i < pieceWrappers.stream().count(); i++) {
//            System.out.println(String.format("i: %d  %f %f", i, pieceWrappers.get(i).getLayoutX(), pieceWrappers.get(i).getLayoutY()));
//        }
    }

    public void drawPieces(GameBoard gameBoard){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = gameBoard.getPiece(i, j, false);
                if(piece != null){
                    Image image = identifierToImageMap.get(piece.getImageIdentifier());
                    images[7-j][i].setImage(image);
                }
            }
        }
    }

    public void updateUI(int i, int j){
        Piece piece = gameBoard.getPiece(i, j, false);
        if(piece != null){
            Image image = identifierToImageMap.get(piece.getImageIdentifier());
            images[7-j][i].setImage(image);
            images[7-j][i].setOpacity(1);
        }
        else{
            images[7-j][i].setImage(null);
        }
    }

    Image hiddenImage = null;
    public void hideImage(int i, int j, boolean hide){
        if(hide){
            hiddenImage = images[7-j][i].getImage();
            images[7-j][i].setImage(null);
        }
        else if(hiddenImage != null){
            images[7-j][i].setImage(hiddenImage);
        }
    }

    public void setStyleClassWrappers(String styleClass){
        for(VBox pieceWrappers: pieceWrappers){
            pieceWrappers.getStyleClass().add(styleClass);
        }
    }

    public void removeStyleClassWrappers(String styleClass){
        for(VBox pieceWrappers: pieceWrappers){
            pieceWrappers.getStyleClass().removeIf(p ->p.equals(styleClass));
        }
    }

    public TileDragReleaseHandler getTileDragReleaseHandler(){
        return this._tileDragReleaseHandler;
    }

    public void createShadow(int i, int j, Piece piece){
        Image image = identifierToImageMap.get(piece.getImageIdentifier());

        images[7-j][i].setImage(image);
        images[7-j][i].setOpacity(0.4);
    }

    public void deleteShadow(int i, int j){
        images[7-j][i].setImage(null);
        images[7-j][i].setOpacity(1);
    }

    List<Position> markedPossiblePositions = new ArrayList<>();

    public void markAsPossibleMove(Position position){
        Piece piece = this.gameBoard.getPiece(position.x, position.y, false);
        markedPossiblePositions.add(position);
        if(piece == null){
            Image mark = new Image(GameBoardFX.class.getResourceAsStream("/decorations/focus-empty.png"));
            images[7- position.y][position.x].setImage(mark);
        }
        else{
           this.createGroup(identifierToImageMap.get(piece.getImageIdentifier()),
                   new Image(GameBoardFX.class.getResourceAsStream("/decorations/focus-piece.png")),
                   position);
        }
    }

    public void createGroup(Image image1, Image image2, Position position){
        ImageView topView = new ImageView(image1);
        topView.fitHeightProperty().bind(stage.heightProperty().subtract(100).divide(8));
        topView.fitWidthProperty().bind(topView.fitHeightProperty());
        ImageView bottomView = new ImageView(image2);
        bottomView.fitHeightProperty().bind(stage.heightProperty().subtract(100).divide(8));
        bottomView.fitWidthProperty().bind(bottomView.fitHeightProperty());

        topView.setBlendMode(BlendMode.SRC_OVER);

        Group blend = new Group(bottomView, topView);

        pieceWrappersArray[7 - position.y][position.x].getChildren().clear();
        pieceWrappersArray[7 - position.y][position.x].getChildren().add(blend);
    }

    public void unmarkPossibleMoves(){
        for(Position p: markedPossiblePositions){
            unmarkAsPossibleMove(p);
        }
    }

    public void unmarkAsPossibleMove(Position position){
        Piece piece = this.gameBoard.getPiece(position.x, position.y, false);
        if(piece != null){
            this.createImageView(identifierToImageMap.get(piece.getImageIdentifier()), position);
        }
        else{
            this.createImageView(null, position);
        }
    }

    public void createImageView(Image image, Position position){
        ImageView imageView = new ImageView(image);

        imageView.fitHeightProperty().bind(stage.heightProperty().subtract(100).divide(8));
        imageView.fitWidthProperty().bind(imageView.fitHeightProperty());
        tileSizeProperty.bind(imageView.fitWidthProperty());

        pieceWrappersArray[7 - position.y][position.x].getChildren().clear();
        pieceWrappersArray[7 - position.y][position.x].getChildren().add(imageView);
    }

    public List<VBox> getPieceWrappers() {
        return pieceWrappers;
    }


}

