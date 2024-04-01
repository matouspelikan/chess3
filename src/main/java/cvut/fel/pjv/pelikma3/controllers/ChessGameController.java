package cvut.fel.pjv.pelikma3.controllers;

import cvut.fel.pjv.pelikma3.SceneManager;
import cvut.fel.pjv.pelikma3.elements.BoardEventResponder;
import cvut.fel.pjv.pelikma3.elements.GameBoardFX;
import cvut.fel.pjv.pelikma3.elements.tilehandlers.TileDragReleaseHandler;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.utils.Move;
import cvut.fel.pjv.pelikma3.utils.Position;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.List;

public class ChessGameController implements BoardEventResponder {

    public enum ModeGame {
        NEUTRAL, PIECE_CLICKED
    }

    SceneManager sceneManager;
    Stage stage;
    GameData gameData;
    GameBoardFX  gameBoardFX;
    ModeGame mode;

    @FXML AnchorPane pane;
    @FXML VBox boardVbox;

    public ChessGameController(){

    }

    public void setParams(GameData gameData, Stage stage){
        gameBoardFX = new GameBoardFX(pane, boardVbox, stage, gameData.gameBoard,
                gameData.identifierToImageMap, gameData.identifierToPieceMap, this);

        this.gameData = gameData;
        mode = ModeGame.NEUTRAL;
    }

    public void setSceneManager(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }


    @Override
    public void boardTileClicked(MouseEvent mouseEvent) {
        VBox wrapper = (VBox) mouseEvent.getSource();
        System.out.println("Clicked "+ wrapper.getId());
        String id[] = wrapper.getId().split("-");
        int i = Integer.parseInt(id[0]);
        int j = Integer.parseInt(id[1]);

        Piece piece = this.gameData.gameBoard.getPiece(i, j, false);

        if(mode == ModeGame.NEUTRAL) {
            if (piece != null){
                List<Move> moves = piece.acceptGameBoard(gameData.gameBoard);
                System.out.println(piece.pieceType.toString());
                for(Move m: moves){
                    System.out.println(m.newPosition);
                    gameBoardFX.markAsPossibleMove(m.newPosition);
                }
                mode = ModeGame.PIECE_CLICKED;
            }
        }
        else if(mode == ModeGame.PIECE_CLICKED){

        }

        if (piece != null){
            if(mode == ModeGame.NEUTRAL){
                List<Move> moves = piece.acceptGameBoard(gameData.gameBoard);
                System.out.println(piece.pieceType.toString());
                for(Move m: moves){
                    System.out.println(m.newPosition);
                    gameBoardFX.markAsPossibleMove(m.newPosition);
                }
                mode = ModeGame.PIECE_CLICKED;
            }
            else if(mode == ModeGame.PIECE_CLICKED){
                gameBoardFX.unmarkPossibleMoves();
                mode = ModeGame.NEUTRAL;
            }
        }
    }



    @Override
    public void boardTilePressed(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed, TileDragReleaseHandler tileDragReleaseHandler) {

    }

    @Override
    public void boardTileDragged(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed) {

    }

    @Override
    public void boardTileReleased(MouseEvent mouseEvent, ImageView dragImageView, MutableBoolean piecePressed, TileDragReleaseHandler tileDragReleaseHandler) {

    }

    @Override
    public void boardTileDragReleased(MouseDragEvent mouseDragEvent, Piece createdPiece, Position draggedFromPosition) {

    }

    @Override
    public void boardTileHover(VBox wrapper, boolean oldVal, boolean newVal) {

    }
}
