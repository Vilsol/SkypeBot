package io.mazenmc.skypebot.modules.generated;

import com.skype.ChatMessage;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class WhatWouldHappen implements Module {

    private static final String[] OPTIONS = new String[] {
            "an amazing and beautiful thing would happen",
            "ChipDev would become president of [countries]",
            "codename_B would quit Hypixel and go to work for [companies]",
            "md_5 would stop reflecting his own classes",
            "ChipDev would remove Dev from his name",
            "Luke would stop posting flickr photos",
            "Vilsol's internet would resurrect",
            "ChipDev would create a useful resource",
            "Yahoo would release something nice",
            "Minecraft would go open-source",
            "[members] would become internet famous",
            "Mazen's Skype Chat would become [bad groups]",
            "Mazen's Skype Chat would become a daily podcast",
            "ChipDev would become more popular than [youtubers]",
            "[operating systems] would become the most used operating system",
            "[members] takes over the world",
            "Stephen Hawking would be exposed for never having ALS",
            "[celebrities] would come out as gay",
            "[celebrities] would release to the world that [deceased] is still alive",
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
            "[news] is scared that [current events] will destroy [things we care about]",
            "[members] would crash mazen.bot",
            "[members] would break the Nokia 3310 with [objects]",
            "[members] would die because of [current events]",
            "[members] would kill [members] with [objects]",
            "[members] would ejactulate [objects]",
            "[members] would make a joke about [current events]",
            "[members] would stop using reddit",
            "[members] and [members] would have sexy time",
            "[members] would commit suicide",
            "[members] would get a girlfriend",
            "all hell would break loose in [countries]",
            "[members] would join [bad groups]",
            "[bad groups] would kill everyone in Mazen's Skype Chat",
            "[members] would come out of the closet",
            "[staff] would remove [members]",
            "[staff] would be replaced by [members]",
            "[members] would DDoS [members]",
            "[members] would water DDoS [members]",
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
            "[members] would be sued by [companies",
            "[members] would move to [countries]",
            "[members] would buy the rights to [projects] for [money]",
            "[members] would convert to [religions]",
            "[members] would convince [members] to convert to [religions]",
            "[celebrities] would be arrested for slapping [celebrities] with [objects]",
            "Luke's cooldowns would be removed",
            "[bad groups] would target [things we care about]",
            "[companies] would hire the ghost of [deceased]",
            "[companies] would be sued by [companies]",
            "[companies] would acquire [companies] for [money]",
            "[companies] would sell [companies] to [companies] for [money]",
            "[companies]'s HQ would explode",
            "[companies] would take over the world",
            "[companies] would hire [members] as the new [jobs]",
            "[companies] would hire [members] as the new secretary to the [jobs]",
            "[companies] would spend [money] to hire [celebrities] as their new [jobs]",
            "[companies] would donate [money] to charity",
            "[companies] would spend [money] bribing the government of [countries]",
            "Xiaomi would copy [companies]",
            "[projects] would succeed",
            "[projects] would shut down",
            "[projects] would go ~~enterprise~~",
            "[projects] would go closed-source",
            "[projects] would only work on [operating systems]",
            "[projects] would sell out to [companies] for [money]",
            "@whatwouldhappen would be replaced with [objects]"
    };

    private static final HashMap<String, String[]> DATA = new HashMap<String, String[]>() {{
        put("countries", new String[]{"Argentina", "Brazil", "Russia", "The US", "Canada", "China", "North Korea", "France", "Australia", "Saudi Arabia", "Egypt", "The UK", "Texas"});
        put("members", new String[]{"Mazen", "Luke", "rowtn", "Troll", "Garrison", "Erik", "Filip", "Jade", "mattrick", "Vilsol", "Mark"});
        put("staff", new String[]{"Mazen", "Jodie", "Drew"});
        put("objects", new String[]{"a dildo", "a knife", "a Mac", "an iPhone", "an anvil", "spiders", "a kangaroo", "a panda", "a member of ISIS", "a velociraptor", "a nerf gun", "goats", "gay people", "a big black dick", "flying sex snakes", "ChipDev", "a banana", "a hot dog", "a dog", "Mazen's mom", "washing machine"});
        put("current events", new String[] {"people dying in Iraq", "the dress", "Gazamo Games releasing in March (sponsor)", "the king of Saudi Arabia dying", "the war against ISIS", "Putin getting bad coffee for breakfast", "the war on drugs", "the US military buying more tanks", "drones", "llamas"});
        put("celebrities", new String[]{"Bill Gates", "The Sheikh", "Obama", "Putin", "Osama Bin Laden", "Bush", "Jesus", "Steve Wozniak", "Randall Munroe", "Tony Abbott, Lord and Saviour", "David Cameron", "Larry Page", "Sergey Brin"});
        put("deceased", new String[]{"Steve Jobs", "Elvis", "L. Ron Hubbard", "Billy Mays"});
        put("bad groups", new String[]{"the Illuminati", "the KKK", "The New World Order", "the NSA", "ISIS", "Al Qaeda", "FEMA", "Lizard Squad", "Scientology", "the CIA", "Comcast", "Time Warner Cable", "Verizon", "SwiftlyCraft Server"});
        put("things we care about", new String[]{"our children", "the internet", "our homes", "our privacy", "our safety", "our food supply", "our equality", "our maple syrup", "Net Neutrality", "our water supply", "our freedom", "our military", "our navy", "our soldiers"});
        put("deceased", new String[]{"Steve Jobs", "Elvis", "L. Ron Hubbard", "Billy Mays", "Adolf Hitler", "Joseph Stalin"});
        put("body parts", new String[]{"head", "balls", "dick", "arm", "left nut", "right nut", "arm", "ear", "foot", "ass", "scalp"});
        put("companies", new String[]{"Apple", "Microsoft", "Mojang", "GoPro", "Samsung", "HTC", "Valve", "reddit", "Facebook", "Google", "Yahoo", "Intel", "HP", "Lenovo", "IBM", "Nokia", "Walmart", "Kmart", "Costco", "Gazamo"});
        put("youtubers", new String[]{"PewDiePie", "SkyDoesMinecraft", "Smosh", "vsauce", "CaptainSparklez", "RoosterTeeth", "freddiew", "the Yogscast", "Tobuscus", "SeaNanners"});
        put("money", new String[]{"$2.5 billion", "10 cents", "$19.5 Billion", "some loose change and a bus pass", "an incomprehensible amount of money", "12 rupees", "50 bucks", "$0.02", "pretty much nothing", "1 MILLION DOLLARS"});
        put("projects", new String[]{"TridentSDK", "Sponge", "Bukkit", "Spigot", "Glowstone", "Rainbow", "Granite"});
        put("news", new String[]{"Fox News", "CNN", "The Daily Mail", "The New York Times"});
        put("jobs", new String[]{"CEO", "CFO", "Vice President of Networking", "janitor", "intern", "spokesperson"});
        put("operating systems", new String[]{"Ubuntu", "Fedora", "Debian", "Gentoo", "CentOS", "Red Star OS", "Mac OS X", "MS Dos", "Vista"});
        put("religions", new String[]{"Christianity", "Mormonism", "Moronism", "Islam", "Judaism"});
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
