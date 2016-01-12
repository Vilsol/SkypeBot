package io.mazenmc.skypebot;

import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.GroupChat;
import com.samczsun.skype4j.chat.messages.ReceivedMessage;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent;
import com.samczsun.skype4j.exceptions.ConnectionException;
import io.mazenmc.skypebot.api.API;
import io.mazenmc.skypebot.engine.bot.ModuleManager;
import io.mazenmc.skypebot.handler.CooldownHandler;
import io.mazenmc.skypebot.stat.StatisticsManager;
import io.mazenmc.skypebot.utils.*;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SkypeBot {

    private static SkypeBot instance;
    Connection database;
    private Server apiServer;
    private ChatterBotSession bot;
    private twitter4j.Twitter twitter;
    private boolean locked = false;
    private Skype skype;
    private UpdateChecker updateChecker;
    private CooldownHandler cooldownHandler;
    private String username;
    private String password;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SkypeBot(String[] args) {
        instance = this;

        try {
            bot = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession();
        } catch (Exception ignored) {
        }

        ModuleManager.loadModules("io.mazenmc.skypebot.modules");

        try {
            loadConfig();
            loadSkype();
        } catch (Exception e) {
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
        }

        List<String> twitterInfo = Utils.readAllLines("twitter_auth");

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(twitterInfo.get(0))
                .setOAuthConsumerSecret(twitterInfo.get(1))
                .setOAuthAccessToken(twitterInfo.get(2))
                .setOAuthAccessTokenSecret(twitterInfo.get(3));
        twitter = new TwitterFactory(cb.build()).getInstance();

        cooldownHandler = new CooldownHandler();
        StatisticsManager.instance().loadStatistics();
        new Thread(new ChatCleaner(), "ChatCleaner Thread").start();

        Resource.sendMessage("/me " + Resource.VERSION + " initialized!");
    }

    public void loadSkype() {
        try {
            skype = new SkypeBuilder(username, password).withAllResources().build();
            skype.login();
            skype.getEventDispatcher().registerListener(new SkypeEventListener());
            System.out.println("Logged in as " + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() throws IOException {
        Properties prop = new Properties();
        File config = new File("bot.properties");
        if (!config.exists()) {
            try (OutputStream output = new FileOutputStream(config)) {
                prop.setProperty("username", "your.skype.username");
                prop.setProperty("password", "your.skype.password");
                prop.store(output, null);
            }
            System.out.println("Generated default configuration. Exiting.");
            return;
        }

        try (InputStream input = new FileInputStream(config)) {
            prop.load(input);
            username = prop.getProperty("username");
            password = prop.getProperty("password");
        }
    }

    private class SkypeEventListener implements Listener {
        @EventHandler
        public void onMessage(MessageReceivedEvent event) {
            Callback<String> callback;
            ReceivedMessage received = event.getMessage();

            if ((callback = Resource.getCallback(received.getSender().getUsername())) != null) {
                callback.callback(received.getContent().asPlaintext());
                return;
            }

            StatisticsManager.instance().logMessage(received);
            ModuleManager.parseText(received);
        }
    }

    public static SkypeBot getInstance() {
        if (instance == null) {
            new SkypeBot(new String[]{});
        }

        return instance;
    }

    public Skype getSkype() {
        return skype;
    }

    public String askQuestion(String question) {
        if (bot == null) {
            return "ChatterBot Died";
        }

        try {
            return bot.think(question);
        } catch (Exception ignored) {
            return "I am overthinking...";
        }
    }

    public Connection getDatabase() {
        return database;
    }

    private Chat groupConv;

    public void sendMessage(String message) {
        if (groupConv == null) {
            for (Chat conv : skype.getAllChats()) {
                if (conv instanceof GroupChat) {
                    if (((GroupChat) conv).getTopic().contains("Mazen's Skype")) {
                        groupConv = conv;
                        System.out.println(conv.getIdentity() + " was the chosen group conv");
                        break;
                    }
                }
            }
        }

        try {
            groupConv.sendMessage(message);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    public Chat groupConv() {
        return groupConv;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public CooldownHandler getCooldownHandler() {
        return cooldownHandler;
    }
}
