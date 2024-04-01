package cvut.fel.pjv.pelikma3.controllers;

import cvut.fel.pjv.pelikma3.SceneManager;
import cvut.fel.pjv.pelikma3.pgnUtils.PgnParser;
import cvut.fel.pjv.pelikma3.players.Color;
import cvut.fel.pjv.pelikma3.utils.GameBoard;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import java.util.Random;

public class EditController {

    public SceneManager sceneManager;
    public GameBoard loadedBoard = null;
    Color colorToMove = null;
    private int defaultSliderWidth = 400;
    Stage stage;

    @FXML
    private Slider slider1;
    @FXML
    private Slider slider2;
    @FXML
    private Label slider1Label;
    @FXML
    private Label slider2Label;
    @FXML
    private CheckBox editCheckBox;
    @FXML
    private CheckBox pcCheckBox;
    @FXML
    private CheckBox networkCheckBox;
    @FXML
    private CheckBox emptyBoardCheckBox;
    @FXML
    private ComboBox colorComboBox;
    @FXML
    private Label fileName;

    public EditController(){

    }

    /**
     * substitute for constructor, component inits
     * @param stage
     */
    public void setParams(Stage stage){
        setSlider(slider1, slider1Label);
        setSlider(slider2, slider2Label);

        ObservableList<String> comboBoxItems = this.colorComboBox.getItems();

        comboBoxItems.add("Black");
        comboBoxItems.add("White");
        comboBoxItems.add("Random");

//        this.colorComboBox.getItems().add("Black");
//        this.colorComboBox.getItems().add("White");
//        this.colorComboBox.getItems().add("Random");
        this.colorComboBox.getSelectionModel().select(1);
        this.stage = stage;
    }

    private void setSlider(Slider slider, Label sliderLabel){
        slider.setMinWidth(defaultSliderWidth);
        slider.setMin(1f);
        slider.setMax(30f);
        slider.valueProperty().addListener((a, b, c)->{
            sliderLabel.setText(Integer.toString((int)Math.round((double)c)));
        });
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    /**
     * Gets data from javafx components, puts them into gamedata instance and passes it to scenemanager which starts the game
     */
    public void startGame(){
        String playerColor = (String) this.colorComboBox.getSelectionModel().getSelectedItem();
        if(playerColor.equals("Random")){
            Random rand = new Random();
            this.colorComboBox.getSelectionModel().select(rand.nextInt(2));
            playerColor = (String) this.colorComboBox.getSelectionModel().getSelectedItem();
        }

        Color color = Color.WHITE;
        if(playerColor.equals("Black")) {
            color = Color.BLACK;
        }

        boolean empty = emptyBoardCheckBox.isSelected();
        if(this.loadedBoard != null)
            empty = false;

        GameData gd = new GameData((int)Math.round((double)slider1.getValue())*60000,
                (int)Math.round((double)slider2.getValue())*60000, editCheckBox.isSelected(), pcCheckBox.isSelected(),
                color, this.loadedBoard, empty, colorToMove, networkCheckBox.isSelected());

        if(editCheckBox.isSelected()){
            sceneManager.SetConfigScene(gd);
        }
        else{
            sceneManager.SetGameScene(gd);
        }
    }

    @FXML
    private void mainMenu(){
        this.sceneManager.SetMainScene();
    }

    @FXML
    private void loadPgnGame(){
        FileController fileController = new FileController(stage);
        String data = fileController.getDataFromFile();
        fileName.setText(" No File Selected");
        if(data == null)
            return;
        fileName.setText(fileController.fileName);
        PgnParser parser = new PgnParser(data);
        colorToMove = parser.getColorToMove();
        if(!parser.failed)
            loadedBoard = parser.gameboard;
    }
}
