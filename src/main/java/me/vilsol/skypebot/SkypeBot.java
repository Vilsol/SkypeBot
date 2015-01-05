package me.vilsol.skypebot;

import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Skype;
import com.skype.SkypeException;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SkypeBot {

    private static SkypeBot instance;
    Connection database;
    private boolean locked = false;
    private UpdateChecker updateChecker;
    private Queue<ChatMessage> messages = new ConcurrentLinkedQueue<>();
    private Queue<String> stringMessages = new ConcurrentLinkedQueue<>();
    private ChatterBotSession bot;
    private Server apiServer;
    private Printer printer;

    public SkypeBot() {
        instance = this;

        System.setProperty("skype.api.impl", "x11");

        printer = new Printer();
        printer.start();

        try {
            bot = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession();
        } catch (Exception ignored) {
        }

        ModuleManager.loadModules("me.vilsol.skypebot.modules");

        Skype.setDaemon(false);
        try {
            Skype.addChatMessageListener(new ChatMessageAdapter() {
                public void chatMessageReceived(ChatMessage received) throws SkypeException {
                    if (messages.size() > 100) {
                        messages.remove();
                        stringMessages.remove();
                    }

                    System.out.println("got: " + received.getContent());

                    stringMessages.add(Utils.serializeMessage(received));
                    messages.add(received);
                    ModuleManager.parseText(received);
                }
            });
        } catch (SkypeException e) {
            e.printStackTrace();
        }

        updateChecker = new UpdateChecker();
        updateChecker.start();

        Component c = new Component();
        c.getServers().add(Protocol.HTTP, 25565);

        API apiV1 = new API();
        c.getDefaultHost().attach("", apiV1);
        c.getDefaultHost().attach("/v1", apiV1);

        c.getLogService().setEnabled(false);

        try {
            c.start();
        } catch (Exception ignored) {
        }

        Properties connectionProps = new Properties();
        connectionProps.put("user", "skype_bot");
        connectionProps.put("password", "skype_bot");

        try {
            database = DriverManager.getConnection("jdbc:mysql://localhost:3306/skype_bot", connectionProps);
        } catch (SQLException e) {
            R.s("Failed to connect to database: " + Utils.upload(ExceptionUtils.getStackTrace(e)));
        }

        R.s("/me " + R.VERSION + " initialized!");
    }

    public static SkypeBot getInstance() {
        if (instance == null) {
            new SkypeBot();
        }

        return instance;
    }

    public void sendMessage(String message) {
        printer.sendMessage(message);
    }

    public void addToQueue(String[] message) {
        printer.addToQueue(message);
    }

    public List<ChatMessage> getLastMessages() {
        List<ChatMessage> list = new LinkedList<>();
        Queue<ChatMessage> newMessages = new ConcurrentLinkedQueue<>();

        messages.stream().forEach(m -> {
            list.add(m);
            newMessages.add(m);
        });

        messages = newMessages;

        return list;
    }

    public List<String> getLastStringMessages() {
        List<String> list = new LinkedList<>();
        Queue<String> newMessages = new ConcurrentLinkedQueue<>();

        stringMessages.stream().forEach(m -> {
            list.add(m);
            newMessages.add(m);
        });

        stringMessages = newMessages;

        return list;
    }

    public String askQuestion(String question) {
        if (bot == null) {
            return "ChatterBot Died";
        }

        try {
            return bot.think(question);
        } catch (Exception ignored) {
            return "I am overthinking... (" + ExceptionUtils.getStackTrace(ignored) + ")";
        }
    }

    public Connection getDatabase() {
        return database;
    }

    public boolean isQueueEmpty() {
        return printer.isQueueEmpty();
    }

}
