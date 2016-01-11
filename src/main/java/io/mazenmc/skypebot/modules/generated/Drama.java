package io.mazenmc.skypebot.modules.generated;

import xyz.gghost.jskype.message.Message;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Drama implements Module {

    private static HashMap<String, String[]> data = new HashMap<String, String[]>() {{
        put("people", new String[]{"jeb", "Notch", "Mojang", "Curse", "Searge", "mbaxter", "Garrison", "Komp", "Gaben", "RawCode", "Vilsol", "md_5", "sk89q", "MazenMC", "hawkfalcon", "New Bukkit Staff", "Wolvereness", "CaptainBern", "Hoolean", "drtshock", "EvilSeph", "TnT", "lukegb", "codename_B", "Cryptkeeper", "hintss", "Saad", "Obama", "TheMogMiner", "TridentSDK Staff", "Reddit's Hive Mind", "/r/Minecraft", "macguy8 (Colin)", "ChipDev"});
        put("sites", new String[]{"Reddit", "4chan", "GitHub", "Slack", "Skype", "BitBucket", "IRC", "Google+", "Twitch", "Bukkit Forums", "Trident Forums", "Spigot Forums", "Sponge Forums", "The YouTube comment section", "Google"});
        put("things", new String[]{"Bukkit", "Trident", "TridentSDK", "Spigot", "Sponge", "WorldEdit", "Essentials", "WorldModify", "Google", "Spout", "ForgeCraft", "BungeeCord", "Mazen's Skype Bot"});
        put("networks", new String[]{"EA", "Ubisoft", "Hypixel", "ArkhamNetwork", "Mineplex", "Minecade", "GazamoGames", "ShotBow", "KitPVP"});
        put("function", new String[]{"MJ support", "RF support", "EU support", "NSA spying", "FMP compatibility", "quarries", "automatic mining", "GregTech balance", "ComputerCraft APIs", "OpenComputers APIs", "Bukkit plugin compatibility", "MCPC+ support", "ID allocation", "ore processing", "smelting", "crafting", "balance", "bees", "ThaumCraft integration", "realism", "decorative blocks", "new mobs", "TCon tool parts", "new wood types", "bundled cable support", "new player capes", "more drama", "less drama", "microblocks", "drama generation commands", "Blutricity support", "overpowered items", "underpowered items", "new ores", "better SMP support", "achievements", "quests", "more annoying worldgen", "virus generation", "allow viruses to be sent to the Minecraft Client"});
        put("adj", new String[]{"bad", "wrong", "illegal", "horrible", "nasty", "not in ForgeCraft", "noncompliant with Mojang's EULA", "a serious problem", "incompatible", "a waste of time", "wonderful", "amazing", "toxic", "too vanilla", "shameful", "disappointing", "bloated", "outdated", "incorrect", "full of drama", "too realistic", "too cleanroom", "epic", "amazing", "overrated", "disgusting", "genuine", "puzzling"});
        put("badsoft", new String[]{"malware", "spyware", "adware", "DRM", "viruses", "trojans", "keyloggers", "stolen code", "easter eggs", "potential login stealers", "adf.ly links", "bad code", "stolen assets", "malicious code", "secret backdoors", "boilerplate code", "BuzzFeed", "9gag", "Justin Bieber"});
        put("drama", new String[]{"bugs", "crashes", "drama", "lots of drama", "imbalance", "pain and suffering", "piracy", "bees", "adf.ly"});
        put("crash", new String[]{"crash", "explode", "break", "lag", "blow up", "corrupt chunks", "corrupt worlds", "rain hellfish", "spawn bees"});
        put("ban", new String[]{"ban", "kick", "put a pumpkin of shame on", "add items mocking", "blacklist", "whitelist", "give admin rights to", "shame", "destroy"});
        put("code", new String[]{"code", "assets", "ideas", "concepts", "a single function", "5 lines of code", "a class", "a few files", "a ZIP file", "Gradle buildscripts", "a GitHub repository"});
        put("worse", new String[]{"worse", "better", "faster", "slower", "more stable", "less buggy", "stronger", "stable", "bugger"});
        put("ac1", new String[]{"sue", "destroy the life of", "flame", "cause drama about", "complain about", "kick"});
        put("price", new String[]{"200$", "250$", "300$", "350$", "400$", "450$", "500$", "600$", "7000$", "10000000$", "1 Billion Dollars", "2 Billion Dollars", "0.1$", "0.000001$", "1$"});
        put("activates", new String[]{"activates", "works", "functions", "breaks", "corrupts", "destroys"});
        put("says", new String[]{"says", "tweets", "claims", "confirms", "denies", "accepts", "posts", "notes"});
        put("enormous", new String[]{"big", "large", "huge", "gigantic", "enormous", "tiny", "singular", "microscopic"});
    }};
    private static String[] sentences = new String[]{
            "[people] launched a DoS attack on the website of [things]",
            "[sites] urges everyone to stop using [things]",
            "After a [enormous] amount of requests, [networks] removes [things]",
            "After a [enormous] amount of requests, [networks] adds [things]",
            "After a [enormous] amount of requests, [networks] adds [function] to [things]",
            "[people] plays [things] on Twitch",
            "[people] fixes [function] in [things] to be unlike [things]",
            "[things] makes [things] [crash], [sites] users complain",
            "[people] complained about being in [things] on [sites]",
            "[people] releases [code] of [things] for [price]",
            "[sites] considers [things] worse than [things]",
            "[people] made [things] depend on [things]",
            "[people] bans [people] from using [things] in [networks]",
            "[people] complains that [things] discussion doesn't belong on [sites]",
            "[people] has a Patreon goal to add [function] to [things] for [price] a month",
            "[people] has a Patreon goal to add [things] compatibility to [things] for [price] a month",
            "[people] complains that [people] replaced [things] by [things]",
            "[people] complains that [people] replaced [things] by [things] in [networks]",
            "[people] complains that [people] removed [function] in [networks]",
            "[people] decided that [things] is too [adj] and replaced it with [things]",
            "[people] [says] [things] is [adj].",
            "[people] [says] [things] is literally [adj].",
            "[things] is not updated for the latest version of Minecraft.",
            "[people] removes [things] from [networks].",
            "[people] adds [things] to [networks].",
            "[people] quits modding. Fans of [things] rage.",
            "[people] is found to secretly like [things]",
            "[people] openly hates [function] in [things]",
            "[people] threatens to [ac1] [people] until they remove [things] from [networks]",
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
            "[people] mocks [people]'s [code] on [sites]",
            "[people] [says] that [things] should be more like [things]",
            "[people] [says] that [things] should be less like [things]",
            "[people] rebalances [things] for [networks]",
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
            "[people] decides to [ban] [people] from [networks]",
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
            "[things] adds [badsoft] that only [activates] in [networks]",
            "[things] adds [badsoft] that only [activates] alongside [things]",
            "[things] makes [people] invincible from [things] in [networks]",
            "[people] decides to base their entire modpack on [things]",
            "[people] tweaks balance in [things] too much, annoying [sites]",
            "[people] tweaks balance in [things] too much, annoying [people]",
            "[people] [says] [people] is worse than [people]",
            "[people] [says] [things] is [worse] than [things]",
            "[people] bans [people] from [sites]",
            "[things] [ac1]s [function]",
            "[people] hates [people]",
            "[things] hates [things]",
            "[people] wants to [ac1] [people]",
            "[things] is planning to release [badsoft] onto the world",
            "[people] is forking [things]",
            "[people] is forking [things] because of vigorous complaints from [sites]",
            "[people] forked [things] to add [function]",
            "[people] forked [things] to remove [function]",
            "[people] star-bombed [things] on GitHub"
    };

    @Command(name = "drama", cooldown = 15)
    public static void cmdDrama(Message chat) throws Exception {
        String sentence = sentences[ThreadLocalRandom.current().nextInt(Arrays.asList(sentences).size())];

        for (Map.Entry<String, String[]> s : data.entrySet()) {
            while (sentence.contains("[" + s.getKey() + "]")) {
                sentence = sentence.replaceFirst("\\[" + s.getKey() + "\\]", s.getValue()[ThreadLocalRandom.current().nextInt(Arrays.asList(s.getValue()).size())]);
            }
        }

        Resource.sendMessage(chat, sentence);
    }

}
