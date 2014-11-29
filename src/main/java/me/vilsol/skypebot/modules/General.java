package me.vilsol.skypebot.modules;

import com.skype.ChatMessage;
import me.vilsol.skypebot.R;
import me.vilsol.skypebot.engine.Command;
import me.vilsol.skypebot.engine.Module;

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

}
