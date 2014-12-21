package me.vilsol.skypebot;

import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.skype.Skype;
import me.vilsol.skypebot.api.API;
import me.vilsol.skypebot.engine.bot.ModuleManager;
import me.vilsol.skypebot.engine.bot.Printer;
import me.vilsol.skypebot.utils.R;
import me.vilsol.skypebot.utils.UpdateChecker;
import me.vilsol.skypebot.utils.Utils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SkypeBot {

    private static SkypeBot instance;

    private boolean locked = false;
    private UpdateChecker updateChecker;
    private ChatterBotSession bot;
    private Server apiServer;
    private Printer printer;
    Connection database;

    public SkypeBot(){
        instance = this;

        printer = new Printer();
        printer.start();

        try{
            bot = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession();
        }catch(Exception ignored){
        }

        ModuleManager.loadModules("me.vilsol.skypebot.modules");

        Skype.setDaemon(false);

        updateChecker = new UpdateChecker();
        updateChecker.start();

        Component c = new Component();
        c.getServers().add(Protocol.HTTP, 25565);

        API apiV1 = new API();
        c.getDefaultHost().attach("", apiV1);
        c.getDefaultHost().attach("/v1", apiV1);

        c.getLogService().setEnabled(false);

        try{
            c.start();
        }catch(Exception ignored){
        }

        Properties connectionProps = new Properties();
        connectionProps.put("user", "skype_bot");
        connectionProps.put("password", "skype_bot");

        try{
            database = DriverManager.getConnection("jdbc:mysql://localhost:3306/skype_bot", connectionProps);
        }catch(SQLException e){
            R.s("Failed to connect to database: " + Utils.upload(ExceptionUtils.getStackTrace(e)));
        }

        R.s("/me " + R.version + " initialized!");
    }

    public static SkypeBot getInstance(){
        if(instance == null){
            new SkypeBot();
        }

        return instance;
    }

    public void sendMessage(String message){
        printer.sendMessage(message);
    }

    public void addToQueue(String[] message){
        printer.addToQueue(message);
    }

    public String askQuestion(String question){
        if(bot == null){
            return "ChatterBot Died";
        }

        try{
            return bot.think(question);
        }catch(Exception ignored){
            return "I am overthinking... (" + ExceptionUtils.getStackTrace(ignored) + ")";
        }
    }

    public Connection getDatabase(){
        return database;
    }

}
