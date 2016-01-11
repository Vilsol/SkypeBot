package io.mazenmc.skypebot.modules.generated;

import xyz.gghost.jskype.message.Message;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.util.Random;

// @author Luke Anderson | stuntguy3000
// TODO: MOAR
public class Forumla implements Module {
    private static String[] sentences = new String[]{
            "ChipDev", "Microsoft", "PornHub", "Your Mother", "Steam", "Java8", "Bukkit", "Facebook", "Twitter", "Instagram", "Sex", "Drugs", "Boobs", "Dicks",
            "Ponies", "Dogs", "Cats", "Mice", "Horses", "Rabbits", "Minecraft", "GTA", "Snapchat", "Tiny Tower", "Tony Abbott", "Kevin Rudd", "Luke", "Mazen",
            "rowtn", "Illuminati", "Bread", "Butter", "Apple", "Orange", "Banana", "Spotify", "iTunes", "Pandora", "Uber", "Girlfriend",
            "Fucking", "Shitting", "Yoga", "Wii Fit", "Nintendo", "Microsoft", "Mojang", "iPhone", "iPad", "iPod", "Steve Jobs", "Australia", "MURICA'" +
            "GitHub", "BitBucket", "Niagara Falls"
    };

    @Command(name = "formula", cooldown = 15)
    public static void cmdForumla(Message chat) throws Exception {
        Random random = new Random();
        String thing1 = sentences[random.nextInt(sentences.length)];
        String thing2 = sentences[random.nextInt(sentences.length)];
        String thing3 = sentences[random.nextInt(sentences.length)];
        Resource.sendMessage(chat, thing1 + " + " + thing2 + " = " + thing3);
    }
}
    