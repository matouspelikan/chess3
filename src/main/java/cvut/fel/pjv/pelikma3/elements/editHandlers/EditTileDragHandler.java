package cvut.fel.pjv.pelikma3.elements.editHandlers;

import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import cvut.fel.pjv.pelikma3.elements.EditEventResponder;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class EditTileDragHandler implements EventHandler<MouseEvent> {

    EditEventResponder responder;
    ImageView dragImageView;
    MutableBoolean piecePressed;
    public EditTileDragHandler(EditEventResponder responder, ImageView dragImageView, MutableBoolean piecePressed){
        this.dragImageView = dragImageView;
        this.piecePressed = piecePressed;
        this.responder = responder;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        responder.editTileDragged(mouseEvent, dragImageView, piecePressed);
    }
}
