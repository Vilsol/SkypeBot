package io.mazenmc.skypebot.modules.generated;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ChipDev implements Module {

    private static final String[] SENTANCES = new String[] {
            "I select everything from the database and sort it when its in an array.",
            "EvilSeph is my hero.",
            "I only need one class for my plugin",
            "Spigot? What the hell is a Spigot?",
            "Trident? Why is greek mythology relevant?",
            "Thread? I'm trying to make a Bukkit plugin, not a scarf.",
            "Generics? My code is too generic for that.",
            "An api is like training wheels, allows people to program easier. Once you get good, you can start not using an api and expand.",
            //Actual ChipDev quotes
            "If you aren't using Java's file creator, maybe ask in the library's thread.",
            "Could be helpful to give your plugin DA POWA and have all other plugins cancelled. Think about it.",
            "I taught myself typing and I type with 2 fingers",
            "Don't spoon feed someone. Especially if they don't already have code, That was more like a plugin request.",
            "http://bukkit.org/threads/i-cant-export-my-plugin-java.334362/#post-2974539",
            "You cannot legally get Craftbukkit anymore (Except with Spigot, and all that stuff. Don't get me into that please!)",
            "I remember when we were all only good at programming. You excelled :P",
            "Currently with 211 likes and 1306 MSG's, I get 1 like per 6.18957345972 messages!",
            "I wish I would've bought github plus for privacy :/",
            "You CANNOT share this link with others, hint hint mazen.",
            "Also, don't make a plugin inside of the JavaPlugin, use the main instance for non NPE'S",
            "https://gist.github.com/ChipDev/fa03cdc3811a81b86101",
            "The github cat looks like a octopus. o.o",
            "When I started Bukkit, I thought Java was coffee. Still the same thought.",
            "Scoreboards haunt me.",
            "I liek trains",
            "Im a straight A student. Not counting anything but math.",
            "Why watch TV, when you can PLAY TV?",
            "^ sucks..    lollypops",
            "I suck, (Lollypops!)",
            "i Don wanna endanger my 5k comp.",
            "Shut up is a good thing! Its the opposite of Shut down, which is turn off != turn on..... You are telling someone to turn on (keep talking!)"
            //End actual ChipDev quotes
    };

    @Command(name = "chipdev")
    public static void cmdChipDev(ChatMessage chat) throws SkypeException {
        String sentence = SENTANCES[ThreadLocalRandom.current().nextInt(SENTANCES.length)];

        Resource.sendMessage(chat, sentence);
    }

}
