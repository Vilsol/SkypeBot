package io.mazenmc.skypebot.modules.generated;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.util.*;

public class BuzzFeed implements Module {
    private static HashMap<String, List<String>> data = new HashMap<String, List<String>>() {{

    }};

    private static List<String> sentences = new ArrayList<String>() {{
        add("BuzzFeed coming soon!");
    }};

    @Command(name = "buzzfeed", cooldown = 15)
    public static void cmdBuzzFeed(ChatMessage chat) throws SkypeException {
        String sentence = sentences.get(new Random().nextInt(Arrays.asList(sentences).size()));

        Random r = new Random();
        for (Map.Entry<String, List<String>> s : data.entrySet()) {
            while (sentence.contains("[" + s.getKey() + "]")) {
                sentence = sentence.replaceFirst("\\[" + s.getKey() + "\\]", s.getValue().get(r.nextInt(Arrays.asList(s.getValue()).size())));
            }
        }

        Resource.sendMessage(chat, sentence);
    }

}
