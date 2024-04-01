package cvut.fel.pjv.pelikma3.handlers;

import cvut.fel.pjv.pelikma3.controllers.GameController;
import cvut.fel.pjv.pelikma3.utils.Game;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class WrapperPressHandler extends MouseHandler implements EventHandler<MouseEvent> {


    public WrapperPressHandler(Game game, GameController gameController) {
        super(game, gameController);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        VBox v = (VBox) mouseEvent.getSource();
        System.out.println("pressed " + v.getId());
        System.out.println("from outside " + this.game.iter);
    }
}
