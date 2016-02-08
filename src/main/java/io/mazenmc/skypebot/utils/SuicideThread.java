package io.mazenmc.skypebot.utils;

/*
 * because ALL Skype APIs are terrible
 */
public class SuicideThread extends Thread {
    public SuicideThread() {
        super("SuicideThread");
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10800000); // 3 hours fam
        } catch (InterruptedException ex) {
            run(); // no
        }

        Utils.restartBot(); // ayy
    }

    public void commit() {
        start();
    }
}
