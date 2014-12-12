package me.vilsol.skypebot.modules;

import com.skype.ChatMessage;
import me.vilsol.skypebot.engine.bot.Command;
import me.vilsol.skypebot.engine.bot.Module;
import me.vilsol.skypebot.utils.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Drama implements Module {

    private static HashMap<String, String[]> data = new HashMap<String, String[]>() {{
        put("people", new String[] {"Player", "jadedcat", "Alblaka", "Greg", "Eloraam", "jeb", "Notch", "Mojang", "Curse", "Searge", "Jade", "mbaxter", "Garrison", "RawCode", "Vilsol", "md_5", "sq89q", "MazenMC", "hawkfalcon", "New Bukkit Staff", "Wolvereness", "CaptainBern", "Hoolean", "drtshock", "EvilSeph", "TnT", "lukegb", "ArkhamNetwork", "Mineplex", "Hypixel", "codename_B", "Cryptkeeper", "hintss"});
        put("sites", new String[] {"Reddit", "4chan", "GitHub", "BitBucket", "IRC", "Google+", "Twitch", "Bukkit Forums", "Trident Forums", "Spigot Forums", "Sponge Forums", "The YouTube comment section"});
        put("things", new String[] {"Bukkit", "Trident", "TridentSDK", "Spigot", "Sponge", "WorldEdit", "Essentials", "WorldModify", "Google", "Spout", "ForgeCraft"});
        put("packs", new String[] {"Feed The Beast", "the ForgeCraft pack", "FTB Monster", "FTB Unstable", "Agrarian Skies", "Direwolf20 Pack", "Tekkit", "Hexxit", "ATLauncher", "Resonant Rise", "MCUpdater", "Attack of the B-Team", "Mindcrack", "Magic Maiden", "ForgeCraft", "Technic"});
        put("function", new String[] {"MJ support", "RF support", "EU support", "FMP compatibility", "quarries", "automatic mining", "GregTech balance", "ComputerCraft APIs", "OpenComputers APIs", "Bukkit plugin compatibility", "MCPC+ support", "ID allocation", "ore processing", "smelting", "crafting", "balance", "bees", "ThaumCraft integration", "realism", "decorative blocks", "new mobs", "TCon tool parts", "new wood types", "bundled cable support", "new player capes", "more drama", "less drama", "microblocks", "drama generation commands", "Blutricity support", "overpowered items", "underpowered items", "new ores", "better SMP support", "achievements", "quests", "more annoying worldgen", "virus generation", "allow viruses to be sent to the Minecraft Client"});
        put("adj", new String[] {"bad", "wrong", "illegal", "horrible", "nasty", "not in ForgeCraft", "noncompliant with Mojang's EULA", "a serious problem", "incompatible", "a waste of time", "wonderful", "amazing", "toxic", "too vanilla", "shameful", "disappointing", "bloated", "outdated", "incorrect", "full of drama", "too realistic", "too cleanroom"});
        put("badsoft", new String[] {"malware", "spyware", "adware", "DRM", "viruses", "trojans", "keyloggers", "stolen code", "easter eggs", "potential login stealers", "adf.ly links", "bad code", "stolen assets", "malicious code", "secret backdoors"});
        put("drama", new String[] {"bugs", "crashes", "drama", "lots of drama", "imbalance", "pain and suffering", "piracy", "bees", "adf.ly"});
        put("crash", new String[] {"crash", "explode", "break", "lag", "blow up", "corrupt chunks", "corrupt worlds", "rain hellfish", "spawn bees"});
        put("ban", new String[] {"ban", "kick", "put a pumpkin of shame on", "add items mocking", "blacklist", "whitelist", "give admin rights to", "shame", "destroy"});
        put("code", new String[] {"code", "assets", "ideas", "concepts", "a single function", "5 lines of code", "a class", "a few files", "a ZIP file", "Gradle buildscripts", "a GitHub repository"});
        put("worse", new String[] {"worse", "better", "faster", "slower", "more stable", "less buggy"});
        put("ac1", new String[] {"sue", "destroy the life of", "flame", "cause drama about", "complain about", "kick"});
        put("price", new String[] {"200$", "250$", "300$", "350$", "400$", "450$", "500$", "600$"});
        put("activates", new String[] {"activates", "works", "functions", "breaks"});
        put("says", new String[] {"says", "tweets", "claims", "confirms", "denies"});
        put("enormous", new String[] {"big", "large", "huge", "gigantic", "enormous"});
    }};
    private static String[] sentences = new String[] {
            "[people] launched a DoS attack on the website of [things]",
            "[sites] urges everyone to stop using [things]",
            "After a [enormous] amount of requests, [packs] removes [things]",
            "After a [enormous] amount of requests, [packs] adds [things]",
            "After a [enormous] amount of requests, [packs] adds [function] to [things]",
            "[people] plays [things] on Twitch",
            "[people] fixes [function] in [things] to be unlike [things]",
            "[things] makes [things] [crash], [sites] users complain",
            "[people] complained about being in [things] on [sites]",
            "[people] releases [code] of [things] for [price]",
            "[sites] considers [things] worse than [things]",
            "[people] made [things] depend on [things]",
            "[people] bans [people] from using [things] in [packs]",
            "[people] complains that [things] discussion doesn't belong on [sites]",
            "[people] has a Patreon goal to add [function] to [things] for [price] a month",
            "[people] has a Patreon goal to add [things] compatibility to [things] for [price] a month",
            "[people] complains that [people] replaced [things] by [things]",
            "[people] complains that [people] replaced [things] by [things] in [packs]",
            "[people] complains that [people] removed [function] in [packs]",
            "[people] decided that [things] is too [adj] and replaced it with [things]",
            "[people] [says] [things] is [adj].",
            "[people] [says] [things] is literally [adj].",
            "[things] is not updated for the latest version of Minecraft.",
            "[people] removes [things] from [packs].",
            "[people] adds [things] to [packs].",
            "[people] quits modding. Fans of [things] rage.",
            "[people] is found to secretly like [things]",
            "[people] openly hates [function] in [things]",
            "[people] threatens to [ac1] [people] until they remove [things] from [packs]",
            "[people] threatens to [ac1] [people] until they remove [function] from [things]",
            "[people] threatens to [ac1] [people] until they add [function] to [things]",
            "[people] came out in support of [things]",
            "[people] came out in support of [drama]",
            "[people] and [people] came out in support of [drama]",
            "[people] came out against [drama], [sites] rages",
            "[people] and [people] came out against [drama], [sites] rages",
            "[people] forks [things] causing [drama]",
            "[people] [says] to replace [things] with [things]",
            "[people] [says] [people] causes drama",
            "[things] fans claim that [things] should be more like [things]",
            "[things] fans claim that [things] should have better [function]",
            "[people] DMCAs [things]",
            "[people] mocks [people]'s [code] on [site]",
            "[people] [says] that [things] should be more like [things]",
            "[people] [says] that [things] should be less like [things]",
            "[people] rebalances [things] for [packs]",
            "[people] adds [function] to [things] by request of [people]",
            "[people] removes [function] from [things] by request of [people]",
            "[people] removes compatibility between [things] and [things] by request of [people]",
            "[people] [says] [people]'s attitude is [adj]",
            "[people] [says] [sites]'s attitude is [adj]",
            "[people] quits the development team of [things]",
            "[people] [says] [things] is too much like [things]",
            "[people] [says] [things] is a ripoff of [things]",
            "[people] [says] [people] stole [code] from [people]",
            "[people] [says] [people] did not steal [code] from [people]",
            "[people] decides to [ban] [people] from [packs]",
            "[things] doesn't work with [things] since the latest update",
            "[people] sues [things]",
            "[people] [says] [things] is [adj] on [sites]",
            "[people] [says] [things] is full of [badsoft]",
            "[people] [says] [things] causes [drama]",
            "[people] [says] [things] causes [drama] when used with [things]",
            "[people] [says] using [things] and [things] together is [adj]",
            "[people] rants about [things] on [sites]",
            "[people] rants about [function] in mods on [sites]",
            "[people] steals code from [things]",
            "[things] breaks [function]",
            "[people] sues [things] developers",
            "[people] reminds you that [things] is [adj]",
            "[people] and [people] get into a drama fight on [sites]",
            "Fans of [things] and [things] argue on [sites]",
            "[people] and [people] argue about [things]",
            "[people] puts [badsoft] in [things]",
            "[people] complains about [things] breaking [things]",
            "[people] complains about [things] breaking [function]",
            "[people] complains about [things] including [function]",
            "[things] breaks [function] in [things]",
            "[things] breaks [things] support in [things]",
            "[things] adds code to [ban] [people] automatically",
            "[things] adds code to [ban] people using [things]",
            "[things] removes compatibility with [things]",
            "[people] [says] not to use [things]",
            "[people] [says] not to use [things] with [things]",
            "[people] finds [badsoft] in [things]",
            "[people] drew a nasty graffiti about [people]",
            "[people] drew a nasty graffiti about [things]",
            "[things] makes [things] [crash] when used with [things]",
            "[things] makes [things] [crash] when used by [people]",
            "[things] makes [things] crash [things] when used by [people]",
            "[things] adds [badsoft] that only [activates] in [packs]",
            "[things] adds [badsoft] that only [activates] alongside [things]",
            "[things] makes [people] invincible from [things] in [packs]",
            "[people] decides to base their entire modpack on [things]",
            "[people] tweaks balance in [things] too much, annoying [sites]",
            "[people] tweaks balance in [things] too much, annoying [people]",
            "[people] [says] [people] is worse than [people]",
            "[people] [says] [things] is [worse] than [things]",
            "[people] bans [people] from [sites]"
    };

    @Command(name = "drama")
    public static void cmdDrama(ChatMessage chat){
        String sentence = sentences[new Random().nextInt(Arrays.asList(sentences).size())];

        Random r = new Random();
        for(Map.Entry<String, String[]> s : data.entrySet()){
            while(sentence.contains("[" + s.getKey() + "]")){
                sentence = sentence.replaceFirst("\\[" + s.getKey() + "\\]", s.getValue()[r.nextInt(Arrays.asList(s.getValue()).size())]);
            }
        }

        R.s(sentence);
    }

}
