package io.mazenmc.skypebot.modules;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.util.Arrays;
import java.util.Random;

public class ChipDev implements Module {

    private static String[] sentences = new String[]{
            "I select everything from the database and sort it when its in an array.",
            "EvilSeph is my hero.",
            "I only need one class for my plugin",
            "Spigot? What the hell is a Spigot?",
            "Trident? Why is greek mythology relevant?",
            "Thread? I'm trying to make a Bukkit plugin, not a scarf.",
            //Actual ChipDev quotes
            "If you aren't using Java's file creator, maybe ask in the library's thread.",
            "Could be helpful to give your plugin DA POWA and have all other plugins cancelled. Think about it.",
            "I taught myself typing and I type with 2 fingers",
            "Don't spoon feed someone. Especially if they don't already have code, That was more like a plugin request.",
            "http://bukkit.org/threads/i-cant-export-my-plugin-java.334362/#post-2974539",
            "You cannot legally get Craftbukkit anymore (Except with Spigot, and all that stuff. Don't get me into that please!)",
            "I remember when we were all only good at programming. You excelled :P",
            "Currently with 211 likes and 1306 MSG's, I get 1 like per 6.18957345972 messages!"
            //End actual ChipDev quotes
    };

    @Command(name = "chipdev")
    public static void cmdChipDev(ChatMessage chat) throws SkypeException {
        String sentence = sentences[new Random().nextInt(Arrays.asList(sentences).size())];

        Resource.sendMessage(chat, sentence);
    }

}
