package cvut.fel.pjv.pelikma3.elements;

import cvut.fel.pjv.pelikma3.elements.tilehandlers.TileDragReleaseHandler;
import cvut.fel.pjv.pelikma3.elements.tilehandlers.TileMouseEnteredHandler;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.utils.Position;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.mutable.MutableBoolean;

public interface BoardEventResponder {
    public void boardTilePressed(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed, TileDragReleaseHandler tileDragReleaseHandler);
    public void boardTileDragged(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed);
    public void boardTileReleased(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed,
                                  TileDragReleaseHandler tileDragReleaseHandler);
    public void boardTileDragReleased(MouseDragEvent mouseDragEvent, Piece createdPiece, Position draggedFromPosition);
    public void boardTileClicked(MouseEvent mouseEvent);

    public void boardTileHover(VBox wrapper, boolean oldVal, boolean newVal);
 }
