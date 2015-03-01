package io.mazenmc.skypebot.modules.generated;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BuzzFeed implements Module {
    private static HashMap<String, List<String>> data = new HashMap<String, List<String>>() {{
        put("emotion1", Arrays.asList("amazed", "shocked", "excited", "saddened", "happy", "bored"));
        put("emotion2", Arrays.asList("hate", "love", "like", "admire", "want to kill"));
        put("action", Arrays.asList("blow your mind", "kill your sister", "end your life", "punch a wall", "laugh at Mazen", "think about Jesus", "masturbate vigorously"));
        put("things", Arrays.asList("Ponies", "Kittens", "Java Projects", "DMCA's", "Minecraft Renders", "ChipDev Quotes", "Balloons", "People", "Houses", "Beach Balls", "Emojis", "Japanese People", "iPhone Cases", "Costumes the size of your mother"));

        List<String> numbers = new ArrayList<>();
        for (int i = 5; i < 59; i++) {
            numbers.add(String.valueOf(i));
        }
        put("number", numbers);
    }};

    private static List<String> sentences = new ArrayList<String>() {{
        add("The top [number] [things] that will make you [action]!");
        add("[number] of the sexiest [things] that will leave you [emotion1]");
        add("Which of the [number] [things] do you [emotion2] the most?");
        add("This command needs [number] more sentences. Contribute by typing @git");
    }};

    @Command(name = "buzzfeed", cooldown = 15)
    public static void cmdBuzzFeed(ChatMessage chat) throws SkypeException {
        String sentence = sentences.get(ThreadLocalRandom.current().nextInt(sentences.size()));

        for (Map.Entry<String, List<String>> s : data.entrySet()) {
            while (sentence.contains("[" + s.getKey() + "]")) {
                sentence = sentence.replaceFirst("\\[" + s.getKey() + "\\]", s.getValue().get(ThreadLocalRandom.current().nextInt(s.getValue().size())));
            }
        }

        Resource.sendMessage(chat, sentence);
    }

}
