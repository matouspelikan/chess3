package cvut.fel.pjv.pelikma3.handlers;

import cvut.fel.pjv.pelikma3.controllers.GameController;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.utils.Position;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class GameboardTileClickHandler extends MouseHandler implements EventHandler<MouseEvent> {
    public GameboardTileClickHandler(Game game, GameController gameController){
        super(game, gameController);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        System.out.println("cliccck");
        VBox vbox = (VBox) mouseEvent.getSource();
        String[] id= vbox.getId().split("-");
        Position position = new Position(Integer.parseInt(id[1]), Integer.parseInt(id[0]));

        System.out.println("click " + position.x + " " + position.y);
        gameController.SquareClicked(position);
    }
}
