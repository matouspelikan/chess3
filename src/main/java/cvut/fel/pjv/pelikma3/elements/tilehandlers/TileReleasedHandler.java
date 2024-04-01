package cvut.fel.pjv.pelikma3.elements.tilehandlers;

import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import cvut.fel.pjv.pelikma3.elements.GameBoardFX;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import cvut.fel.pjv.pelikma3.utils.Position;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.HashMap;
import java.util.Map;

public class TileReleasedHandler implements EventHandler<MouseEvent> {

    BoardEventResponder responder;
    ImageView dragImageView;
    HashMap<String, Piece> identifierToPieceMap;
    MutableBoolean piecePressed;
    MutableBoolean pieceReleased;
    GameBoard gameBoard;
    GameBoardFX gameBoardFX;
    TileMouseEnteredHandler tileMouseEnteredHandler;
    TileDragReleaseHandler tileDragReleaseHandler;

    public TileReleasedHandler(BoardEventResponder responder, ImageView dragImageView, GameBoard gameBoard, GameBoardFX gameBoardFX,
                               HashMap<String, Piece> identifierToPieceMap, MutableBoolean piecePressed,
                               MutableBoolean pieceReleased, TileDragReleaseHandler tileDragReleaseHandler){
        this.dragImageView = dragImageView;
        this.identifierToPieceMap = identifierToPieceMap;
        this.piecePressed = piecePressed;
        this.gameBoard = gameBoard;
        this.gameBoardFX = gameBoardFX;
        this.pieceReleased = pieceReleased;
//        this.tileMouseEnteredHandler = tileMouseEnteredHandler;
        this.tileDragReleaseHandler = tileDragReleaseHandler;
        this.responder = responder;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        responder.boardTileReleased(mouseEvent, dragImageView, piecePressed, tileDragReleaseHandler);
    }
}
