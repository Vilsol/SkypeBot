package io.mazenmc.skypebot.game;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer extends TimerTask {
    public GameTimer() {
        new Timer().schedule(this, 1000);
    }

    @Override
    public void run() {
        if (GameManager.instance().current() != null) {
            Game current = GameManager.instance().current();

            if (current.state() == GameState.INGAME || current.state() == GameState.CHOOSING) {
                current.onSecond();
            }
        }
        
        new GameTimer();
    }
}
