package cvut.fel.pjv.pelikma3.elements.tilehandlers;

import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.HashMap;

public class TilePressHandler implements EventHandler<MouseEvent> {

    BoardEventResponder responder;
    ImageView dragImageView;
    GameBoard gameBoard;
    private HashMap<String, Image> identifierToImageMap;
    MutableBoolean piecePressed;
    TileDragReleaseHandler tileDragReleaseHandler;

    public TilePressHandler(BoardEventResponder responder, ImageView imageView, GameBoard gameBoard, HashMap<String, Image> identifierToImageMap,
                            MutableBoolean piecePressed, TileDragReleaseHandler tileDragReleaseHandler){
        this.responder = responder;
        this.dragImageView = imageView;
        this.gameBoard = gameBoard;
        this.identifierToImageMap = identifierToImageMap;
        this.piecePressed = piecePressed;
        this.tileDragReleaseHandler = tileDragReleaseHandler;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        responder.boardTilePressed(mouseEvent, dragImageView, piecePressed, tileDragReleaseHandler);
    }
}
