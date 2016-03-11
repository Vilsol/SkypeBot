package io.mazenmc.skypebot.utils;

import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.ReceivedMessage;
import com.samczsun.skype4j.formatting.Message;
import io.mazenmc.skypebot.SkypeBot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Resource {

    public static final String COMMAND_PREFIX = "@";
    public static final String[] GROUP_ADMINS = {"stuntguy3000", "heycallmethemezand"};
    public static final String KEY_GITHUB = Utils.readFirstLine("key_github");
    public static final String KEY_TRIDENT = Utils.readFirstLine("key_trident");
    public static final String KEY_URBAND = Utils.readFirstLine("key_ud");
    public static final String REGEX_ALL = "(.+)";
    public static final String REGEX_DOUBLE = "(-?[0-9]+\\.[0-9]+)";
    public static final String REGEX_INT = "(-?[0-9]+)";
    public static final String REGEX_WORD = "(\\b+)";
    public static final String VERSION = "2.0";
    public static final Pattern SPOTIFY_HTTP_REGEX = Pattern.compile("open\\.spotify\\.com/track/([A-z0-9]){22}");
    public static final Pattern SPOTIFY_URI_REGEX = Pattern.compile("spotify:track:([A-z0-9]){22}");
    public static final Pattern TWITTER_REGEX = Pattern.compile("twitter\\.com\\/([A-z0-9]+)\\/status\\/([0-9]{18})");
    public static final Pattern WIKIPEDIA_REGEX = Pattern.compile("en\\.wikipedia\\.(?:org|com)\\/wiki\\/(.+)");
    public static final String[] BALL_RESPONSES = new String[] {"It is certain", "It is decidedly so", "Without a doubt",
                                                                "Yes definitely", "You may rely on it", "As I see it, yes",
                                                                "Most likely", "Outlook good", "Yes", "Signs point to yes",
                                                                "Reply hazy try again", "Ask again later", "Better not tell you now",
                                                                "Cannot predict now", "Concentrate and ask again", "Don't count on it",
                                                                "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};

    private static Map<String, Callback<String>> callbacks = new HashMap<>();
    private static ChatterBotSession generalBot;

    public static void sendMessage(String message) {
        SkypeBot.INSTANCE$.sendMessage(message);
    }

    public static void sendMessage(ReceivedMessage chatMessage, String message) {
        String displayName = Utils.getDisplayName(chatMessage.getSender());
        try {
            Chat chat = chatMessage.getChat();

            if (chat.getAllUsers().size() > 2) {
                message = "(" + displayName.replaceAll("[^A-Za-z0-9 ><.»«]", "") + ") " + message;
            }

            message = message.replaceAll("(https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b(?:[-a-zA-Z0-9@:%_\\+.~#?&//=]*))", "<a href=\"$1\">$1</a>");

            chat.sendMessage(Message.fromHtml(message));
        } catch (Exception ex) {
            sendMessage("Error occurred! " + ex.getMessage());
        }
    }

    public static void sendMessages(String... message) {
        StringBuilder sb = new StringBuilder();

        for (String s : message) {
            sb.append(s)
                    .append('\n');
        }

        sendMessage(sb.toString());
    }

    public static void assignCallback(String id, Callback<String> callback) {
        callbacks.put(id, callback);
    }

    public static Callback<String> getCallback(String name) {
        return callbacks.get(name);
    }

    public static String askQuestion(String input) {
        if (generalBot == null) {
            try {
                generalBot = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession();
            } catch (Exception ignored) {
            }
        }

        try {
            return generalBot.think(input);
        } catch (Exception ignored) {
            return "I am overthinking... :'c";
        }
    }
}
