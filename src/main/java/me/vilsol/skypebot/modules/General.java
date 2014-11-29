package me.vilsol.skypebot.modules;

import com.skype.ChatMessage;
import me.vilsol.skypebot.R;
import me.vilsol.skypebot.engine.Command;
import me.vilsol.skypebot.engine.Module;

import java.util.Random;

public class General implements Module {

    @Command(name = "about", alias = {"aboot"})
    public static void cmdAbout(ChatMessage message){
        R.s(new String[] {"Skype bot made by Vilsol", "Version: " + R.version});
    }

    @Command(name = "restart", allow = {"vilsol"})
    public static void cmdRestart(ChatMessage message){
        R.s("/me " + R.version + " Restarting...");
        System.out.println("Restarting...");
        System.exit(0);
    }

    @Command(name = "ping")
    public static void cmdPing(ChatMessage message){
        R.s("Pong!");
    }

    @Command(name = "topkek")
    public static void cmdTopKek(ChatMessage message){
        R.s("https://topkek.mazenmc.io/ Gotta be safe while keking!");
    }

    @Command(name = "doc")
    public static void cmdDoc(ChatMessage message){
        R.s("https://docs.google.com/document/d/1LoTYCauVyEiiLZ5Klw3UB8rbEtkzNn7VLmgm87Fzyy0/edit#");
    }

    @Command(name = "spoon")
    public static void cmdSpoon(ChatMessage message){
        R.s("There is no spoon");
    }

    @Command(name = "9gag", exact = false, command = false)
    public static void cmd9Gag(ChatMessage message){
        R.s("Shut up 9Fag!");
    }

    @Command(name = "8ball", parameters = "(.*)")
    public static void cmd8Ball(ChatMessage message){
        String[] options = new String[] {"It is certain", "It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes", "Reply hazy try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again", "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"};
        int chosen = new Random().nextInt(options.length);
        R.s(options[chosen]);
    }


}
