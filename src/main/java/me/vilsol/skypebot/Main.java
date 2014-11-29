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

public class Main implements ClipboardOwner {

    private static Robot robot;

    public Main() throws SkypeException{
        try{
            robot = new Robot();
        }catch(AWTException e){
            e.printStackTrace();
        }

        if(robot == null){
            return;
        }

        Skype.setDaemon(false);
        Skype.addChatMessageListener(new ChatMessageAdapter() {
            public void chatMessageReceived(ChatMessage received) throws SkypeException{
                printText(received.getContent());
            }
        });
    }

    public void printText(String s){
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection ss = new StringSelection(s);
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

    public static void main(String[] args) throws SkypeException{
        new Main();
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents){
    }
}
