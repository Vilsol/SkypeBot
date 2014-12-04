package me.vilsol.skypebot.utils;

import com.google.common.base.Joiner;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import me.vilsol.skypebot.Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isInteger(Object s){
        try{
            Integer.parseInt(s.toString());
            return true;
        }catch(Exception ignore){
        }

        return false;
    }

    public static boolean isDouble(Object s){
        try{
            Double.parseDouble(s.toString());
            return true;
        }catch(Exception ignore){
        }

        return false;
    }

    public static String serializeMessage(ChatMessage message){
        String s = "";

        try{
            s += "[" + message.getTime().toString() + "] " + message.getSenderDisplayName() + ": " + message.getContent();
        }catch(SkypeException ignored){
        }

        return s;
    }

    public static String upload(List<ChatMessage> s){
        String data = "";
        for(ChatMessage m : s){
            data += serializeMessage(m) + "\n";
        }
        return upload(data);
    }

    public static String upload(Collection<String> s){
        return upload(Joiner.on("\n").join(s));
    }

    public static String upload(ChatMessage s){
        return upload(serializeMessage(s));
    }

    public static String upload(String s){
        try{
            URL url = new URL("http://paste.ubuntu.com");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setInstanceFollowRedirects(true);
            String postData = "content=" + URLEncoder.encode(s, "UTF-8") + "&poster=Skype Bot&syntax=text";
            con.setRequestProperty("Content-length", String.valueOf(postData.length()));
            con.setDoOutput(true);
            con.setDoInput(true);
            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(postData);
            output.close();
            con.getResponseCode();
            return con.getURL().toString();
        }catch(java.io.IOException ignored){
        }

        return null;
    }

    public static String getMD5Hash(String file){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Files.readAllBytes(Paths.get(file)));
            return Arrays.toString(md.digest());
        } catch(Exception ignored){
        }

        return null;
    }

    public static String getJarName(){
        return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
    }

    public static void restartBot(){
        R.s("/me " + R.version + " Restarting...");
        System.out.println("Restarting...");
        System.exit(0);
    }

    public static String resolveSkype(String name){
        try{
            URL url = new URL("http://resolvethem.com/index.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setInstanceFollowRedirects(true);
            String postData = "id=" + URLEncoder.encode(name, "UTF-8");
            con.setRequestProperty("Content-length", String.valueOf(postData.length()));
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.10 Safari/537.36");
            con.setDoOutput(true);
            con.setDoInput(true);
            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(postData);
            DataInputStream input = new DataInputStream(con.getInputStream());
            int c;
            StringBuilder resultBuf = new StringBuilder();
            while((c = input.read()) != -1){
                resultBuf.append((char) c);
            }
            input.close();
            String result = resultBuf.toString();
            Pattern p = Pattern.compile("<input type=\"text\" name=\"id\" value=\"(.*)\" placeholder=\"Skype Username\"/><br>");
            Matcher m = p.matcher(result);
            if(m.find()){
                return m.group(1);
            }else{
                return "IP Not Found";
            }
        }catch(Exception ignore){
        }

        return "IP Not Found";
    }

    public static String readFirstLine(String file){
        try{
            return Files.readAllLines(Paths.get(file)).get(0);
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }

}
