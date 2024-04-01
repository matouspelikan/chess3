package cvut.fel.pjv.pelikma3.handlers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.stage.Stage;

public class StageResizeHandler implements InvalidationListener {
    @Override
    public void invalidated(Observable observable) {
        System.out.println("resize invalidation");
        System.out.println(observable);
    }
}
