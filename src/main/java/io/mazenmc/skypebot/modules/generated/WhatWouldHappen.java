package io.mazenmc.skypebot.modules.generated;

import com.skype.ChatMessage;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.util.concurrent.ThreadLocalRandom;

public class WhatWouldHappen implements Module {

    private static final String[] OPTIONS = new String[] {
            "World Domination would occur",
            "An amazing and beautiful thing would happen",
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
            "Minecraft would go open-source"
    };

    @Command(name = "whatwouldhappen")
    public static void whatWouldHappen(ChatMessage chat, String message) {
        String option = OPTIONS[ThreadLocalRandom.current().nextInt(OPTIONS.length)];
        Resource.sendMessage(chat, "If " + message + " happened, " + option);
    }

}
