package cvut.fel.pjv.pelikma3.handlers;

import cvut.fel.pjv.pelikma3.controllers.GameController;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.Game;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PromotionClickHandler extends MouseHandler implements EventHandler<MouseEvent> {

    private static final Logger logger = Logger.getLogger(PromotionClickHandler.class.getName());

    private HBox promotionBoxDown;
    private HBox promotionBoxUp;
    private HashMap<String, Piece> promPieces;
    private Game game;
    private GameController gameController;

    public PromotionClickHandler(Game game, GameController gameController, HBox promoBoxDown, HBox promoBoxUp, HashMap<String, Piece> promPieces){
        super(game, gameController);
        this.promotionBoxDown = promoBoxDown;
        this.promotionBoxUp = promoBoxUp;
        this.promPieces = promPieces;
        this.gameController = gameController;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        VBox vBox = (VBox) mouseEvent.getSource();
        String[] id= vBox.getId().split("-");
//        logger.log(Level.INFO, id[0]);
        Piece promotedPiece = promPieces.get(id[0]).createSelf();
        if(id[1].equals("white")){
            promotedPiece.setColor(Color.WHITE);
        }
        else{
            promotedPiece.setColor(Color.BLACK);
        }
        gameController.createPromotedPiece(promotedPiece);
        if(promotionBoxUp.isVisible())
            promotionBoxUp.setVisible(false);
        if(promotionBoxDown.isVisible())
            promotionBoxDown.setVisible(false);
    }
}
