package cvut.fel.pjv.pelikma3.handlers;

import cvut.fel.pjv.pelikma3.controllers.GameController;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.utils.Game;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class EditClickHandler extends MouseHandler implements EventHandler<MouseEvent> {

    HashMap<String, Piece> editPieces;
    public EditClickHandler(Game game, GameController gameController, HashMap<String, Piece> editPieces) {
        super(game, gameController);
        this.editPieces = editPieces;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        System.out.println("edit clicked");
        VBox vBox = (VBox) mouseEvent.getSource();
        String[] id= vBox.getId().split("-");
        Piece newEditedPiece = editPieces.get(id[0]).createSelf();
        newEditedPiece.setColor(id[1]);
        this.gameController.createnewPiece(newEditedPiece, vBox);
    }
}
