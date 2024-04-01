package cvut.fel.pjv.pelikma3.elements.tilehandlers;

import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import cvut.fel.pjv.pelikma3.elements.GameBoardFX;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import cvut.fel.pjv.pelikma3.utils.Position;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class TileMouseEnteredHandler extends TileHandler {

    Piece createdPiece = null;
    MutableBoolean pieceReleased;
    public TileMouseEnteredHandler(BoardEventResponder responder, GameBoard gameBoard, GameBoardFX gameBoardFX, MutableBoolean pieceReleased){
        super(responder, gameBoard, gameBoardFX);
        this.pieceReleased = pieceReleased;
    }

    public void setCreatedPiece(Piece createdPiece){
        this.createdPiece = createdPiece;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
//        responder.boardTileMouseEntered(mouseEvent, pieceReleased, createdPiece);
    }
}
