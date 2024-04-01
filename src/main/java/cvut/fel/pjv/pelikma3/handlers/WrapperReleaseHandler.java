package cvut.fel.pjv.pelikma3.handlers;

import cvut.fel.pjv.pelikma3.controllers.GameController;
import cvut.fel.pjv.pelikma3.utils.Game;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class WrapperReleaseHandler extends MouseHandler implements EventHandler<MouseEvent> {


    public WrapperReleaseHandler(Game game, GameController gameController) {
        super(game, gameController);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        System.out.println("release");
    }
}
