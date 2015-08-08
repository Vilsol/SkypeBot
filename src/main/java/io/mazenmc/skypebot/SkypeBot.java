package io.mazenmc.skypebot;

import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.skype.*;
import com.skype.connector.Connector;
import com.skype.connector.ConnectorException;
import io.mazenmc.skypebot.api.API;
import io.mazenmc.skypebot.engine.bot.ModuleManager;
import io.mazenmc.skypebot.engine.bot.Printer;
import io.mazenmc.skypebot.handler.CooldownHandler;
import io.mazenmc.skypebot.stat.StatisticsManager;
import io.mazenmc.skypebot.utils.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

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
    private Server apiServer;
    private ChatterBotSession bot;
    private twitter4j.Twitter twitter;
    private boolean locked = false;
    private Queue<ChatMessage> messages = new ConcurrentLinkedQueue<>();
    private Printer printer;
    private Queue<String> stringMessages = new ConcurrentLinkedQueue<>();
    private UpdateChecker updateChecker;
    private CooldownHandler cooldownHandler;

    public SkypeBot(String[] args) {
        instance = this;

        System.setProperty("skype.api.impl", "x11");

        printer = new Printer();
        printer.start();

        try {
            bot = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession();
        } catch (Exception ignored) {
        }

        ModuleManager.loadModules("io.mazenmc.skypebot.modules");

        Skype.setDaemon(false);
        try {
            Skype.addChatMessageListener(new GlobalChatMessageListener() {
                public void chatMessageReceived(ChatMessage received) throws SkypeException {
                    Callback<String> callback = null;
                    Chat chat = received.getChat();

                    if (!locked) {
                        for (User user : usersFor(chat)) {
                            System.out.println("checked " + user.getId());
                            if (!StatisticsManager.instance().statistics()
                                    .containsKey(user.getId())) {
                                StatisticsManager.instance().addStat(user.getId());
                                System.out.println("added " + user.getId() + " as a valid stat");
                            }
                        }

                        locked = true;
                    }

                    if ((callback = Resource.getCallback(received.getSenderId())) != null) {
                        callback.callback(received.getContent());
                        return;
                    }

                    stringMessages.add(Utils.serializeMessage(received));
                    StatisticsManager.instance().logMessage(received);
                    messages.add(received);
                    ModuleManager.parseText(received);
                }

                @Override
                public void chatMessageSent(ChatMessage sentChatMessage) throws SkypeException {
                    StatisticsManager.instance().logMessage(sentChatMessage);
                }

                @Override
                public void newChatStarted(Chat chat, User[] users) {
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

    public static SkypeBot getInstance() {
        if (instance == null) {
            new SkypeBot(new String[]{});
        }

        return instance;
    }

    public void addToQueue(String[] message) {
        printer.addToQueue(message);
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

    public Printer getPrinter() {
        return printer;
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

    public boolean isQueueEmpty() {
        return printer.isQueueEmpty();
    }

    public void sendMessage(String message) {
        printer.sendMessage(message);
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public CooldownHandler getCooldownHandler() {
        return cooldownHandler;
    }

    private User[] usersFor(Chat chat) {
        try {
            String ex = "GET CHAT " + chat.getId() + " MEMBERS";
            String responseHeader = "CHAT " + chat.getId() + " MEMBERS ";
            String response = Connector.getInstance().execute(ex, responseHeader);

            System.out.println(ex);
            System.out.println(response);
            System.out.println(responseHeader.length());
            
            String data = response.substring(responseHeader.length());

            System.out.println(data);

            if("".equals(data)) {
                return new User[0];
            } else {
                String[] ids = data.split(" ");
                User[] users = new User[ids.length];

                for(int i = 0; i < ids.length; ++i) {
                    users[i] = User.getInstance(ids[i]);
                }

                return users;
            }
        } catch (ConnectorException ignored) {
            return null;
        }
    }
}
