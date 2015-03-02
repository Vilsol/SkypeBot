package io.mazenmc.skypebot.modules.generated;

import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.skype.ChatMessage;

public class WhatWouldHappen implements Module {

    private static final String[] OPTIONS = new String[] {
            "world domination would occur",
            "an amazing and beautiful thing would happen",
            "[members] would die",
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
            "[members] would become internet famous",
            "TridentSDK would go ~~enterprise~~",
            "Mazen's Skype Chat would become [bad groups]",
            "Mazen's Skype Chat would become a daily podcast",
            "ChipDev would be more popular than PewDiePie",
            "Ubuntu would become the most used operating system",
            "[members] takes over the world",
            "Stephen Hawking would be exposed for never having ALS",
            "[celebrities] would come out as gay",
            "[celebrities] would release to the world that Steve Jobs is still alive",
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
            "Fox News is scared that [current events] will destroy [things we care about]",
            "[members] would kill [members] with [objects]",
            "[members] would ejactulate [objects]",
            "[members] would make a joke about [current events]",
            "[members] would stop using reddit",
            "[members] and [members] would have sexy time",
            "[members] would commit suicide",
            "wo-oh wait",
            "[members] would get a girlfriend",
            "all hell would break loose",
            "[members] would join [bad groups]",
            "[bad groups] would kill everyone in Mazen's Skype Chat",
            "[members] would come out of the closet",
            "[staff] would remove [members]",
            "[members] would DDoS [members]",
            "[members] would become the most hated person alive",
            "[members] and [members] would move in together",
            "[members], [members], and [members] would have a 3 way",
            "[members] would shit themselves",
            "[members] would get a sex change",
            "[members] would get his [body parts] cut off",
            "[members] would cut off [members]'s [body parts]",
            "[members] would rub [members]'s [body parts]",
            "[members] would get [deceased]'s [body parts] in the mail",
            "[members] would get their [body parts] removed by [bad groups]",
            "[members] would be drafted into the [countries]/[countries] war",
            "[members] would be forced to fight the zombie apocalypse with [objects]",
            "[members] would desecrate [deceased]'s grave",
            "[members] would shit fury all over [members]'s ass",
            "[members] would become an internet meme",
            "[members] would become [members]'s slave for the day",
            "[members], [members], and [members] would start an anti feminist cult",
            "[members] would rape [members] with their [body parts]",
            "[celebrities] would be arrested for slapping [celebrities] with [objects]",
            "@swear would be readded",
            "Luke's cooldowns would be removed",
            "[bad groups] would target [things we care about]",
            "@whatwouldhappen would be removed"
    };

    private static final HashMap<String, String[]> DATA = new HashMap<String, String[]>() {{
        put("countries", new String[]{"Argentina", "Brazil", "Russia", "USA", "Canada", "China", "North Korea", "France", "Australia"});
        put("members", new String[]{"Mazen", "Luke", "rowtn", "Troll", "Garrison", "Erik", "Filip", "Jade", "mattrick", "Vilsol", "Mark"});
        put("staff", new String[]{"Mazen", "Jodie", "Drew"});
        put("objects", new String[]{"a dildo", "a knife", "a Mac", "an iPhone", "an anvil", "spiders", "a kangaroo", "a panda", "a member of ISIS", "a velociraptor", "a nerf gun", "goats", "gay people", "a big black dick", "flying sex snakes", "ChipDev", "a banana", "a hot dog", "a dog", "Mazen's mom", "washing machine"});
        put("current events", new String[] {"people dying in Iraq", "the dress", "Gazamo Games releasing in March (sponsor)", "the king of Saudi Arabia dying", "the war against ISIS", "Putin getting bad coffee for breakfast", "the war on drugs", "the US military buying more tanks", "drones", "llamas"});
        put("celebrities", new String[]{"Bill Gates", "The Sheikh", "Obama", "Putin", "Osama Bin Laden", "Bush", "Jesus", "Steve Wozniak", "Randall Munroe", "Tony Abbott, Lord and Saviour"});
        put("deceased", new String[]{"Steve Jobs", "Elvis", "L. Ron Hubbard", "Billy Mays"});
        put("bad groups", new String[]{"the Illumanati", "the KKK", "The New World Order", "the NSA", "ISIS", "Al Qaeda", "FEMA", "Lizard Squad", "Scientology", "the CIA", "Comcast", "Time Warner Cable", "Verizon", "SwiftlyCraft Server"});
        put("things we care about", new String[]{"our children", "the internet", "our homes", "our privacy", "our safety", "our food supply", "our equality", "our maple syrup", "Net Neutrality", "our water supply", "our freedom", "our military", "our navy", "our soldiers"});
        put("deceased", new String[]{"Steve Jobs", "Elvis", "L. Ron Hubbard", "Billy Mays"});
        put("body parts", new String[]{"head", "balls", "dick", "arm", "left nut", "right nut", "arm", "ear", "foot", "ass", "scalp"});
    }};

    @Command(name = "whatwouldhappen")
    public static void whatWouldHappen(ChatMessage chat, String message) throws Exception {
        String option = OPTIONS[new Random().nextInt(OPTIONS.length)];

        for (Map.Entry<String, String[]> s : DATA.entrySet()) {
            while (option.contains("[" + s.getKey() + "]")) {
                option = option.replaceFirst("\\[" + s.getKey() + "\\]", s.getValue()[ThreadLocalRandom.current().nextInt(s.getValue().length)]);
            }
        }
        
        String displayName = chat.getSenderDisplayName().replaceAll("[^A-Za-z0-9 ><.»«]", "");
        
        message = message.replaceAll("\\sI\\s/i", displayName).replaceAll("\\s(my|me)\\s/i", displayName + "'s");

        Resource.sendMessage(chat, message + ", " + option);
    }

}
