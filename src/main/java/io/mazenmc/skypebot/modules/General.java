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
import com.samczsun.skype4j.chat.messages.ReceivedMessage;
import com.samczsun.skype4j.exceptions.ChatNotFoundException;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.NoSuchContactException;
import com.samczsun.skype4j.user.User;
import io.mazenmc.skypebot.SkypeBot;
import io.mazenmc.skypebot.engine.bot.*;
import io.mazenmc.skypebot.engine.bot.Optional;
import io.mazenmc.skypebot.stat.Message;
import io.mazenmc.skypebot.stat.MessageStatistic;
import io.mazenmc.skypebot.stat.StatisticsManager;
import io.mazenmc.skypebot.utils.Resource;
import io.mazenmc.skypebot.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class General implements Module {
    private static ChatterBotSession cleverBot;
    private static ChatterBotSession jabberWacky;
    private static Thread rantThread;
    private static boolean ranting = false;

    @Command(name = "exclude", admin = true)
    public static void cmdExclude(ReceivedMessage chat, int days, String person) {
        MessageStatistic statistic = StatisticsManager.instance().statistics().get(person);

        if (statistic == null) {
            Resource.sendMessage(chat, "Couldn't find statistic by " + person);
            return;
        }

        Date date = new Date(System.currentTimeMillis() + ((long)days * 86400000L));

        statistic.addMessage(new Message("Gone until " + date.toString(), date.getTime()));
        Resource.sendMessage(chat, "Successfully excluded " + person + " from chat cleaner until " + date.toString());
    }

    @Command(name = "kick", admin = true)
    public static void cmdReset(ReceivedMessage chat, String person) {
        MessageStatistic statistic = StatisticsManager.instance().statistics().get(person);

        if (statistic == null) {
            Resource.sendMessage(chat, "Couldn't find statistic by " + person);
            return;
        }

        StatisticsManager.instance().removeStat(person);
        Resource.sendMessage("/kick " + person);
        Resource.sendMessage("Kicked " + person + " and removed their stats");
    }

    @Command(name = "8ball")
    public static void cmd8Ball(ReceivedMessage chat, @Optional
            String question) {
        String[] options = new String[]{"It is certain", "It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes", "Reply hazy try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again", "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};
        int chosen = ThreadLocalRandom.current().nextInt(options.length);
        Resource.sendMessage(chat, options[chosen]);
    }

    @Command(name = "flickr.com/photos/stuntguy3000", exact = false, command = false)
    public static void cmdStuntguyFlickr(ReceivedMessage chat) throws Exception {
        if (chat.getSender().getUsername().equalsIgnoreCase("stuntguy3000")) {
            Resource.sendMessage("Nobody likes your photos, Luke.");
        }
    }

    @Command(name = "about")
    public static void cmdAbout(ReceivedMessage chat) {
        Resource.sendMessage(chat, "Originally created by Vilsol, reincarnated and improved by MazenMC and stuntguy3000");
        Resource.sendMessage(chat, "Version: " + Resource.VERSION);
    }

    @Command(name = "choice")
    public static void choice(ReceivedMessage chat, String message) {
        String[] choices = message.trim().split(",");

        if (choices.length == 1) {
            Resource.sendMessage(chat, "Give me choices!");
            return;
        }

        Resource.sendMessage(chat, "I say " + choices[ThreadLocalRandom.current().nextInt(choices.length)].trim());
    }

    @Command(name = "hof", alias = {"halloffame", "hallofame"})
    public static void cmdHof(ReceivedMessage chat) {
        Resource.sendMessage(chat, "https://gist.github.com/mkotb/3befd5bf719496278052");
    }

    @Command(name = "c")
    public static void cmdC(ReceivedMessage chat, String question) throws Exception {
        Resource.sendMessage(chat, SkypeBot.getInstance().askQuestion(question));
    }

    @Command(name = "doc")
    public static void cmdDoc(ReceivedMessage chat) {
        Resource.sendMessage(chat, "https://docs.google.com/document/d/1LoTYCauVyEiiLZ5Klw3UB8rbEtkzNn7VLmgm87Fzyy0/edit#");
    }

    @Command(name = "fish go moo", exact = false, command = false)
    public static void cmdFishGoMoo(ReceivedMessage chat) throws Exception {
        Resource.sendMessage("/me notes that " + Utils.getDisplayName(chat.getSender()) + " is truly enlightened.");
    }

    @Command(name = "thank mr bot", exact = false, command = false)
    public static void cmdThankMrBot(ReceivedMessage chat) throws Exception {
        Resource.sendMessage("may good cpus and dedotated wams come to you");
    }

    @Command(name = "git", alias = {"repo", "repository", "source"})
    public static void cmdGit(ReceivedMessage chat) {
        Resource.sendMessage(chat, "Git Repository: https://github.com/MazenMC/SkypeBot");
    }

    @Command(name = "help", alias = {"commands"})
    public static void cmdHelp(ReceivedMessage chat) {
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
    public static void cmdLmgtfy(ReceivedMessage chat, String question) throws Exception {
        String returnString = "http://lmgtfy.com/?q=";
        Resource.sendMessage(chat, returnString + URLEncoder.encode(question));
    }

    @Command(name = "gh", alias = {"hangouts", "ghangouts", "googleh"})
    public static void cmdGh(ReceivedMessage chat) {
        Resource.sendMessage(chat, "https://imgur.com/a/ISm8W");
    }

    @Command(name = "loveme")
    public static void cmdAdd(ReceivedMessage chat) {
        try {
            String[] possibleCards = {"https://i.imgur.com/4WAW8vM.jpg",
                    "https://i.imgur.com/8zMPZYa.png", "https://i.imgur.com/w4JdQFP.jpg",
                    "https://i.imgur.com/nymmBgA.jpg", "https://i.imgur.com/J0E0SHS.jpg",
                    "https://i.imgur.com/G7nAIgU.jpg", "https://i.imgur.com/qsfSjK0.jpg",
                    "https://i.imgur.com/V1D4Ku8.jpg", "https://i.imgur.com/jO5baBM.png",
                    "https://i.imgur.com/LXiQo1g.png", "https://i.imgur.com/Hj9VDIx.jpg",
                    "https://i.imgur.com/88F1rR6.jpg", "https://i.imgur.com/zM3wXsv.jpg",
                    "https://i.imgur.com/kUqruhJ.jpg", "https://i.imgur.com/wf7t0ad.jpg",
                    "https://i.imgur.com/WBZI9yS.jpg"};

            chat.getSender().getContact().sendRequest("I love you");
            chat.getSender().getContact().getPrivateConversation().sendMessage("You can be my special friend <3");
            chat.getSender().getContact().getPrivateConversation().sendMessage(possibleCards[ThreadLocalRandom.current().nextInt(possibleCards.length)]);
            Resource.sendMessage(chat, "I'll love you...");
        } catch (ConnectionException | ChatNotFoundException | NoSuchContactException ex) {
            Resource.sendMessage(chat, "Unable to love you :c Maybe try to open up your privacy settings?");
        }
    }

    @Command(name = "stats")
    public static void cmdStats(ReceivedMessage chat, @Optional String person) throws Exception {

        DecimalFormat format = new DecimalFormat("##.#");

        if (person != null) {
            MessageStatistic stat = StatisticsManager.instance().statistics().get(person);

            if (stat == null) {
                Resource.sendMessage("No found statistic for " + person + "!");
                return;
            }

            User user = Utils.getUser(person);
            String name = Utils.getDisplayName(user);

            String[] toSend = new String[13];
            Message first = Utils.firstSpoken(stat);
            Message last = Utils.lastSpoken(stat);

            toSend[0] = "------ " + name + "'s statistics ------";
            toSend[1] = "Message count: " + stat.messageAmount();
            toSend[2] = "Word count: " + stat.wordCount();
            toSend[3] = "Average words per message: " + format.format(stat.averageWords());
            toSend[4] = "Command count: " + stat.commandCount();
            if (name.equals("troll.dude.3") || name.equals("julian.ayy")) {
                toSend[5] = "Random message: LOOOOOOLL LMAO RICE ASIANS LMFAO ROFL 4111RRRRR AIIIR OMG LOOOl";
            } else {
                toSend[5] = "Random message: " + stat.randomMessage().contents();
            }
            toSend[6] = "Most common word: " + stat.mostCommonWord();
            toSend[7] = "First message sent at " + new Date(first.time()).toString();
            toSend[8] = "First message: " + first.contents();
            toSend[9] = "Last message sent at " + new Date(last.time()).toString();
            toSend[10] = "Last message: " + last.contents();
            toSend[11] = "Percent of Messages which were commands: " + format.format(stat.commandPercent()) + "%";
            toSend[12] = "---------------------------------------";

            Resource.sendMessages(toSend);
            return;
        }

        List<String> toSend = new ArrayList<>();
        List<MessageStatistic> messages = new ArrayList<>(StatisticsManager.instance()
                .statistics()
                .values());

        Collections.sort(messages, (a, b) -> b.letterCount() - a.letterCount());

        int letterTotal = messages.stream()
                .mapToInt(MessageStatistic::letterCount)
                .sum();
        double total = messages.stream()
                .mapToInt(MessageStatistic::messageAmount)
                .sum();

        toSend.add("---------------------------------------");

        IntStream.range(0, 5).forEach((i) -> {
            if (messages.size() <= i)
                return;

            MessageStatistic stat = messages.get(i);
            double percentage = ((double)stat.letterCount() / letterTotal) * 100;

            toSend.add(StatisticsManager.instance().ownerFor(stat) + ": " +
                    stat.letterCount() + " - " + format.format(percentage) + "%");
        });

        toSend.add("---------------------------------------");

        toSend.add(letterTotal + " total characters sent in this chat");
        toSend.add((int) total + " total messages sent in this chat (which has been logged by the bot)");
        toSend.add(messages.size() + " members sent messages which were acknowledged by the bot");

        List<List<String>> raw = messages.stream()
                .map(MessageStatistic::messages)
                .map((l) -> l.stream().map(Message::contents).collect(Collectors.toList()))
                .collect(Collectors.toList());
        List<String> msgs = new ArrayList<>((int) total);

        raw.forEach(msgs::addAll);

        double commands = msgs.stream()
                .filter((s) -> s.startsWith("@"))
                .count();
        long characters = msgs.stream()
                .mapToLong(String::length)
                .sum();
        List<String> words = new ArrayList<>();

        msgs.stream()
                .map((s) -> s.split(" "))
                .forEach((s) -> words.addAll(Arrays.asList(s)));

        words.removeIf((s) -> s.equals("") || s.equals(" "));

        String mostCommonWord = words.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet().stream()
                .sorted((e, e1) -> (int) (e1.getValue() - e.getValue()))
                .findFirst().get()
                .getKey();
        double wordPerMessage = words.size() / msgs.size();

        toSend.add(Math.round(((commands / total) * 100)) + "% of those messages were commands");
        toSend.add(words.size() + " words were sent");
        toSend.add(characters + " characters were sent");
        toSend.add((int) commands + " commands were sent");
        toSend.add("---------------------------------------");
        toSend.add("Average words per message: " + format.format(wordPerMessage));
        toSend.add("Most common word: " + mostCommonWord);

        Resource.sendMessages(toSend.toArray(new String[toSend.size()]));
    }

    @Command(name = "wordstats")
    public static void cmdWordStats(ReceivedMessage chat, @Optional String lol) {
        List<MessageStatistic> messages = new ArrayList<>(StatisticsManager.instance()
                .statistics()
                .values());
        List<List<String>> raw = messages.stream()
                .map(MessageStatistic::messages)
                .map((l) -> l.stream().map(Message::contents).collect(Collectors.toList()))
                .collect(Collectors.toList());
        List<String> msgs = new ArrayList<>((int) messages.stream()
                .mapToInt(MessageStatistic::messageAmount)
                .sum());

        raw.forEach(msgs::addAll);
        msgs.replaceAll(String::toLowerCase);
        List<String> words = new ArrayList<>();

        msgs.stream()
                .map((s) -> s.split(" "))
                .forEach((s) -> words.addAll(Arrays.asList(s)));

        words.removeIf((s) -> s.equals("") || s.equals(" "));

        List<Map.Entry<String, Long>> commonWords = words.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet().stream()
                .sorted((e, e1) -> (int) (e1.getValue() - e.getValue()))
                .collect(Collectors.toList());
        String[] toSend = new String[12];

        toSend[0] = "---------------------------------------";

        IntStream.range(0, 10).forEach((i) -> {
            Map.Entry<String, Long> entry = commonWords.get(i);

            toSend[i + 1] = (i + 1) + ". " + entry.getKey() + " with " + entry.getValue() + " occurrences";
        });

        toSend[11] = "---------------------------------------";
        Resource.sendMessages(toSend);
    }

    @Command(name = "kicklist")
    public static void kickList(ReceivedMessage chat) {
        long timestamp = System.currentTimeMillis();
        Collection<MessageStatistic> stats = StatisticsManager.instance().statistics().values();
        List<Map.Entry<MessageStatistic, Long>> sorted = stats.stream()
                .filter((person) -> !person.messages().isEmpty())
                .map((person) -> new HashMap.SimpleEntry<>(person, timestamp - person.messages().stream()
                        .sorted((m1, m2) -> (int) (m2.time() - m1.time())).findFirst().get().time()))
                .sorted((person, person1) -> (int) (person.getValue() - person1.getValue()))
                .collect(Collectors.toList());
        String[] toSend = new String[12];

        toSend[0] = "---------------------------------------";

        IntStream.range(0, 10).forEach((i) -> {
            Map.Entry<MessageStatistic, Long> entry = sorted.get(i);
            long days = TimeUnit.MILLISECONDS.toDays(entry.getValue());

            toSend[i + 1] = (i + 1) + ". " + entry.getKey().name() + " to be kicked in " + days + " days";
        });

        toSend[11] = "---------------------------------------";
        Resource.sendMessages(toSend);
    }

    @Command(name = "md5")
    public static void cmdMd5(ReceivedMessage chat) {
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
    public static void cmdPing(ReceivedMessage chat, @Optional
            final String ip) throws JSONException, Exception {
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
                Resource.sendMessage(chat, "Error...");
            }
        }
    }

    @Command(name = "quote")
    public static void cmdQuote(ReceivedMessage chat, String category) throws Exception {
        try {
            HttpResponse<JsonNode> response = Unirest.post("https://andruxnet-random-famous-quotes.p.mashape.com/cat=" + category).header("X-Mashape-Key", "rIizXnIZ7Umsh3o3sfCLfL86lZY2p1bda69jsnAqK1Sc6C5CV1").header("Content-Type", "application/x-www-form-urlencoded").asJson();
            Resource.sendMessage(chat, "\"" + response.getBody().getObject().get("quote") + "\" - " + response.getBody().getObject().get("author"));
        } catch (UnirestException | JSONException ignored) {
        }
    }

    @Command(name = "random")
    public static void cmdRandom(ReceivedMessage chat, int number1, int number2) throws Exception {
        int high = Math.max(number1, number2);
        int low = Math.min(number1, number2);

        if (high == low) {
            Resource.sendMessage(chat, "The numbers cannot be the same!");
            return;
        }

        Resource.sendMessage(chat, String.valueOf(ThreadLocalRandom.current().nextInt(high - low) + low));
    }

    @Command(name = "whatwouldrandomsay")
    public static void cmdRandomSay(ReceivedMessage chat) throws Exception {
        Map<String, MessageStatistic> messageStatisticMap = StatisticsManager.instance().statistics();
        String username = (String) messageStatisticMap.keySet().toArray()[ThreadLocalRandom.current().nextInt(0, messageStatisticMap.size())];
        randomSay(chat, username);
    }

    @Command(name = "whatwouldselectsay")
    public static void cmdSelectSay(ReceivedMessage chat, String username) throws Exception {
        MessageStatistic stat = StatisticsManager.instance().statistics().get(username);

        if (stat == null) {
            Resource.sendMessage("No found statistic for " + username + "!");
            return;
        }

        randomSay(chat, username);
    }

    private static void randomSay(ReceivedMessage chat, String username) throws Exception {
        Map<String, MessageStatistic> messageStatisticMap = StatisticsManager.instance().statistics();
        MessageStatistic statistic = messageStatisticMap.get(username);
        Message message = statistic.randomMessage();

        while (message.contents().startsWith("@")) {
            message = statistic.randomMessage();
        }

        if (message.contents().equals("<never sent message>")) {
            return;
        }

        Resource.sendMessage(chat, username + " says: \" " + message.contents() + " \" at " + new Date(message.time()).toString());
    }

    @Command(name = "whatwouldrandomquestion")
    public static void cmdRandomQuestion(ReceivedMessage chat) throws Exception {
        Map<String, MessageStatistic> messageStatisticMap = StatisticsManager.instance().statistics();
        MessageStatistic[] statistics = messageStatisticMap.values().toArray(new MessageStatistic[messageStatisticMap.size()]);
        MessageStatistic stat = statistics[ThreadLocalRandom.current().nextInt(statistics.length)];
        Message msg = stat.randomMessage();
        String message = msg.contents();

        while (message.startsWith("@") || (!message.endsWith("?") && !message.toLowerCase().startsWith("why"))) {
            stat = statistics[ThreadLocalRandom.current().nextInt(statistics.length)];
            msg = stat.randomMessage();
            message = msg.contents();
        }

        Resource.sendMessage(chat, stat.name() + " said: \" " + msg.contents() + " \" at " + new Date(msg.time()).toString());
    }

    @Command(name = "rant")
    public static void cmdRant(ReceivedMessage chat,
            @Optional
            final String question) throws Exception {
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
            jabberWacky = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession();
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

                        while (cleverBotResponse.startsWith("\n") ||
                                cleverBotResponse.contains("app")) {
                            cleverBotResponse = cleverBot.think(cleverThink);
                        }

                        Resource.sendMessage("[CB] " + cleverBotResponse);
                        Thread.sleep(500);

                        String jabberWackyResponse = jabberWacky.think(cleverBotResponse);

                        while (jabberWackyResponse.startsWith("\n") ||
                                jabberWackyResponse.contains("app")) {
                            jabberWackyResponse = jabberWacky.think(cleverBotResponse);
                        }

                        cleverThink = jabberWackyResponse;

                        assert (!jabberWackyResponse.trim().equals(""));
                        Resource.sendMessage("[JW] " + jabberWackyResponse);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        Resource.sendMessage(chat, "A bot got banned :( (");
                        ranting = false;
                        this.stop();
                    }
                }
            }
        };

        rantThread.start();
    }

    @Command(name = "restart", admin = true)
    public static void cmdRestart(ReceivedMessage chat) {
        Utils.restartBot();
    }

    @Command(name = "sql", cooldown = 30)
    public static void cmdSQL(ReceivedMessage chat, String query) throws SQLException, Exception {
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
            Resource.sendMessage(chat, "Error...");
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Command(name = "topkek")
    public static void cmdTopKek(ReceivedMessage chat) throws Exception {
        Resource.sendMessage(chat, "https://topkek.mazenmc.io/ Gotta be safe while keking!");
    }

    @Command(name = "define")
    public static void cmddefine(ReceivedMessage chat, String word) throws Exception {
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
    public static void pornDetect(ReceivedMessage chat) throws Exception {
        String message = chat.getContent().asPlaintext();
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
                Resource.sendMessage(Utils.getDisplayName(chat.getSender()) + "'s image is probably porn");
            }
        } catch (Exception ignored) {
        }
    }

    @Command(name = "dreamincode", alias = {"whatwouldmazensay"})
    public static void cmddreamincode(ReceivedMessage chat) throws Exception {
        String[] options = new String[] {
            "No, I'm not interested in having a girlfriend I find it a tremendous waste of time.",
            "Hi, my name is Santiago Gonzalez and I'm 14 and I like to program.",
            "I'm fluent in a dozen different programming languages.",
            "Thousands of people have downloaded my apps for the Mac, iPhone, and iPad.",
            "I will be 16 when I graduate college and 17 when I finish my masters.",
            "I really like learning, I find it as essential as eating.",
            "Dr. Bakos: I often have this disease which I call long line-itus.",
            "Dr. Bakos: Are you eager enough just to write down a slump of code, or is the code itself a artistic medium?",
            "Beautiful code is short and concise.",
            "When you're around a certain race enough, you tend to become like them physically and mentally; soon enough I'll be driving terribly and doing math competitions",
            "Sometimes when I go to sleep I'm stuck with that annoying bug I cannot fix, and in my dreams I see myself programming. \nWhen I wake up I have the solution!",
            "One of the main reasons I started developing apps was to help people what they want to do like decorate a christmas tree.",
            "I really like to crochet.",
            "I made a good website http://slgonzalez.com/",
            "If that was my sister I'd eat her.",
            "https://s.mzn.pw/08bG9Ti.png",
            "ISIS more like waswas",
            "eat less food habibi @aaomidi",
            "Are aero bars forming in my ears?",
            "yo I was walking down the halls and into my next class playing \"In Da Club\" by 50 cent on full volume with my laptop on one hand",
            "Less halawa more carrot @aaomidi",
            "I live in Vancouver, which is practically Asia. Soon enough British Columbia will be renamed to Asian Columbia"
        };
        int chosen = ThreadLocalRandom.current().nextInt(options.length);
        Resource.sendMessage(chat, options[chosen]);
    }

    @Command(name = "relevantxkcd", alias = {"xkcd"})
    public static void cmdrelevantxkcd(ReceivedMessage chat, @Optional String name) {
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
                Resource.sendMessage(chat, "Error...");
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
                Resource.sendMessage("Error...");
            }
        }
    }

    @Command(name = "roflcopter", alias = {"rofl"})
    public static void cmdRofl(ReceivedMessage chat) throws Exception {
        Resource.sendMessage(chat, "ROFL all day long! http://goo.gl/pCIqXv");
    }

    @Command(name = "restoretopic")
    public static void cmdRestoreTopic(ReceivedMessage chat) throws Exception {
        Resource.sendMessage("/topic Mazen's Skype Chat");
    }

    @Command(name = "has changed the conversation picture.", command = false)
    public static void cmdConvoPictureChange(ReceivedMessage chat) throws Exception {
        Resource.sendMessage("/me would love to remove " + chat.getSender().getUsername() + "'s ass right now");
    }

    @Command(name = "basoon")
    public static void cmdBasoon(ReceivedMessage chat) throws Exception {
        Resource.sendMessage(chat, "Blows Air Strongly Out Of Nose");
    }

    @Command(name = "(?i)savage", command = false, alias = "(?i)brutal")
    public static void savageDettector(ReceivedMessage chat) throws Exception {
        String[] options = new String[] {
                "https://i.imgur.com/t137FTZ.jpg",
                "https://i.imgur.com/jLLRs2j.jpg",
                "https://i.imgur.com/sES5tg1.png",
                "https://i.imgur.com/kHQUtvf.jpg",
                "https://i.imgur.com/VaHSXD4.png",
                "https://i.imgur.com/sDdHFWp.png",
                "https://i.imgur.com/KOCnBca.gif"
        };
        int chosen = ThreadLocalRandom.current().nextInt(options.length);
        Resource.sendMessage(options[chosen]);
    }

    @Command(name = "lenny")
    public static void cmdLenny(ReceivedMessage chat) throws Exception {
        Resource.sendMessage(chat, "( ͡° ͜ʖ ͡°)");
    }
    
    @Command(name = "flip")
    public static void cmdFlip(ReceivedMessage chat) throws Exception {
        Resource.sendMessage(chat, "(╯°□°）╯︵ ┻━┻)");
    }
    
    @Command(name = "idk")
    public static void cmdIdk(ReceivedMessage chat) throws Exception {
        Resource.sendMessage(chat, "¯\\_(ツ)_/¯");
    }
    
    @Command(name = "wat")
    public static void cmdWat(ReceivedMessage chat) throws Exception {
        Resource.sendMessage(chat, "http://waitw.at O.o");
    }
    
    @Command(name = "confirmed")
   public static void cmdConfirmed(ReceivedMessage chat, String question) {
        String[] options = new String[] { "%s Confirmed", "%s won't happen!" ,"%s will happen some day",
            "%s will happen some day", "Just wait and see"};
        int chosen = ThreadLocalRandom.current().nextInt(options.length);

        Resource.sendMessage(chat, String.format(options[chosen], question));
    }

    @Command(name = "pme")
    public static void cmdPme(ReceivedMessage chat, String msg) {
        try {
            SkypeBot.getInstance().getSkype().getContact(chat.getSender().getUsername()).getPrivateConversation().sendMessage(msg);
        } catch (Exception e) {
            Resource.sendMessage("Failed to send a message to you...");
        }
    }
    
    @Command(name = "phallusexercise", alias = {"whatwouldjustissay"})
    public static void cmdphallusexercise(ReceivedMessage message, @Optional String special) {
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
        		"Just in case you ever wanted to wash your vibrator with your eating utensils. I know I do.",
                "I think I've been deepthroating too much... It hurts so much to swallow... ;-;"};
        int chosen = ThreadLocalRandom.current().nextInt(options.length);

        Resource.sendMessage(message, options[chosen]);
    }

    @Command(name = "poll", alias = {"strawpoll"})
    public static void cmdPoll(ReceivedMessage message, String arguments) throws Exception, UnirestException, JSONException {
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
    public static void ayy(ReceivedMessage message, @Optional String ayy) throws Exception {
        if (message.getContent().asPlaintext().contains("lmao")) {
            Resource.sendMessage("ayy lmao");
            return;
        }
        
        Resource.sendMessage("lmao");
    }
    
    @Command(name = "(?i)alien", exact = false, command = false)
    public static void ayyLmao(ReceivedMessage message, @Optional String lmao) {
        Resource.sendMessage("ayy lmao");
    }
    
    @Command(name = "aikar")
    public static void lordSaviorAikar(ReceivedMessage chat) throws Exception {
        Resource.sendMessage("All kneel and hail our lord and saviour Aikar, god of timings™ and all other glory which is efficiency improvements");
    }
}
