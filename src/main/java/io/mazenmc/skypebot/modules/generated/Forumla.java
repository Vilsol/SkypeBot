package io.mazenmc.skypebot.modules.generated;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.utils.Resource;

import java.util.concurrent.ThreadLocalRandom;

// @author Luke Anderson | stuntguy3000
// TODO: MOAR
public class Forumla {
    private static String[] sentences = new String[]{
            "ChipDev", "Microsoft", "PornHub", "Your Mother", "Steam", "Java8", "Bukkit", "Facebook", "Twitter", "Instagram", "Sex", "Drugs", "Boobs", "Dicks",
            "Ponies", "Dogs", "Cats", "Mice", "Horses", "Rabbits", "Minecraft", "GTA", "Snapchat", "Tiny Tower", "Tony Abbott", "Kevin Rudd", "Luke", "Mazen",
            "rowtn", "Illuminati", "Bread", "Butter", "Apple", "Orange", "Banana", "Spotify", "iTunes", "Pandora", "Uber", "Girlfriend",
            "Fucking", "Shitting", "Yoga", "Wii Fit", "Nintendo", "Microsoft", "Mojang", "iPhone", "iPad", "iPod", "Steve Jobs", "Australia", "MURICA'" +
            "GitHub", "BitBucket", "Niagara Falls"
    };

    @Command(name = "formula", cooldown = 15)
    public static void cmdForumla(ChatMessage chat) throws SkypeException {
        String thing1 = sentences[ThreadLocalRandom.current().nextInt(sentences.length)];
        String thing2 = sentences[ThreadLocalRandom.current().nextInt(sentences.length)];
        String thing3 = sentences[ThreadLocalRandom.current().nextInt(sentences.length)];
        Resource.sendMessage(chat, thing1 + " + " + thing2 + " = " + thing3);
    }
}
    