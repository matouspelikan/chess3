package cvut.fel.pjv.pelikma3;

import cvut.fel.pjv.pelikma3.controllers.*;
import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    private Scene scene;
    FXMLLoader menuLoader;
    Parent menuRoot;
    FXMLLoader gameLoader;
    Parent gameRoot;
    FXMLLoader editLoader;
    Parent editRoot;
    FXMLLoader configLoader;
    Parent configRoot;
    FXMLLoader chessGameLoader;
    Parent chessGameRoot;

    FXMLLoader gameNetworkLoader;
    Parent gameNetworkRoot;
    GameControllerNetwork gameNetworkController;

    MenuController menuController;
    GameController gameController;
    EditController editController;
    ConfiguratorController configuratorController;
    ChessGameController chessGameController;

    Game game;
    Stage stage;


    public SceneManager(Stage stage){
        this.stage = stage;
        menuLoader = new FXMLLoader(App.class.getResource("/views/menu.fxml"));
        try {
            this.menuRoot = menuLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        menuController = getMenuController();
        menuController.setSceneManager(this);

//        loadGameController();
//        loadEditController();

    }

    public MenuController getMenuController(){
        return menuLoader.getController();
    }

    public GameController getGameController(){
        return gameLoader.getController();
    }

    public EditController getEditController(){return editLoader.getController();}

    public ConfiguratorController getConfiguratorController(){return configLoader.getController();}

    public GameControllerNetwork getGameNetworkController(){
        return gameNetworkLoader.getController();
    }


    public Parent GenerateMenu(){
        return menuRoot;
    }

    public void SetMainScene(){

        scene.setRoot(menuRoot);
    }

    public void loadGameNetworkController(){
        gameNetworkLoader = new FXMLLoader(App.class.getResource("/views/gameNetwork.fxml"));
        try {
            this.gameNetworkRoot = gameNetworkLoader.load();
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("problem asi");
        }

        gameNetworkController = getGameNetworkController();
        System.out.println(gameNetworkController == null);
        gameNetworkController.setSceneManager(this);
    }

    public void loadGameController(){
        gameLoader = new FXMLLoader(App.class.getResource("/views/game.fxml"));
        try {
            this.gameRoot = gameLoader.load();
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("problem asi");
        }
        gameController = getGameController();
        gameController.setSceneManager(this);
    }

    public void loadEditController(){
        editLoader = new FXMLLoader(App.class.getResource("/views/editGame.fxml"));
        try {
            this.editRoot = editLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        editController = getEditController();
        editController.setSceneManager(this);
    }

    public void loadConfigController(){
        configLoader = new FXMLLoader(App.class.getResource("/views/configurator.fxml"));
        try {
            this.configRoot = configLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuratorController = getConfiguratorController();
        configuratorController.setSceneManager(this);
    }

    public void loadChessGameController(){
        chessGameLoader = new FXMLLoader(App.class.getResource("/views/chessGame.fxml"));
        try {
            this.chessGameRoot = chessGameLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chessGameController = chessGameLoader.getController();
        chessGameController.setSceneManager(this);
    }

    public void SetGameScene(){
        loadGameController();
        Game newGame = new Game(Color.BLACK, Color.WHITE, false);
        this.game = newGame;

        gameController.game = newGame;
        gameController.setParams(null, stage);

        scene.setRoot(gameRoot);
    }

    public void SetGameScene(GameData gameData){
        loadGameController();
        Game newGame;
        if(gameData.gameBoard != null){
            newGame = new Game(gameData.downColor.otherColor(), gameData.downColor, gameData.gameBoard);
        }
        else{
            newGame = new Game(gameData.downColor.otherColor(), gameData.downColor, gameData.emptyBoard);
        }

        if(gameData.colorToMove != null){
            newGame.setColorTurn(gameData.colorToMove);
        }
        else{
            newGame.setColorTurn(Color.WHITE);
        }
        gameController.game = newGame;
        gameController.setParams(gameData, stage);
        scene.setRoot(gameRoot);


    }

    public void setChessGameScene(GameData gameData){
        loadChessGameController();
        chessGameController.setParams(gameData, stage);
        scene.setRoot(chessGameRoot);
    }

    public void SetEditScene(){
        loadEditController();
        editController.setParams(stage);
        scene.setRoot(editRoot);
    }

    public void SetConfigScene(GameData gameData){
        loadConfigController();
        configuratorController.setParams(gameData, stage);
        scene.setRoot(configRoot);
    }

}
