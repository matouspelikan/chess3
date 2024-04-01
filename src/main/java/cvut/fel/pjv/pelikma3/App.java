package cvut.fel.pjv.pelikma3;

import cvut.fel.pjv.pelikma3.controllers.FileController;
import cvut.fel.pjv.pelikma3.controllers.GameController;
import cvut.fel.pjv.pelikma3.handlers.StageResizeHandler;
import cvut.fel.pjv.pelikma3.pgnUtils.PgnParser;
import cvut.fel.pjv.pelikma3.pieces.Piece;
import cvut.fel.pjv.pelikma3.pieces.PieceKnight;
import cvut.fel.pjv.pelikma3.pieces.PiecePawn;
import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.Game;
import cvut.fel.pjv.pelikma3.utils.Position;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class App extends Application {
//    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager sceneManager = new SceneManager(stage);
        Scene scene = new Scene(sceneManager.GenerateMenu());
        sceneManager.setScene(scene);

        stage.setWidth(1280);
        stage.setHeight(800);
        stage.setScene(scene);
        stage.setMinHeight(500);
        stage.setMinWidth(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
