package me.vilsol.skypebot.modules;

import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import me.vilsol.skypebot.SkypeBot;
import me.vilsol.skypebot.engine.bot.*;
import me.vilsol.skypebot.utils.R;
import me.vilsol.skypebot.utils.Utils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.Map;
import java.util.Random;

public class General implements Module {

    private static ChatterBotSession cleverBot;
    private static ChatterBotSession jabberWacky;
    private static boolean ranting = false;
    private static Thread rantThread;

    @Command(name = "about", alias = {"aboot"})
    public static void cmdAbout(ChatMessage chat){
        R.s(new String[] {"Skype bot made by Vilsol", "Version: " + R.version});
    }

    @Command(name = "restart", allow = {"vilsol"})
    public static void cmdRestart(ChatMessage chat){
        Utils.restartBot();
    }

    @Command(name = "ping")
    public static void cmdPing(ChatMessage chat){
        R.s("Pong!");
    }

    @Command(name = "topkek")
    public static void cmdTopKek(ChatMessage chat){
        R.s("https://topkek.mazenmc.io/ Gotta be safe while keking!");
    }

    @Command(name = "doc")
    public static void cmdDoc(ChatMessage chat){
        R.s("https://docs.google.com/document/d/1LoTYCauVyEiiLZ5Klw3UB8rbEtkzNn7VLmgm87Fzyy0/edit#");
    }

    @Command(name = "spoon")
    public static void cmdSpoon(ChatMessage chat){
        R.s("There is no spoon");
    }

    @Command(name = "9gag", exact = false, command = false)
    public static void cmd9Gag(ChatMessage chat){
        R.s("Shut up 9Fag!");
    }

    @Command(name = "8ball")
    public static void cmd8Ball(ChatMessage chat, @Optional
    String question){
        String[] options = new String[] {"It is certain", "It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes", "Reply hazy try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again", "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};
        int chosen = new Random().nextInt(options.length);
        R.s(options[chosen]);
    }

    @Command(name = "random")
    public static void cmdRandom(ChatMessage chat, int low, int high){
        if (low > high) {
            low ^= high;
            high ^= low;
            low ^= high;
        }

        if((high - low) + low <= 0){
            R.s("One number must be larger than the other");
            return;
        }

        R.s(new Random().nextInt(high - low) + low);
    }

    @Command(name = "fish go moo", exact = false, command = false)
    public static void cmdFishGoMoo(ChatMessage chat) throws SkypeException{
        R.s("/me notes that " + chat.getSenderDisplayName() + " is truly enlightened.");
    }

    @Command(name = "bot")
    public static void cmdBot(ChatMessage chat, String message){
        R.s("/me " + message);
    }

    @Command(name="help")
    public static void cmdHelp(ChatMessage chat){
        String commands = "Available Commands: ";

        for(Map.Entry<String, CommandData> data : ModuleManager.getCommands().entrySet()){
            if(!data.getValue().getCommand().exact()){
                continue;
            }

            if(!commands.equals("Available Commands: ")){
                commands += ", ";
            }

            commands += R.command + data.getKey();

            if(!data.getValue().getParamaterNames().equals("")){
                commands += " " + data.getValue().getParamaterNames();
            }
        }

        R.s(commands);
    }

    @Command(name = "capture")
    public static void cmdCapture(ChatMessage chat){
        R.s("Capture: " + Utils.upload(SkypeBot.getInstance().getLastStringMessages()));
    }

    @Command(name = "git")
    public static void cmdGit(ChatMessage chat){
        R.s("Git Repository: https://github.com/Vilsol/SkypeBot");
    }

    @Command(name = "resolve")
    public static void cmdResolve(ChatMessage chat, String user){
        R.s(user + ": " + Utils.resolveSkype(user));
    }

    @Command(name = "c")
    public static void cmdC(ChatMessage chat, String question) throws SkypeException {
        R.s("[" + chat.getSenderDisplayName() + "] " + SkypeBot.getInstance().askQuestion(question));
    }

    @Command(name = "rant")
    public static void cmdRant(ChatMessage chat, @Optional final String question){
        if(ranting){
            rantThread.stop();
            ranting = false;
            R.s("Ranting stopped!");
            return;
        }

        if(question == null || question.equals("")){
            R.s("Enter a question!");
            return;
        }

        try{
            cleverBot = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession();
            jabberWacky = new ChatterBotFactory().create(ChatterBotType.JABBERWACKY).createSession();
        }catch(Exception ignored){
        }

        if(cleverBot == null || jabberWacky == null){
            R.s("One of the bots died!");
            return;
        }

        R.s("Ranting started...");

        ranting = true;

        rantThread = new Thread(){

            String cleverThink = null;

            @Override
            public void run(){
                while(true){
                    try{
                        if(cleverThink == null){
                            cleverThink = question;
                        }

                        String cleverBotResponse = cleverBot.think(cleverThink);

                        assert(cleverBotResponse != null && !cleverBotResponse.trim().equals(""));
                        R.s("[CB] " + cleverBotResponse);
                        Thread.sleep(500);

                        String jabberWackyResponse = jabberWacky.think(cleverBotResponse);
                        cleverThink = jabberWackyResponse;

                        assert(jabberWackyResponse != null && !jabberWackyResponse.trim().equals(""));
                        R.s("[JW] " + jabberWackyResponse);
                        Thread.sleep(500);
                    }catch(Exception e){
                        R.s("A bot got banned :( (" + Utils.upload(ExceptionUtils.getStackTrace(e)) + ")");
                        ranting = false;
                        this.stop();
                    }
                }
            }
        };

        rantThread.start();
    }

    @Command(name = "quote")
    public static void cmdQuote(ChatMessage message, String category){
        try{
            HttpResponse<JsonNode> response = Unirest.post("https://andruxnet-random-famous-quotes.p.mashape.com/cat=" + category).header("X-Mashape-Key", "rIizXnIZ7Umsh3o3sfCLfL86lZY2p1bda69jsnAqK1Sc6C5CV1").header("Content-Type", "application/x-www-form-urlencoded").asJson();
            R.s("\"" + response.getBody().getObject().get("quote") + "\" - " + response.getBody().getObject().get("author"));
        }catch(UnirestException e){
        }
    }

    @Command(name = "dreamincode", alias = {"whatwouldmazensay"})
    public static void cmddreamincode(ChatMessage chat){
        String[] options = new String[] {"No, Im not interested in having a girlfriend I find it a tremendous waste of time.", "Hi, my name is Santiago Gonzalez and I'm 14 and I like to program.", "Im fluent in a dozen different programming languages.", "Thousands of people have downloaded my apps for the mac, iphone, and ipad.", "I will be 16 when I graduate college and 17 when I finish my masters.", "I really like learning, I find it as essential as eating.", "Dr. Bakos: I often have this disease which I call long line-itus.", "Dr. Bakos: Are you eager enough just to write down a slump of code, or is the code itself a artistic median?", "Beutaiful code is short and conzied.", "Sometimes when I goto sleep im stuck with that annoying bug I cannot fix, and in my dreams I see myself programming. When I wake up I have the solution!", "One of the main reasons I started developing apps was to help people what they want to do like decorate a christmas tree.", "I really like to crochet.", "I make good website http://slgonzalez.com/"};
        int chosen = new Random().nextInt(options.length);
        R.s(options[chosen]);
    }

    @Command(name = "authorize")
    public static void cmdAuthorize(ChatMessage chat) throws SkypeException{
        if(!chat.getSender().isAuthorized()){
            chat.getSender().setAuthorized(true);
        }
    }

}
