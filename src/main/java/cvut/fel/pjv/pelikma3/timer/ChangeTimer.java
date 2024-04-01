package cvut.fel.pjv.pelikma3.timer;

import javafx.scene.control.Label;

public class ChangeTimer implements Runnable{

    Label label;
    String value;


    public ChangeTimer(Label label, String value){
        this.label = label;
        this.value = value;
    }


    @Override
    public void run() {
        this.label.setText(this.value);
    }
}
