package io.mazenmc.skypebot.modules;

import com.skype.ChatMessage;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.utils.Resource;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Programmed by DevRo on 28/02/15.
 */
public class WhatWouldHappen {

    /**
     * I put this in a separate class because the options will probably grow a bunch.
     */

    private static final String[] options = new String[]{"World Domination would occur", "An amazing and beautiful thing would happen", "Mazen would die", "ChipDev would become president", "codeaname_B would quit hypixel", "Gazamo would take over the world"};

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    @Command(name = "whatwouldhappen")
    public static void whatWouldHappen(ChatMessage chat, String message) {
        String option = options[random.nextInt(options.length)];
        Resource.sendMessage(chat, "If " + message + " happened, " + option);
    }

}
