package cvut.fel.pjv.pelikma3.elements.editHandlers;

import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import cvut.fel.pjv.pelikma3.elements.EditEventResponder;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class EditTileClickHandler implements EventHandler<MouseEvent> {

    EditEventResponder responder;
    List<Piece> allPieces;

    public EditTileClickHandler(EditEventResponder responder, List<Piece> allPieces){
        this.responder = responder;
        this.allPieces = allPieces;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        responder.editTileClicked(mouseEvent, allPieces);
    }
}
