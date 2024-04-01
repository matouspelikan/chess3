package cvut.fel.pjv.pelikma3.elements.editHandlers;

import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import cvut.fel.pjv.pelikma3.elements.EditEventResponder;
import cvut.fel.pjv.pelikma3.elements.GameBoardFX;
import cvut.fel.pjv.pelikma3.elements.tilehandlers.TileDragReleaseHandler;
import cvut.fel.pjv.pelikma3.elements.tilehandlers.TileMouseEnteredHandler;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.HashMap;

public class EditTileReleaseHandler implements EventHandler<MouseEvent> {

    EditEventResponder responder;
    ImageView dragImageView;
    HashMap<String, Piece> identifierToPieceMap;
    MutableBoolean piecePressed;
    GameBoard gameBoard;
    GameBoardFX gameBoardFX;

    public EditTileReleaseHandler(EditEventResponder responder, ImageView dragImageView, GameBoard gameBoard, GameBoardFX gameBoardFX,
                                  HashMap<String, Piece> identifierToPieceMap, MutableBoolean piecePressed){
        this.dragImageView = dragImageView;
        this.identifierToPieceMap = identifierToPieceMap;
        this.piecePressed = piecePressed;
        this.gameBoard = gameBoard;
        this.gameBoardFX = gameBoardFX;
        this.responder = responder;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        responder.editTileReleased(mouseEvent, dragImageView, piecePressed);
    }
}
