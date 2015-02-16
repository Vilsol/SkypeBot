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
            "I select everything from the database and sort it when its in an array",
            "EvilSeph is my hero"
    };

    @Command(name = "chipdev")
    public static void cmdChipDev(ChatMessage chat) throws SkypeException {
        String sentence = sentences[new Random().nextInt(Arrays.asList(sentences).size())];

        Resource.sendMessage(chat, sentence);
    }

}
