package me.vilsol.skypebot;

import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Skype;
import com.skype.SkypeException;
import me.vilsol.skypebot.api.Test;
import me.vilsol.skypebot.engine.ModuleManager;
import me.vilsol.skypebot.utils.R;
import me.vilsol.skypebot.utils.UpdateChecker;
import me.vilsol.skypebot.utils.Utils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.restlet.Server;
import org.restlet.data.Protocol;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SkypeBot implements ClipboardOwner {

    private static SkypeBot instance;

    private Robot robot;
    private boolean locked = false;
    private UpdateChecker updateChecker;
    private Queue<String> queue = new ConcurrentLinkedQueue<>();
    private Queue<ChatMessage> messages = new ConcurrentLinkedQueue<>();
    private Queue<String> stringMessages = new ConcurrentLinkedQueue<>();
    private ChatterBotSession bot;
    private Clipboard c;
    private String lastSentMessage;
    private Server apiServer;

    public SkypeBot(){
        instance = this;

        c = Toolkit.getDefaultToolkit().getSystemClipboard();

        try{
            robot = new Robot();
        }catch(AWTException ignored){
        }

        try{
            bot = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession();
        }catch(Exception ignored){
        }

        ModuleManager.loadModules("me.vilsol.skypebot.modules");

        Skype.setDaemon(false);
        try{
            Skype.addChatMessageListener(new ChatMessageAdapter() {
                public void chatMessageReceived(ChatMessage received) throws SkypeException{
                    if(messages.size() > 100){
                        messages.remove();
                        stringMessages.remove();
                    }

                    stringMessages.add(Utils.serializeMessage(received));
                    messages.add(received);
                    ModuleManager.parseText(received);
                }
            });
        }catch(SkypeException ignored){
        }

        updateChecker = new UpdateChecker();
        updateChecker.start();

        try{
            apiServer = new Server(Protocol.HTTP, 25565, Test.class);
            apiServer.start();
        }catch(Exception ignored){
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
        if(locked){
            queue.add(message);
            return;
        }

        locked = true;

        if(message != null){
            pureSend(message);
        }

        Iterator<String> i = queue.iterator();

        while(i.hasNext()){
            String s = i.next();
            pureSend(s);
            i.remove();

            if(i.hasNext()){
                robot.delay(200);
            }
        }

        locked = false;

        robot.delay(200);
    }

    private void pureSend(String message){
        if(lastSentMessage != null && lastSentMessage.equals(message)){
            return;
        }

        lastSentMessage = message;

        robot.delay(10);
        StringSelection ss = new StringSelection(message);
        c.setContents(ss, this);

        robot.delay(10);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(10);
    }

    public void addToQueue(String[] s){
        Collection<String> c = new ArrayList<>(Arrays.asList(s));
        queue.addAll(c);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents){
    }

    public List<ChatMessage> getLastMessages(){
        List<ChatMessage> list = new LinkedList<>();
        Queue<ChatMessage> newMessages = new ConcurrentLinkedQueue<>();

        messages.stream().forEach(m -> {
            list.add(m);
            newMessages.add(m);
        });

        messages = newMessages;

        return list;
    }

    public List<String> getLastStringMessages(){
        List<String> list = new LinkedList<>();
        Queue<String> newMessages = new ConcurrentLinkedQueue<>();

        stringMessages.stream().forEach(m -> {
            list.add(m);
            newMessages.add(m);
        });

        stringMessages = newMessages;

        return list;
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

}
