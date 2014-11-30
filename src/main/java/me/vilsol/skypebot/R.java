package me.vilsol.skypebot;

public class R {

    public static final String version = "1.1";
    public static final String command = "@";

    public static final String REGEX_ALL = "(.+)";
    public static final String REGEX_WORD = "(\\b+)";
    public static final String REGEX_DOUBLE = "(-?[0-9]+\\.[0-9]+)";
    public static final String REGEX_INT = "(-?[0-9]+)";

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
