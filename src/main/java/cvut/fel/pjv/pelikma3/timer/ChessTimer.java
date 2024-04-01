package cvut.fel.pjv.pelikma3.timer;

import cvut.fel.pjv.pelikma3.controllers.GameController;
import cvut.fel.pjv.pelikma3.utils.Side;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class ChessTimer extends Thread{

    final Object o = new Object();
    final Object p = new Object();

    GameController gameController;
    Label timerLabelUp;
    Label timerLabelDown;
    public boolean gameEnded = false;
    public boolean started = false;
    private boolean isStartTimer;
    public long upTime;
    public long downTime;
    long minutes;
    long seconds;
    long mills;

    private boolean upTurn;
    boolean upStarts;


    public boolean isUpTurn() {
        synchronized (o) {
            return upTurn;
        }
    }
    public void setUpTurn(boolean upTurn) {
        synchronized (o){
            this.upTurn = upTurn;
        }
    }
    public boolean isStartTimer() {
        return isStartTimer;
    }

    public ChessTimer(Label timerLabelUp, Label timerLabelDown, GameController gameController, long upTime, long downTime, boolean upStarts, boolean isStartTimer){
        this.timerLabelUp = timerLabelUp;
        this.timerLabelDown = timerLabelDown;
        this.gameController = gameController;
        this.upTime = upTime;
        this.downTime = downTime;
        this.upStarts = upStarts;
        this.isStartTimer = isStartTimer;
    }

    @Override
    public void run() {
        started = true;
        Clock clock = Clock.systemDefaultZone();
        long start = clock.millis();
        long elapsed = 0;
        long lastelapsed = -1;

        while(!gameEnded){
            elapsed = clock.millis() - start;
            if(elapsed - lastelapsed > 10){
                if(isUpTurn()){
                    upTime -= (elapsed - lastelapsed);
                    if(upTime <= 0){
                        this.gameEnded = true;
                        gameController.endGame(false, true, false);
                    }
                    Platform.runLater(new ChangeTimer(timerLabelUp, MinuteFormat(upTime)));
                }
                else {
                    downTime -= (elapsed - lastelapsed);
                    if(downTime <= 0){
                        this.gameEnded = true;
                        gameController.endGame(true, true, false);
                    }
                    Platform.runLater(new ChangeTimer(timerLabelDown, MinuteFormat(downTime)));
                }
                lastelapsed = elapsed;
            }
        }
    }

    public String MinuteFormat(long time){
        minutes = (time/1000)/60;
        String s = Long.toString(minutes);
        if(minutes == 0)
            s = "00";
        seconds = (time/1000)%60;
        s += ":";
        if(seconds<10)
            s+= "0";
        s += Long.toString(seconds);
        mills = (time/10)%100;
        if(mills == 0){
            s+= ":00";
            return s;
        }
        s+=":";
        s+= Long.toString(mills);
        if(mills < 10)
            s+="0";
        return s;
    }
}
