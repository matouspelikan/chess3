package cvut.fel.pjv.pelikma3.elements.editHandlers;

import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import cvut.fel.pjv.pelikma3.elements.EditEventResponder;
import cvut.fel.pjv.pelikma3.elements.tilehandlers.TileDragReleaseHandler;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.HashMap;

public class EditTilePressHandler implements EventHandler<MouseEvent> {

    EditEventResponder responder;
    javafx.scene.image.ImageView dragImageView;
    GameBoard gameBoard;
    private HashMap<String, Image> identifierToImageMap;
    MutableBoolean piecePressed;
    TileDragReleaseHandler tileDragReleaseHandler;

    public EditTilePressHandler(EditEventResponder responder, ImageView imageView, GameBoard gameBoard, HashMap<String, Image> identifierToImageMap,
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
        responder.editTilePressed(mouseEvent, dragImageView, piecePressed, tileDragReleaseHandler);
    }
}
