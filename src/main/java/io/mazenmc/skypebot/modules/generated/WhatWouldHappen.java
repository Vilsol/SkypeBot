package io.mazenmc.skypebot.modules.generated;

import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.skype.ChatMessage;

public class WhatWouldHappen implements Module {

    private static final String[] OPTIONS = new String[] {
        "World Domination would occur",
        "an amazing and beautiful thing would happen",
        "Mazen would die",
        "ChipDev would become president",
        "codename_B would quit Hypixel",
        "Gazamo would take over the world",
        "md_5 would stop reflecting his own classes",
        "ChipDev would remove Dev from his name",
        "TridentSDK would succeed",
        "Luke would stop posting flickr photos",
        "Vilsol's internet would resurrect",
        "ChipDev would create a useful resource",
        "Microsoft would sell Mojang to Apple",
        "Google would buy Apple",
        "Yahoo would release something nice",
        "Apple's HQ would explode",
        "Minecraft would go open-source",
        "Mazen would become internet famous",
        "TridentSDK would go ~~enterprise~~",
        "Mazen's Skype Chat would become the illuminati",
        "Mazen's Skype Chat would become a daily podcast",
        "ChipDev would be more popular than PewDiePie",
        "Ubuntu would become the most used operating system",
        "Gentoo takes over the world",
        "Stephen Hawking would be exposed for never having ALS",
        "Garrison would kill himself",
        "Garrison would make a joke about people dying in Iraq",
        "Bill Gates would come out gay",
        "Steve Wozniak would release to the world that Steve Jobs is still alive",
        "the Nokia 3310 would break",
        "North Korea would nuke [countries]",
        "[countries] would start a war against [countries]",
        "Jade would stop caring about being pinged",
        "Mazen would lose his virginity",
        "Mazen would stop making apps to decorate Christmas trees",
        "md_5 would sell Spigot to Mojang without telling anyone",
        "md_5 would go to work for Mojang",
        "rowtn would have good internet",
        "Mazen's Skype Chat would be killed by Lizard Squad (again)",
        "[countries] would nuke [countries]",
        "ADDITIONAL PYLONS WOULD BE CONSTRUCTED",
        "mazen.bot would die",
        "[members] would kill [members] with [objects]",
        "[members] would ejactulate [objects]"
    };

    private static final HashMap<String, String[]> DATA = new HashMap<String, String[]>() {{
        put("countries", new String[]{"Argentina", "Brazil", "Russia", "USA", "Canada", "China", "North Korea", "France", "Australia"});
        put("members", new String[]{"Mazen", "Luke", "rowtn", "Troll", "Garrison", "Erik", "Filip", "Jade", "mattrick", "Vilsol", "Mark"});
        put("objects", new String[]{"a dildo", "a knife", "a Mac", "an iPhone", "an anvil", "spiders", "a kangaroo", "a panda", "a member of ISIS", "a velociraptor", "a nerf gun"});
    }};

    @Command(name = "whatwouldhappen")
    public static void whatWouldHappen(ChatMessage chat, String message) {
        String option = OPTIONS[ThreadLocalRandom.current().nextInt(OPTIONS.length)];

        for (Map.Entry<String, String[]> s : DATA.entrySet()) {
            while (option.contains("[" + s.getKey() + "]")) {
                option = option.replaceFirst("\\[" + s.getKey() + "\\]", s.getValue()[ThreadLocalRandom.current().nextInt(s.getValue().length)]);
            }
        }

        Resource.sendMessage(chat, message + ", " + option);
    }

}
