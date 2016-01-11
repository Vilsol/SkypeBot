package io.mazenmc.skypebot.modules.generated;

import com.google.api.client.util.Joiner;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SunKid implements Module {
    // RIP in spelling
    private static final String[] SENTANCES = new String[] {
            // These are acutal quotes
            "You could make your own packet based of this packet",
            "Google can tell you more than anyone can type in this thread.",
            "I am 17 years old have been programming since the age of so",
            "I am a contributor to the famous Bukkit and CraftBukkit",
            "I am a official developer of TRiBot. The most used Runescape bat",
            "I also have all my Oracle and Cisco certifications",
            "Web 0 HTML 0 CSS 0 JavaScript 0 PHP Applications 0 Java 0 Python 0 NET 0 C++",
            "I currently work as an intern at Oracle",
            "I have decided to sell the bot privately. It will be more effective and harder to detect",
            "Clear your are the closed minded one",
            "You are seriously a moron",
            "Please stop posting you are just a straight out dumb fuck",
            "I also use the same human mouse algorithm is my RSbot",
            "Sorry did that upset you. Maybe you can draw yourself a picture",
            "Mineplex, most likely won't be able to manual ban as they have 20k players", // Not sure wtf this was but ok
            "I'm calling bullshit", 
            "there is no way you made a full implementation of a bot", // there is more but it's funnier without it
            "I'm sending actual key board input and mouse input",
            "You would have to find something that I fucked up on that isn;t the norm",
            "now that everyone is butt hurt and talking shit",
            "Are you trolling?", // by far the best quote
            "If you tp to someone and suspect them for bottling then you can ban them. Good luck detecting who;s bottling", // >bottling
            "I made the bot with 10 hours of free time anyone knowledgeable could re-create it",
            "I made it for the \"lolz\", if the butt hurt strikes then I guess it strikes",
            "I'll put a method to detect the client", // lolwat
            "it crashes a lot when you load resources packs",
            "a market for a minecraft bot",
            "That has no relevance",
            "I'm referring to a client sided bot. Like a hacked client",
            "I'm a contributor to Bukkit I am fully aware of what spigot is used for",
            "Plebs like yourself may find it unimaginable to write code that is from an API so I understand where you are coming from, buddy", // HAHHA LOOOL
            "Minecraft being \"open-source\" takes the tedious part out of reverse engineering.",
            "Runescape hasn't been able to detect it for 14 years",
            "They way I do it in my Runescape bot is identical to the way I do it in Minecraft",
            "community driven makes it harder to defend against as there is no way to protect all servers and there isn't enough budget to per server to invest it human pattern detection.",
            "Challenge accepted",
            // End actual quotes
            
            "I am a master oracle developer",
            "I enjoy non-canadian football",
            "I am comparing Minecraft botting to Russia/Ukraine?", // thanks RiotShielder
            "this thread hurts my brain for multiple reasons"
    };

    @Command(name = "sunkid")
    public static void cmdSunKid(SkypeMessage chat) throws Exception {
        String sentence = SENTANCES[ThreadLocalRandom.current().nextInt(SENTANCES.length)];

        Resource.sendMessage(chat, sentence);
    }
}    
