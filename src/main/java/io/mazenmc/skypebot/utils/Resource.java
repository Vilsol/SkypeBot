package io.mazenmc.skypebot.utils;

import io.mazenmc.skypebot.SkypeBot;

public class Resource {

    public static final String COMMAND_PREFIX = "@";
    public static final String[] GROUP_ADMINS = {"stuntguy3000", "gnosticjade", "heycallmethemezand"};
    public static final String KEY_GITHUB = Utils.readFirstLine("key_github");
    public static final String KEY_TRIDENT = Utils.readFirstLine("key_trident");
    public static final String REGEX_ALL = "(.+)";
    public static final String REGEX_DOUBLE = "(-?[0-9]+\\.[0-9]+)";
    public static final String REGEX_INT = "(-?[0-9]+)";
    public static final String REGEX_WORD = "(\\b+)";
    public static final String URBAN_DICTIONARY_URL = "http://www.urbandictionary.com/define.php?term=";
    public static final String VERSION = "1.3";

    public static void sendMessage(String message) {
        SkypeBot.getInstance().sendMessage(message);
    }

    public static void sendMessage(int i) {
        SkypeBot.getInstance().sendMessage(String.valueOf(i));
    }

    public static void sendMessage(String[] message) {
        SkypeBot.getInstance().addToQueue(message);
    }
}
