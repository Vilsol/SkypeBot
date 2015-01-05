package io.mazenmc.skypebot.modules;

import com.google.api.client.http.HttpRequest;
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
import com.google.common.base.Joiner;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import io.mazenmc.skypebot.SkypeBot;
import io.mazenmc.skypebot.engine.bot.*;
import io.mazenmc.skypebot.utils.Resource;
import io.mazenmc.skypebot.utils.Utils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONException;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class General implements Module {

    private static ChatterBotSession cleverBot;
    private static ChatterBotSession jabberWacky;
    private static Thread rantThread;
    private static boolean ranting = false;

    @Command(name = "8ball")
    public static void cmd8Ball(ChatMessage chat, @Optional
    String question) {
        String[] options = new String[]{"It is certain", "It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes", "Reply hazy try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again", "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};
        int chosen = new Random().nextInt(options.length);
        Resource.sendMessage(options[chosen]);
    }

    @Command(name = "9gag", exact = false, command = false)
    public static void cmd9Gag(ChatMessage chat) {
        Resource.sendMessage("Shut up 9Fag!");
    }

    @Command(name = "about")
    public static void cmdAbout(ChatMessage chat) {
        Resource.sendMessage(new String[]{"Skype bot made by Vilsol, MazenMC & stuntguy3000", "Version: " + Resource.VERSION});
    }

    @Command(name = "authorize")
    public static void cmdAuthorize(ChatMessage chat) throws SkypeException {
        Resource.sendMessage("In order for authorization to work, you must send me a contact request. I will now try to authorize you!");
        if (!chat.getSender().isAuthorized()) {
            chat.getSender().setAuthorized(true);
            chat.getSender().send("You are now authorized!");
        } else {
            chat.getSender().send("You are already authorized silly!");
        }
    }

    @Command(name = "bot")
    public static void cmdBot(ChatMessage chat, String message) {
        Resource.sendMessage("/me " + message);
    }

    @Command(name = "c")
    public static void cmdC(ChatMessage chat, String question) throws SkypeException {
        Resource.sendMessage("[" + chat.getSenderDisplayName() + "] " + SkypeBot.getInstance().askQuestion(question));
    }

    @Command(name = "cloudflare")
    public static void cmdCloudflare(ChatMessage chat, String url) throws UnirestException {
        if (url.startsWith("http://")) {
            url = url.substring(7);
        } else if (url.startsWith("https://")) {
            url = url.substring(8);
        }

        if (url.startsWith("www")) {
            url = url.substring(3);
        }

        if (url.contains("/")) {
            url = url.split("/")[0];
        }

        HttpRequestWithBody request = Unirest.post("http://iphostinfo.com/cloudflare/" + url);
        request.field("cfdns", url);

        HttpResponse<String> response = request.asString();
        String body = response.getBody();

        Pattern p = Pattern.compile("<TR><TD>(.*)</TD></TR>");
        Matcher m = p.matcher(body);

        if (m.find()) {
            String data = m.group();

            Pattern x = Pattern.compile("<b>(.*)</b></a>");

            List<String> resolved = new ArrayList<>();

            data = data.replaceAll("</TR>\\s<TR>", "</TR><TR>");

            for (String sub : data.split("</TR><TR>")) {
                Matcher z = x.matcher(sub);
                if (z.find()) {
                    Pattern c = Pattern.compile("<TD>(.*)</TD> <TD>");
                    Matcher v = c.matcher(sub);
                    v.find();

                    String ip = z.group();
                    ip = ip.substring(3, ip.length() - 8);

                    String name = v.group();
                    name = name.substring(4, name.length() - 11);
                    name = name.replace("." + url, "");

                    resolved.add(name + ":" + ip);
                }
            }

            if (resolved.size() > 0) {
                Resource.sendMessage(url + ": " + Joiner.on(", ").join(resolved));
            } else {
                Resource.sendMessage(url + ": No IP's Found!");
            }
        } else {
            Resource.sendMessage("Domain Unresolvable!");
        }
    }

    @Command(name = "doc")
    public static void cmdDoc(ChatMessage chat) {
        Resource.sendMessage("https://docs.google.com/document/d/1LoTYCauVyEiiLZ5Klw3UB8rbEtkzNn7VLmgm87Fzyy0/edit#");
    }

    @Command(name = "fish go moo", exact = false, command = false)
    public static void cmdFishGoMoo(ChatMessage chat) throws SkypeException {
        Resource.sendMessage("/me notes that " + chat.getSenderDisplayName() + " is truly enlightened.");
    }

    @Command(name = "git")
    public static void cmdGit(ChatMessage chat) {
        Resource.sendMessage("Git Repository: https://github.com/MazenMC/SkypeBot");
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

        Resource.sendMessage("Available Commands: " + Utils.upload(commands));
    }

    @Command(name = "lmgtfy")
    public static void cmdLmgtfy(ChatMessage chat, String question) throws SkypeException {
        //Thanks DevRoMc for the idea. - ibJarrett
        String returnString = "http://lmgtfy.com/?q=";
        Resource.sendMessage("[" + chat.getSenderDisplayName() + "] " + returnString + URLEncoder.encode(question));
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
    final String ip) throws JSONException {
        if (ip == null) {
            Resource.sendMessage("Fuck yo ping!");
        } else {
            try {
                HttpResponse<JsonNode> response = Unirest.get("https://igor-zachetly-ping-uin.p.mashape.com/pinguin.php?address=" + URLEncoder.encode(ip))
                        .header("X-Mashape-Key", "sHb3a6jczqmshcYqUEwQq3ZZR3BVp18NqaAjsnIYFvVNHMqvCb")
                        .asJson();
                if (response.getBody().getObject().get("result").equals(false)) {
                    Resource.sendMessage("Thats an invalid IP / domain silly!");
                } else {
                    Resource.sendMessage(ip + " - Response took " + response.getBody().getObject().get("time") + "ms");
                }
            } catch (UnirestException e) {
                Resource.sendMessage("Error: " + Utils.upload(ExceptionUtils.getStackTrace(e)));
            }
        }
    }

    @Command(name = "quote")
    public static void cmdQuote(ChatMessage message, String category) {
        try {
            HttpResponse<JsonNode> response = Unirest.post("https://andruxnet-random-famous-quotes.p.mashape.com/cat=" + category).header("X-Mashape-Key", "rIizXnIZ7Umsh3o3sfCLfL86lZY2p1bda69jsnAqK1Sc6C5CV1").header("Content-Type", "application/x-www-form-urlencoded").asJson();
            Resource.sendMessage("\"" + response.getBody().getObject().get("quote") + "\" - " + response.getBody().getObject().get("author"));
        } catch (UnirestException | JSONException e) {
        }
    }

    @Command(name = "random")
    public static void cmdRandom(ChatMessage chat, int low, int high) {
        if (low > high) {
            low ^= high;
            high ^= low;
            low ^= high;
        }

        if ((high - low) + low <= 0) {
            Resource.sendMessage("One number must be larger than the other");
            return;
        }

        Resource.sendMessage(new Random().nextInt(high - low) + low);
    }

    @Command(name = "rant")
    public static void cmdRant(ChatMessage chat, @Optional final String question) {
        if (ranting) {
            rantThread.stop();
            ranting = false;
            Resource.sendMessage("Ranting stopped!");
            return;
        }

        if (question == null || question.equals("")) {
            Resource.sendMessage("Enter a question!");
            return;
        }

        try {
            cleverBot = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession();
            jabberWacky = new ChatterBotFactory().create(ChatterBotType.JABBERWACKY).createSession();
        } catch (Exception ignored) {
        }

        if (cleverBot == null || jabberWacky == null) {
            Resource.sendMessage("One of the bots died!");
            return;
        }

        Resource.sendMessage("Ranting started...");

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
                        Resource.sendMessage("A bot got banned :( (" + Utils.upload(ExceptionUtils.getStackTrace(e)) + ")");
                        ranting = false;
                        this.stop();
                    }
                }
            }
        };

        rantThread.start();
    }

    @Command(name = "resolve")
    public static void cmdResolve(ChatMessage chat, String user) {
        Resource.sendMessage(user + ": " + Utils.resolveSkype(user));
    }

    @Command(name = "restart", admin = true)
    public static void cmdRestart(ChatMessage chat) {
        Utils.restartBot();
    }

    @Command(name = "sql")
    public static void cmdSQL(ChatMessage chat, String query) throws SQLException {
        if (SkypeBot.getInstance().getDatabase() == null) {
            Resource.sendMessage("Connection is down!");
            return;
        }

        if (query.toUpperCase().contains("DROP DATABASE") || query.toUpperCase().contains("CREATE DATABASE") || query.toUpperCase().contains("USE")) {
            Resource.sendMessage("Do not touch the databases!");
            return;
        }

        if (query.toUpperCase().contains("INFORMATION_SCHEMA")) {
            Resource.sendMessage("Not that fast!");
            return;
        }

        Statement stmt = null;

        try {
            stmt = SkypeBot.getInstance().getDatabase().createStatement();
            if (query.toLowerCase().startsWith("select") || query.toLowerCase().startsWith("show")) {
                ResultSet result = stmt.executeQuery(query);
                String parsed = Utils.parseResult(result);
                parsed = query + "\n\n" + parsed;
                Resource.sendMessage("SQL Query Successful: " + Utils.upload(parsed));
            } else {
                stmt.execute(query);
                Resource.sendMessage("SQL Query Successful!");
            }
        } catch (SQLException e) {
            String message = e.getMessage();
            message = message.replace("You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near", "");
            Resource.sendMessage("Error executing SQL: " + message);
        } catch (Exception e) {
            Resource.sendMessage("Error: " + Utils.upload(ExceptionUtils.getStackTrace(e)));
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Command(name = "spoon")
    public static void cmdSpoon(ChatMessage chat) {
        Resource.sendMessage("There is no spoon");
    }

    @Command(name = "topkek")
    public static void cmdTopKek(ChatMessage chat) {
        Resource.sendMessage("https://topkek.mazenmc.io/ Gotta be safe while keking!");
    }

    @Command(name = "define")
    public static void cmddefine(ChatMessage chat, String word) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(Resource.URBAN_DICTIONARY_URL + URLEncoder.encode(word)).openConnection();
        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        List<String> lines = new ArrayList<>(400);
        for (int i = 0; i < 400; i++) lines.add(reader.readLine());

        int definitionStart = lines.indexOf("<div class='meaning'>");
        String definition = lines.get(definitionStart + 1);
        definition = definition.replaceAll("<a href=\"\\/define\\.php\\?term=[a-z]*\">", "");
        definition = definition.replace("</a>", "");

        Resource.sendMessage("Definition of " + word + ": " + Jsoup.parse(definition).text());
    }

    @Command(name = "dreamincode", alias = {"whatwouldmazensay"})
    public static void cmddreamincode(ChatMessage chat) {
        String[] options = new String[]{"No, Im not interested in having a girlfriend I find it a tremendous waste of time.", "Hi, my name is Santiago Gonzalez and I'm 14 and I like to program.", "Im fluent in a dozen different programming languages.", "Thousands of people have downloaded my apps for the mac, iphone, and ipad.", "I will be 16 when I graduate college and 17 when I finish my masters.", "I really like learning, I find it as essential as eating.", "Dr. Bakos: I often have this disease which I call long line-itus.", "Dr. Bakos: Are you eager enough just to write down a slump of code, or is the code itself a artistic median?", "Beutaiful code is short and conzied.", "Sometimes when I goto sleep im stuck with that annoying bug I cannot fix, and in my dreams I see myself programming. When I wake up I have the solution!", "One of the main reasons I started developing apps was to help people what they want to do like decorate a christmas tree.", "I really like to crochet.", "I make good website http://slgonzalez.com/"};
        int chosen = new Random().nextInt(options.length);
        Resource.sendMessage(options[chosen]);
    }

    @Command(name = "relevantxkcd")
    public static void cmdrelevantxkcd(ChatMessage chat, @Optional String name) {
        if (name == null) {
            try {
                HttpResponse<JsonNode> response = Unirest.get("http://xkcd.com/info.0.json")
                        .asJson();
                int urlnumber = new Random().nextInt((Integer) response.getBody().getObject().get("num")) + 1;
                HttpResponse<JsonNode> xkcd = Unirest.get("http://xkcd.com/" + urlnumber + "/info.0.json")
                        .asJson();
                String transcript = xkcd.getBody().getObject().get("transcript").toString();
                String image = xkcd.getBody().getObject().get("img").toString();
                Resource.sendMessage("Image - " + image);
                Resource.sendMessage("Transcript - " + transcript);
            } catch (Exception e) {
                Resource.sendMessage("Error: " + Utils.upload(ExceptionUtils.getStackTrace(e)));
            }
        } else {
            try {
                String key = "AIzaSyDulfiY_C1PK19PinCLmagTeMMeVlmhimI";
                String cx = "012652707207066138651:Azudjtuwe28q";

                HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {

                    @Override
                    public void initialize(HttpRequest request) throws IOException {

                    }
                };
                JsonFactory jsonFactory = new JacksonFactory();

                Customsearch custom = new Customsearch(new NetHttpTransport(), jsonFactory, httpRequestInitializer);
                Customsearch.Cse.List list = custom.cse().list(name);
                list.setCx(cx);
                list.setKey(key);

                Search result = list.execute();

                Customsearch.Cse.List listResult = (Customsearch.Cse.List) result.getItems();
                if (listResult.isEmpty()) {
                    Resource.sendMessage("No results found. :/ sorry");
                } else {
                    Result first = (Result) listResult.get(0);
                    Resource.sendMessage(first.getFormattedUrl());
                }
            } catch (IOException e) {
                Resource.sendMessage("Error: " + Utils.upload(ExceptionUtils.getStackTrace(e)));
            }
        }
    }

}
