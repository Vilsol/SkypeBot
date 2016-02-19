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
import com.samczsun.skype4j.events.chat.sent.PictureReceivedEvent;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.handler.ErrorHandler;
import com.samczsun.skype4j.formatting.Message;
import com.samczsun.skype4j.formatting.Text;
import com.samczsun.skype4j.internal.SkypeEventDispatcher;
import com.samczsun.skype4j.internal.SkypeImpl;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
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
    private Field listenerMap;
    private Runnable relogRunnable;
    private ErrorHandler errorHandler;

    public SkypeBot(String[] args) {
        instance = this;

        try {
            bot = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession();
        } catch (Exception ignored) {
        }

        ModuleManager.loadModules("io.mazenmc.skypebot.modules");
        //System.setProperty("com.samczsun.skype4j.debugExceptions", "true");

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
        //new SuicideThread().commit(); // GO AWAY STUPID SKYPE NOT WORKING BS ADJSUHDAUJS
        //new Thread(new ChatCleaner(), "ChatCleaner Thread").start(); nobody cares lol
    }

    public void loadSkype() {
        try {
            listenerMap = SkypeEventDispatcher.class.getDeclaredField("listeners");
            listenerMap.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        errorHandler = (errorSource, error, shutdown) -> {
            if (shutdown) {
                System.out.println("Error detected, relogging: " + error.toString());
                skype = null;
                scheduler.submit(relogRunnable);
            }
        };
        relogRunnable = () -> {
            System.out.println("Starting relog process");
            Skype newSkype = null;
            boolean retry = true;
            while (retry) {
                try {
                    newSkype = new SkypeBuilder(username, password).withAllResources()
                            .withExceptionHandler(errorHandler).build();
                    newSkype.login();
                    System.out.println("Logged in with username " + username);
                    newSkype.subscribe();
                    System.out.println("Successfully subscribed");
                    newSkype.getEventDispatcher().registerListener(new SkypeEventListener());
                    retry = false;
                } catch (Throwable t) {
                    t.printStackTrace();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            Skype oldSkype = skype;
            if (oldSkype != null) {
                try {
                    Map<?, ?> listeners = (Map<?, ?>) listenerMap.get(oldSkype.getEventDispatcher());
                    listeners.clear();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println("Logging out of old Skype");
                    oldSkype.logout();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            skype = newSkype;
            groupConv = null;
        };
        scheduler.scheduleAtFixedRate(relogRunnable, 0, 8, TimeUnit.HOURS);
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

            ModuleManager.parseText(received);
            StatisticsManager.instance().logMessage(received);
        }

        @EventHandler
        public void onImage(PictureReceivedEvent event) {
            File file = new File("lastImage.png");

            if (file.exists()) {
                file.delete();
            }

            try {
                if (!groupConv().getAllUsers().stream().anyMatch((u) -> u.getUsername().equals(event.getSender().getUsername()))) {
                    return; // don't let randoms send pics
                }

                ImageIO.write(event.getSentImage(), "png", file);
                event.getChat().sendMessage(Message.create().with(Text.rich("Uploading image...").withColor(Color.CYAN)));

                String link = Utils.upload(file);

                event.getChat().sendMessage(Message.create().with(Text.rich("Uploaded!").withColor(Color.GREEN)));
                Resource.sendMessage(event.getSender().getUsername() + " sent an image...");
                Resource.sendMessage(link);
            } catch (Exception ex) {
                try {
                    event.getChat().sendMessage(Message.create().with(Text
                            .rich("ERROR: Unable to send image to group chat").withColor(Color.RED)));
                } catch (Exception ignored) {
                }

                ex.printStackTrace();
            }
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
        try {
            groupConv().sendMessage(message);
        } catch (ConnectionException e) {
            groupConv = null;
            sendMessage(message);
        }
    }

    public void sendMessage(Message message) {
        try {
            groupConv().sendMessage(message);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    public Chat groupConv() {
        if (groupConv == null) {
            try {
                groupConv = skype.getOrLoadChat("19:7cb2a86653594e18abb707e03e2d1848@thread.skype");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return groupConv;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public CooldownHandler getCooldownHandler() {
        return cooldownHandler;
    }
}
