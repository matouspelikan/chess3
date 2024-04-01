package cvut.fel.pjv.pelikma3.elements.tilehandlers;

import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.ResponseCache;

public class TileClickHandler implements EventHandler<MouseEvent> {

    BoardEventResponder responder;
    public TileClickHandler(BoardEventResponder responder){
        this.responder = responder;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        responder.boardTileClicked(mouseEvent);
    }
}
