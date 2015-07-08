package io.mazenmc.skypebot.modules;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.skype.ChatMessage;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;
import io.mazenmc.skypebot.SkypeBot;
import io.mazenmc.skypebot.engine.bot.*;
import io.mazenmc.skypebot.engine.bot.Optional;
import io.mazenmc.skypebot.stat.MessageStatistic;
import io.mazenmc.skypebot.stat.StatisticsManager;
import io.mazenmc.skypebot.utils.Resource;
import io.mazenmc.skypebot.utils.Utils;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class General implements Module {
    private static ChatterBotSession cleverBot;
    private static ChatterBotSession jabberWacky;
    private static Thread rantThread;
    private static boolean ranting = false;

    @Command(name = "8ball")
    public static void cmd8Ball(ChatMessage chat, @Optional
            String question) {
        String[] options = new String[]{"It is certain", "It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes", "Reply hazy try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again", "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};
        int chosen = ThreadLocalRandom.current().nextInt(options.length);
        Resource.sendMessage(chat, options[chosen]);
    }

    @Command(name = "flickr.com/photos/stuntguy3000", exact = false, command = false)
    public static void cmdStuntguyFlickr(ChatMessage chat) throws SkypeException {
        if (chat.getSender().getId().equalsIgnoreCase("stuntguy3000")) {
            Resource.sendMessage("Nobody likes your photos, Luke.");
        }
    }

    @Command(name = "about")
    public static void cmdAbout(ChatMessage chat) {
        Resource.sendMessage(chat, "Originally created by Vilsol, reincarnated and improved by MazenMC and stuntguy3000");
        Resource.sendMessage(chat, "Version: " + Resource.VERSION);
    }

    @Command(name = "bot")
    public static void cmdBot(ChatMessage chat, String message) {
        Resource.sendMessage("/me " + message);
    }

    @Command(name = "choice")
    public static void choice(ChatMessage chat, String message) {
        String[] choices = message.trim().split(",");

        if (choices.length == 1) {
            Resource.sendMessage("Give me choices!");
            return;
        }

        Resource.sendMessage("I say " + choices[ThreadLocalRandom.current().nextInt(choices.length)].trim());
    }

    @Command(name = "c")
    public static void cmdC(ChatMessage chat, String question) throws SkypeException {
        Resource.sendMessage(chat, SkypeBot.getInstance().askQuestion(question));
    }

    @Command(name = "doc")
    public static void cmdDoc(ChatMessage chat) {
        Resource.sendMessage(chat, "https://docs.google.com/document/d/1LoTYCauVyEiiLZ5Klw3UB8rbEtkzNn7VLmgm87Fzyy0/edit#");
    }

    @Command(name = "fish go moo", exact = false, command = false)
    public static void cmdFishGoMoo(ChatMessage chat) throws SkypeException {
        Resource.sendMessage("/me notes that " + chat.getSenderDisplayName() + " is truly enlightened.");
    }

    @Command(name = "git", alias = {"repo", "repository", "source"})
    public static void cmdGit(ChatMessage chat) {
        Resource.sendMessage(chat, "Git Repository: https://github.com/MazenMC/SkypeBot");
    }

    @Command(name = "help", alias = {"commands"})
    public static void cmdHelp(ChatMessage chat) {
        String commands = "";

        for (Map.Entry<String, CommandData> data : ModuleManager.getCommands().entrySet()) {
            if (!data.getValue().getCommand().exact()) {
                continue;
            }

            if (!commands.equals("")) {
                commands += ", ";
            }

            commands += Resource.COMMAND_PREFIX + data.getKey();

            if (!data.getValue().getParameterNames().equals("")) {
                commands += " " + data.getValue().getParameterNames();
            }
        }

        Resource.sendMessage(chat, "Available Commands: " + Utils.upload(commands));
    }

    @Command(name = "lmgtfy")
    public static void cmdLmgtfy(ChatMessage chat, String question) throws SkypeException {
        String returnString = "http://lmgtfy.com/?q=";
        Resource.sendMessage(chat, returnString + URLEncoder.encode(question));
    }

    @Command(name = "stats")
    public static void cmdStats(ChatMessage chat, @Optional String person) throws SkypeException {
        if (person != null) {
            MessageStatistic stat = StatisticsManager.instance().statistics().get(person);

            if (stat == null) {
                Resource.sendMessage("No found statistic for " + person + "!");
                return;
            }

            User user = Skype.getUser(person);
            String name = user.getDisplayName();

            if ("".equals(name))
                name = user.getFullName();

            if ("".equals(name))
                name = person;

            Resource.sendMessage("------ " + name + "'s statistics ------");
            Resource.sendMessage("Message count: " + stat.messageAmount());
            Resource.sendMessage("Command count: " + stat.commandCount());
            if (name.equals("troll.dude.3") || name.equals("julian.ayy")) {
                Resource.sendMessage("Random message: LOOOOOOLL LMAO LMFAO ROFL 4111RRRRR AIIIR OMG LOOOl");
            } else {
                Resource.sendMessage("Random message: " + stat.randomMessage());
            }
            Resource.sendMessage("Percent of Messages which were commands: " + Math.round(stat.commandPercent()) + "%");
            Resource.sendMessage("---------------------------------------");
            return;
        }

        List<MessageStatistic> messages = new ArrayList<>(StatisticsManager.instance()
                .statistics()
                .values());

        Collections.sort(messages, (a, b) -> b.messageAmount() - a.messageAmount());
        
        double total = messages.stream()
                .mapToInt(MessageStatistic::messageAmount)
                .sum();

        Resource.sendMessage("---------------------------------------");

        IntStream.range(0, 5).forEach((i) -> {
            if (messages.size() <= i)
                return;

            MessageStatistic stat = messages.get(i);
            
            long percentage = Math.round((stat.messageAmount()/total)*100);

            Resource.sendMessage(StatisticsManager.instance().ownerFor(stat) + ": " +
                    stat.messageAmount() + " - " + percentage + "%");
        });

        Resource.sendMessage("---------------------------------------");

        Resource.sendMessage(total + " total messages sent in this chat (which has been logged by the bot)");
        Resource.sendMessage(messages.size() + " members sent messages which were acknowledged by the bot");

        List<List<String>> raw = messages.stream()
                .map(MessageStatistic::messages)
                .collect(Collectors.toList());
        List<String> msgs = new ArrayList<>((int) total);

        raw.forEach(msgs::addAll);

        double commands = msgs.stream()
                .filter((s) -> s.startsWith("@"))
                .count();
        long characters = msgs.stream()
                .mapToLong(String::length)
                .sum();

        Resource.sendMessage(Math.round(((commands / total) * 100)) + "% of those messages were commands");
        Resource.sendMessage(characters + " characters were sent");
        Resource.sendMessage(commands + " commands were sent");
    }

    @Command(name = "md5")
    public static void cmdMd5(ChatMessage chat) {
        String s = "md_1 = 1% of devs (people who know their shit)\n" +
                "md_2 = uses one class for everything\n" +
                "md_3 = true == true, yoo!\n" +
                "md_4 = New instance to call static methods\n" +
                "md_5 = reflects his own classes\n" +
                "md_6 = return this; on everything\n" +
                "md_7 = abstract? never heard of it\n" +
                "md_8 = interface? never heard of it\n" +
                "md_9 = enum? never heard of it\n" +
                "md_10 = java? never heard of it";
        Resource.sendMessage(s);
    }

    @Command(name = "ping")
    public static void cmdPing(ChatMessage chat, @Optional
            final String ip) throws JSONException, SkypeException {
        if (ip == null) {
            Resource.sendMessage(chat, "Pong");
        } else {
            try {
                HttpResponse<JsonNode> response = Unirest.get("https://igor-zachetly-ping-uin.p.mashape.com/pinguin.php?address=" + URLEncoder.encode(ip))
                        .header("X-Mashape-Key", "sHb3a6jczqmshcYqUEwQq3ZZR3BVp18NqaAjsnIYFvVNHMqvCb")
                        .asJson();
                if (response.getBody().getObject().get("result").equals(false)) {
                    Resource.sendMessage(chat, "Invalid hostname!");
                } else {
                    Object timeLeftObject = response.getBody().getObject().get("time");
                    if (timeLeftObject != null) {
                        String timeLeft = timeLeftObject.toString();
                        if (!timeLeft.isEmpty()) {
                            Resource.sendMessage(chat, ip + " - Response took " + timeLeft + "ms");
                            return;
                        }
                    }

                    Resource.sendMessage(chat, ip + " - No response received!");
                }
            } catch (UnirestException e) {
                Resource.sendMessage(chat, "Error: " + Utils.upload(ExceptionUtils.getStackTrace(e)));
            }
        }
    }

    @Command(name = "quote")
    public static void cmdQuote(ChatMessage chat, String category) throws SkypeException {
        try {
            HttpResponse<JsonNode> response = Unirest.post("https://andruxnet-random-famous-quotes.p.mashape.com/cat=" + category).header("X-Mashape-Key", "rIizXnIZ7Umsh3o3sfCLfL86lZY2p1bda69jsnAqK1Sc6C5CV1").header("Content-Type", "application/x-www-form-urlencoded").asJson();
            Resource.sendMessage(chat, "\"" + response.getBody().getObject().get("quote") + "\" - " + response.getBody().getObject().get("author"));
        } catch (UnirestException | JSONException ignored) {
        }
    }

    @Command(name = "random")
    public static void cmdRandom(ChatMessage chat, int number1, int number2) throws SkypeException {
        int high = Math.max(number1, number2);
        int low = Math.min(number1, number2);

        if (high == low) {
            Resource.sendMessage(chat, "The numbers cannot be the same!");
            return;
        }

        Resource.sendMessage(chat, String.valueOf(ThreadLocalRandom.current().nextInt(high - low) + low));
    }

    @Command(name = "whatwouldrandomsay")
    public static void cmdRandomSay(ChatMessage chat, @Optional String lol) throws SkypeException {

        Map<String, MessageStatistic> messageStatisticMap = StatisticsManager.instance().statistics();
        String username = (String) messageStatisticMap.keySet().toArray()[ThreadLocalRandom.current().nextInt(0, messageStatisticMap.size())];
        MessageStatistic statistic = messageStatisticMap.get(username);
        String message = statistic.randomMessage();

        while (message.startsWith("@")) {
            message = statistic.randomMessage();
        }

        Resource.sendMessage(chat, username + " says: \" " + message + " \" ");
    }

    @Command(name = "rant")
    public static void cmdRant(ChatMessage chat,
            @Optional
            final String question) throws SkypeException {
        if (ranting) {
            rantThread.stop();
            ranting = false;
            Resource.sendMessage(chat, "Ranting stopped!");
            return;
        }

        if (question == null || question.equals("")) {
            Resource.sendMessage(chat, "Enter a question!");
            return;
        }

        try {
            cleverBot = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession();
            jabberWacky = new ChatterBotFactory().create(ChatterBotType.JABBERWACKY).createSession();
        } catch (Exception ignored) {
        }

        if (cleverBot == null || jabberWacky == null) {
            Resource.sendMessage(chat, "One of the bots died!");
            return;
        }

        Resource.sendMessage(chat, "Ranting started...");

        ranting = true;

        rantThread = new Thread() {

            String cleverThink = null;

            @Override
            public void run() {
                while (true) {
                    try {
                        if (cleverThink == null) {
                            cleverThink = question;
                        }

                        String cleverBotResponse = cleverBot.think(cleverThink);

                        assert (cleverBotResponse != null && !cleverBotResponse.trim().equals(""));

                        Resource.sendMessage("[CB] " + cleverBotResponse);
                        Thread.sleep(500);

                        String jabberWackyResponse = jabberWacky.think(cleverBotResponse);
                        cleverThink = jabberWackyResponse;

                        assert (jabberWackyResponse != null && !jabberWackyResponse.trim().equals(""));
                        Resource.sendMessage("[JW] " + jabberWackyResponse);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        Resource.sendMessage(chat, "A bot got banned :( (" + Utils.upload(ExceptionUtils.getStackTrace(e)) + ")");
                        ranting = false;
                        this.stop();
                    }
                }
            }
        };

        rantThread.start();
    }

    @Command(name = "restart", admin = true)
    public static void cmdRestart(ChatMessage chat) {
        Utils.restartBot();
    }

    @Command(name = "sql", cooldown = 30)
    public static void cmdSQL(ChatMessage chat, String query) throws SQLException, SkypeException {
        if (SkypeBot.getInstance().getDatabase() == null) {
            Resource.sendMessage(chat, "Connection is down!");
            return;
        }

        if (query.toUpperCase().contains("DROP DATABASE") || query.toUpperCase().contains("CREATE DATABASE") || query.toUpperCase().contains("USE") || query.toUpperCase().contains("CREATE PROCEDURE")) {
            Resource.sendMessage(chat, "Do not touch the databases!");
            return;
        }

        if (query.toUpperCase().contains("INFORMATION_SCHEMA")) {
            Resource.sendMessage(chat, "Not that fast!");
            return;
        }

        Statement stmt = null;

        try {
            stmt = SkypeBot.getInstance().getDatabase().createStatement();
            if (query.toLowerCase().startsWith("select") || query.toLowerCase().startsWith("show")) {
                ResultSet result = stmt.executeQuery(query);
                String parsed = Utils.parseResult(result);
                parsed = query + "\n\n" + parsed;
                Resource.sendMessage(chat, "SQL Query Successful: " + Utils.upload(parsed));
            } else {
                stmt.execute(query);
                Resource.sendMessage(chat, "SQL Query Successful!");
            }
        } catch (SQLException e) {
            String message = e.getMessage();
            message = message.replace("You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near", "");
            Resource.sendMessage(chat, "Error executing SQL: " + message);
        } catch (Exception e) {
            Resource.sendMessage(chat, "Error: " + Utils.upload(ExceptionUtils.getStackTrace(e)));
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Command(name = "topkek")
    public static void cmdTopKek(ChatMessage chat) throws SkypeException {
        Resource.sendMessage(chat, "https://topkek.mazenmc.io/ Gotta be safe while keking!");
    }

    @Command(name = "define")
    public static void cmddefine(ChatMessage chat, String word) throws Exception {
        HttpResponse<String> response = Unirest.get("https://mashape-community-urban-dictionary.p.mashape.com/define?term=" + word.replace(" ", "+"))
                .header("X-Mashape-Key", Resource.KEY_URBAND)
                .header("Accept", "text/plain")
                .asString();
        JSONObject object = new JSONObject(response.getBody());

        if (object.getJSONArray("list").length() == 0) {
            Resource.sendMessage(chat, "No definition found for " + word + "!");
            return;
        }

        JSONObject definition = object.getJSONArray("list").getJSONObject(0);

        Resource.sendMessage(chat, "Definition of " + word + ": " + definition.getString("definition"));
        Resource.sendMessage(chat, definition.getString("example"));
        Resource.sendMessage(chat, definition.getInt("thumbs_up") + " thumbs ups, " + definition.getInt("thumbs_down") + " thumbs down");
        Resource.sendMessage(chat, "Definition by " + definition.getString("author"));
    }

    @Command(name = "^https?://(?:[a-z\\-]+\\.)+[a-z]{2,6}(?:/[^/#?]+)+\\.(?:jpg|gif|png)$", command = false, exact = false)
    public static void pornDetect(ChatMessage chat) throws Exception {
        String message = chat.getContent();
        String link = null;

        for (String part : message.split(" ")) {
            if (part.startsWith("http") || part.startsWith("https")) {
                link = part;
                break;
            }
        }

        if (link == null)
            return;

        try {
            HttpResponse<JsonNode> response = Unirest.get("https://sphirelabs-advanced-porn-nudity-and-adult-content-detection.p.mashape.com/v1/get/index.php?url=" + link)
                    .header("X-Mashape-Key", Resource.KEY_URBAND)
                    .header("Accept", "application/json")
                    .asJson();

            if ("True".equals(response.getBody().getObject().getString("Is Porn"))) {
                Resource.sendMessage(chat.getSenderDisplayName() + "'s image is probably porn");
            }
        } catch (Exception ignored) {
        }
    }

    @Command(name = "dreamincode", alias = {"whatwouldmazensay"})
    public static void cmddreamincode(ChatMessage chat) throws SkypeException {
        String[] options = new String[]{"No, I'm not interested in having a girlfriend I find it a tremendous waste of time.",
                "Hi, my name is Santiago Gonzalez and I'm 14 and I like to program.",
                "I'm fluent in a dozen different programming languages.",
                "Thousands of people have downloaded my apps for the Mac, iPhone, and iPad.",
                "I will be 16 when I graduate college and 17 when I finish my masters.",
                "I really like learning, I find it as essential as eating.",
                "Dr. Bakos: I often have this disease which I call long line-itus.",
                "Dr. Bakos: Are you eager enough just to write down a slump of code, or is the code itself a artistic medium?",
                "Beautiful code is short and concise.",
                "Sometimes when I go to sleep I'm stuck with that annoying bug I cannot fix, and in my dreams I see myself programming. " +
                        "When I wake up I have the solution!",
                        "One of the main reasons I started developing apps was to help people what they want to do like decorate a christmas tree.",
                        "I really like to crochet.",
        "I made a good website http://slgonzalez.com/"};
        int chosen = ThreadLocalRandom.current().nextInt(options.length);
        Resource.sendMessage(chat, options[chosen]);
    }

    @Command(name = "relevantxkcd", alias = {"xkcd"})
    public static void cmdrelevantxkcd(ChatMessage chat, @Optional String name) {
        if (name == null) {
            try {
                HttpResponse<JsonNode> response = Unirest.get("http://xkcd.com/info.0.json")
                        .asJson();
                int urlnumber = ThreadLocalRandom.current().nextInt((Integer) response.getBody().getObject().get("num")) + 1;
                HttpResponse<JsonNode> xkcd = Unirest.get("http://xkcd.com/" + urlnumber + "/info.0.json")
                        .asJson();
                String transcript = xkcd.getBody().getObject().get("transcript").toString();
                String image = xkcd.getBody().getObject().get("img").toString();
                Resource.sendMessage(chat, "Image - " + image);
                Resource.sendMessage(chat, "Transcript - " + Utils.upload(transcript));
            } catch (Exception e) {
                Resource.sendMessage(chat, "Error: " + Utils.upload(ExceptionUtils.getStackTrace(e)));
            }
        } else {
            try {
                String key = "AIzaSyDulfiY_C1PK19PinCLmagTeMMeVlmhimI";
                String cx = "012652707207066138651:Azudjtuwe28q";

                HttpRequestInitializer httpRequestInitializer = request -> {
                };
                JsonFactory jsonFactory = new JacksonFactory();
                Customsearch custom = new Customsearch(new NetHttpTransport(), jsonFactory, httpRequestInitializer);
                Customsearch.Cse.List list = custom.cse().list(name);
                list.setCx(cx);
                list.setKey(key);

                Search result = list.execute();

                Customsearch.Cse.List listResult = (Customsearch.Cse.List) result.getItems();
                if (listResult.isEmpty()) {
                    Resource.sendMessage("No results found");
                } else {
                    Result first = (Result) listResult.get(0);
                    Resource.sendMessage(first.getFormattedUrl());
                }
            } catch (IOException e) {
                Resource.sendMessage("Error: " + Utils.upload(ExceptionUtils.getStackTrace(e)));
            }
        }
    }

    @Command(name = "roflcopter", alias = {"rofl"})
    public static void cmdRofl(ChatMessage chat) throws SkypeException {
        Resource.sendMessage(chat, "ROFL all day long! http://goo.gl/pCIqXv");
    }

    @Command(name = "restoretopic", admin = true)
    public static void cmdRestoreTopic(ChatMessage chatMessage) throws SkypeException {
        Resource.sendMessage("/topic Mazen's Skype Chat :: Student freebies: https://education.github.com | https://www.jetbrains.com/student/ | http://products.office.com/en-us/student/office-in-education");
    }

    @Command(name = "has changed the conversation picture.", command = false)
    public static void cmdConvoPictureChange(ChatMessage chatMessage) throws SkypeException {
        Resource.sendMessage("/me would love to remove " + chatMessage.getSenderId() + "'s ass right now");
    }

    @Command(name = "lenny")
    public static void cmdLenny(ChatMessage chat) throws SkypeException {
        Resource.sendMessage(chat, "( ͡° ͜ʖ ͡°)");
    }
    
    @Command(name = "idk")
    public static void cmdIdk(ChatMessage chat) throws SkypeException {
        Resource.sendMessage(chat, "¯\\_(ツ)_/¯");
    }
    
    @Command(name = "wat")
    public static void cmdWat(ChatMessage chat) throws SkypeException {
        Resource.sendMessage(chat, "http://waitw.at O.o");
    }
    
    @Command(name = "confirmed")
   public static void cmdConfirmed(ChatMessage chat, String question) {
        String[] options = new String[] { "%s Confirmed", "%s won't happen!" ,"%s will happen some day",
            "%s will happen some day", "Just wait and see"};
        int chosen = ThreadLocalRandom.current().nextInt(options.length);

        Resource.sendMessage(chat, String.format(options[chosen], question));
    }
    
    @Command(name = "phallusexercise", alias = {"whatwouldjustissay"})
    public static void cmdphallusexercise(ChatMessage message, @Optional String special) {
        String[] options = new String[] {
        		"Guys, Can confirm. Penis exersizes DO work.",
        		"It's only been a week and there is a noticable difference.",
        		"Excersizing my phallus.",
        		"Any lady of mine is gunna feel real lucky.",
        		"If only you guys were as passionate about giving your women pleasure.",
        		"FYI, I have plenty of ladies.",
        		"Any women will apreciate your effort. Knowing you care.",
        		"I'm good at what I do. ;)",
        		"I expect lots of cake!",
        		"We could be sex buddies!!! Makin porn together!!",
        		"All our sex toys are made from 100% ultra-premium custom forumulated silicone; garenteed to last a lifetime.",
        		"Easy To Clean, Eco-Friendly, Hypoallergenic, Hygienic, Boilable, Bleachable and Dishwasher Safe. ;D",
        		"Just in case you ever wanted to wash your vibrator with your eating utensils. I know I do."};
        int chosen = ThreadLocalRandom.current().nextInt(options.length);

        Resource.sendMessage(message, options[chosen]);
    }

    @Command(name = "poll", alias = {"strawpoll"})
    public static void cmdPoll(ChatMessage message, String arguments) throws SkypeException, UnirestException, JSONException {
        String[] args = arguments.split(":");

        if (args.length < 2) {
            Resource.sendMessage(message, "Give the poll options!");
            return;
        }

        String[] options = args[1].split(",");
        JSONObject object = new JSONObject();

        if (options.length < 2) {
            Resource.sendMessage(message, "I kinda need some options for this poll here...");
        }

        object.put("title", args[0]);
        object.put("options", new JSONArray(options));

        JSONObject response = Unirest.post("http://strawpoll.me/api/v2/polls")
                .header("Content-Type", "application/json")
                .body(object.toString())
                .asJson()
                .getBody()
                .getObject();

        if (!response.has("id")) {
            Resource.sendMessage(message, "Invalid request!");
            return;
        }
        int id = response.getInt("id");
        Resource.sendMessage(message, "Poll created: https://strawpoll.me/" + id);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    JSONObject results = Unirest.get("https://strawpoll.me/api/v2/polls/" + id)
                            .asJson()
                            .getBody()
                            .getObject();

                    Resource.sendMessage(message, "-----------------------------------");
                    Resource.sendMessage(message, "The poll results are in!");
                    Resource.sendMessage(message, results.getString("title"));

                    int index = 0;
                    JSONArray options = results.getJSONArray("options");
                    JSONArray votes = results.getJSONArray("votes");

                    while (!options.isNull(index)) {
                        Resource.sendMessage(message, options.getString(index) + " - Votes: " + votes.getInt(index));
                        index++;
                    }

                    Resource.sendMessage(message, "-----------------------------------");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 300000);

    }
    
    @Command(name = "(?i)ayy", exact = false, command = false)
    public static void ayy(ChatMessage message, @Optional String ayy) throws SkypeException {
        if (message.getContent().contains("lmao")) {
            Resource.sendMessage("ayy lmao");
            return;
        }
        
        Resource.sendMessage("lmao");
    }
    
    @Command(name = "(?i)alien", exact = false, command = false)
    public static void ayyLmao(ChatMessage message, @Optional String lmao) {
        Resource.sendMessage("ayy lmao");
    }
}
