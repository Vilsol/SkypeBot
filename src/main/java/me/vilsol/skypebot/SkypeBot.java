package me.vilsol.skypebot;

import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.Skype;
import com.skype.SkypeException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;

public class SkypeBot implements ClipboardOwner {

    private static SkypeBot instance;

    private Robot robot;

    public SkypeBot(){
        instance = this;

        try{
            robot = new Robot();
        }catch(AWTException ignored){
        }

        Skype.setDaemon(false);
        try{
            Skype.addChatMessageListener(new ChatMessageAdapter() {
                public void chatMessageReceived(ChatMessage received) throws SkypeException{
                    SkypeBot.getInstance().sendMessage(received.getContent());
                }
            });
        }catch(SkypeException ignored){
        }
    }

    public static SkypeBot getInstance(){
        if(instance == null){
            new SkypeBot();
        }

        return instance;
    }

    public void sendMessage(String message){
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection ss = new StringSelection(message);
        c.setContents(ss, this);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(2);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(2);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents){
    }

}
