package cvut.fel.pjv.pelikma3.controllers;

import cvut.fel.pjv.pelikma3.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class MenuController {

    public String name;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private SceneManager sceneManager;

    public MenuController(){
        this.name = "controller";
    }


    @FXML
    public void MouseClick(MouseEvent e){
        Object object = e.getSource();
        System.out.println(object.toString());
        System.out.println(e.getX() + " " + e.getY());
        System.out.println(e.getSceneX() + " " + e.getSceneY());
        System.out.println(e.getScreenX() + " " + e.getSceneY());
    }

    @FXML
    public void DragOver(MouseEvent e){
        System.out.println("dragged");
        Object object = e.getSource();
        Label l = (Label)object;
        l.setStyle("-fx-border-color: red");
    }

    @FXML
    public void MouseClickStart(MouseEvent e) {
        sceneManager.SetGameScene();
    }

    @FXML
    public void MouseClickEdit(MouseEvent e){
        this.sceneManager.SetEditScene();
    }

    @FXML
    public void NetworkGameClick(MouseEvent e){

    }
}
