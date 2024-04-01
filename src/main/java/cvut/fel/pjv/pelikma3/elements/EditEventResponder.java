package cvut.fel.pjv.pelikma3.elements;

import cvut.fel.pjv.pelikma3.elements.tilehandlers.TileDragReleaseHandler;
import cvut.fel.pjv.pelikma3.elements.tilehandlers.TileMouseEnteredHandler;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.List;

public interface EditEventResponder {

    public void editTilePressed(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed, TileDragReleaseHandler tileDragReleaseHandler);
    public void editTileDragged(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed);
    public void editTileReleased(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed);
    public void editTileMouseEntered(MouseEvent mouseEvent, Piece createdPiece);
    public void editTileClicked(MouseEvent mouseEvent, List<Piece> allPieces);
}
