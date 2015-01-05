package me.vilsol.skypebot.utils;

public class UpdateChecker extends Thread {

    private String lastHash = Utils.getMD5Hash(Utils.getJarName());

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }

            String newHash = Utils.getMD5Hash(Utils.getJarName());
            if (!newHash.equals(lastHash)) {
                R.s("Updated Jar Detected!");
                Utils.restartBot();
            }
        }
    }

}
