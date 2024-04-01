package cvut.fel.pjv.pelikma3.elements.tilehandlers;

import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import cvut.fel.pjv.pelikma3.elements.GameBoardFX;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import cvut.fel.pjv.pelikma3.utils.Position;
import javafx.event.EventHandler;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class TileDragReleaseHandler implements EventHandler<MouseDragEvent> {


    BoardEventResponder responder;
    GameBoard gameBoard;
    GameBoardFX gameBoardFX;
    Position draggedFromPosition;

    Piece createdPiece = null;
    MutableBoolean pieceReleased;
    public TileDragReleaseHandler(BoardEventResponder responder, GameBoard gameBoard, GameBoardFX gameBoardFX, MutableBoolean pieceReleased){
        this.responder = responder;
        this.gameBoard = gameBoard;
        this.gameBoardFX = gameBoardFX;
        this.pieceReleased = pieceReleased;
    }

    public void setCreatedPiece(Piece createdPiece, Position draggedFromPosition){
        this.createdPiece = createdPiece;
        this.draggedFromPosition = draggedFromPosition;
    }

    @Override
    public void handle(MouseDragEvent mouseDragEvent) {
        responder.boardTileDragReleased(mouseDragEvent, createdPiece, draggedFromPosition);
        this.createdPiece = null;
        this.draggedFromPosition = null;
    }
}
