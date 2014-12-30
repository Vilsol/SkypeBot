package me.vilsol.skypebot.engine.bot;

import me.vilsol.skypebot.utils.Utils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Printer extends Thread implements ClipboardOwner {

    private Queue<String> messageQueue = new ConcurrentLinkedQueue<>();
    private String[] disallowed = new String[]{"/leave", "/topic", "/alertson", "/alertsoff", "/setrole", "/kick", "/get", "/set", "/golive", "/invite", "/showmembers", "/help"};
    private Clipboard c;
    private Robot robot;

    public Printer(){
        try{
            robot = new Robot();
            robot.setAutoDelay(20);

            c = Toolkit.getDefaultToolkit().getSystemClipboard();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void sendMessage(String message){
        if(message.length() < 200){
            messageQueue.add(message);
        }else{
            messageQueue.add(message.substring(0, 200) + "... " + Utils.upload(message));
        }
    }

    public void addToQueue(String[] message){
        messageQueue.addAll(Arrays.asList(message));
    }

    @Override
    public void run(){
        while(!isInterrupted()){
            if(messageQueue.peek() != null){

                pureSend(messageQueue.remove());

                try{
                    Thread.sleep(150);
                }catch(InterruptedException ignored){
                }
            }else{
                try{
                    Thread.sleep(10);
                }catch(InterruptedException ignored){
                }
            }
        }
    }

    private void pureSend(String message){
        message = message.trim();

        for(String s : disallowed){
            if(message.split(" ")[0].startsWith(s)){
                message = "." + message;
                break;
            }
        }

        StringSelection ss = new StringSelection(message);
        c.setContents(ss, this);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public boolean isQueueEmpty(){
        return messageQueue.size() > 0;
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents){
    }

}
