package cvut.fel.pjv.pelikma3.elements.tilehandlers;

import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import cvut.fel.pjv.pelikma3.elements.GameBoardFX;
import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public abstract class TileHandler implements EventHandler<MouseEvent> {

    BoardEventResponder responder;
    GameBoardFX gameBoardFX;
    GameBoard gameBoard;

    public TileHandler(){

    }

    public TileHandler(BoardEventResponder responder, GameBoard gameBoard, GameBoardFX gameBoardFX){
        this.responder = responder;
        this.gameBoard = gameBoard;
        this.gameBoardFX = gameBoardFX;
    }
}
