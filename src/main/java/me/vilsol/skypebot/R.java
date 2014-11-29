package me.vilsol.skypebot;

public class R {

    public static final String version = "1.0";
    public static final String command = "@";

    public static void s(String message){
        SkypeBot.getInstance().sendMessage(message);
    }

    public static void s(int i){
        SkypeBot.getInstance().sendMessage(String.valueOf(i));
    }

    public static void s(String[] message){
        SkypeBot.getInstance().addToQueue(message);
        SkypeBot.getInstance().sendMessage(null);
    }
}
