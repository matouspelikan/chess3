package cvut.fel.pjv.pelikma3.handlers;

import cvut.fel.pjv.pelikma3.controllers.GameController;
import cvut.fel.pjv.pelikma3.utils.Game;

public class MouseHandler {
    public Game game;
    public GameController gameController;

    public MouseHandler(Game game, GameController gameController){
        this.game = game;
        this.gameController = gameController;
    }

}
