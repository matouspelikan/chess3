package cvut.fel.pjv.pelikma3.elements.tilehandlers;

import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class TileDragHandler implements EventHandler<MouseEvent> {

    BoardEventResponder responder;
    ImageView dragImageView;
    MutableBoolean piecePressed;
    public TileDragHandler(BoardEventResponder responder, ImageView dragImageView, MutableBoolean piecePressed){
        this.dragImageView = dragImageView;
        this.piecePressed = piecePressed;
        this.responder = responder;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        responder.boardTileDragged(mouseEvent, dragImageView, piecePressed);
    }
}
