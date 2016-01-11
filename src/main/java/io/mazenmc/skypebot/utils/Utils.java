package io.mazenmc.skypebot.utils;

import com.google.common.base.Joiner;
import com.mashape.unirest.http.Unirest;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import io.mazenmc.skypebot.Main;
import io.mazenmc.skypebot.SkypeBot;
import io.mazenmc.skypebot.stat.Message;
import io.mazenmc.skypebot.stat.MessageStatistic;
import io.mazenmc.skypebot.stat.StatisticsManager;
import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class Utils {

    public static String generateSignature(String key, String payload) {
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA1");

        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException ignored) {
        }

        byte[] result = mac.doFinal(payload.getBytes());
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(result);
    }

    public static String getJarName() {
        return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
    }

    public static String getMD5Hash(String file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Files.readAllBytes(Paths.get(file)));
            return Arrays.toString(md.digest());
        } catch (Exception ignored) {
        }

        return null;
    }

    public static String getSha1(String data) {
        return DigestUtils.shaHex(data);
    }

    public static String getString(InputStream is) throws IOException {
        int ch;
        StringBuilder sb = new StringBuilder();
        while ((ch = is.read()) != -1) {
            sb.append((char) ch);
        }
        return sb.toString();
    }

    public static boolean isDouble(Object s) {
        try {
            Double.parseDouble(s.toString());
            return true;
        } catch (Exception ignore) {
        }

        return false;
    }

    public static boolean isInteger(Object s) {
        try {
            Integer.parseInt(s.toString());
            return true;
        } catch (Exception ignore) {
        }

        return false;
    }

    public static String parseResult(ResultSet rs) throws SQLException {
        if (rs == null || rs.isClosed()) {
            return null;
        }

        ArrayList<LinkedHashMap<String, String>> data = new ArrayList<>();
        LinkedHashMap<String, Integer> longest = new LinkedHashMap<>();

        ResultSetMetaData meta = rs.getMetaData();

        while (rs.next()) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            for (int c = 1; c <= meta.getColumnCount(); c++) {
                String col = meta.getColumnName(c);
                String val = rs.getString(c);

                if (col == null) {
                    col = "NULL";
                }

                if (val == null) {
                    val = "NULL";
                }

                if (!longest.containsKey(col) || longest.get(col) < col.length()) {
                    longest.put(col, col.length());
                }

                if (!longest.containsKey(col) || longest.get(col) < val.length()) {
                    longest.put(col, val.length());
                }

                map.put(col, val);
            }

            data.add(map);
        }

        String result = "";

        String nameBorder = "";
        String nameLine = "";
        for (Map.Entry<String, Integer> s : longest.entrySet()) {
            nameBorder += "+";
            for (int i = 0; i < s.getValue() + 2; i++) {
                nameBorder += "-";
            }

            if (nameLine.equals("")) {
                nameLine += "| " + s.getKey();
            } else {
                nameLine += " | " + s.getKey();
            }

            if (s.getKey().length() < s.getValue()) {
                for (int i = 0; i < s.getValue() - s.getKey().length(); i++) {
                    nameLine += " ";
                }
            }
        }
        nameBorder += "+";
        nameLine += " |";

        result += nameBorder + "\n" + nameLine + "\n" + nameBorder;

        if (data.size() > 0) {
            result += "\n";
        }

        for (LinkedHashMap<String, String> d : data) {
            String dataLine = "";
            for (Map.Entry<String, String> c : d.entrySet()) {
                if (dataLine.equals("")) {
                    dataLine += "| " + c.getValue();
                } else {
                    dataLine += " | " + c.getValue();
                }

                if (longest.get(c.getKey()) > c.getValue().length()) {
                    for (int i = 0; i < longest.get(c.getKey()) - c.getValue().length(); i++) {
                        dataLine += " ";
                    }
                }
            }
            dataLine += " |";
            result += dataLine + "\n";
        }

        result += nameBorder + "\n";
        result += data.size() + " rows in set.";

        return result;
    }

    public static String readFirstLine(String file) {
        try {
            return Files.readAllLines(Paths.get(file)).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> readAllLines(String file) {
        try {
            return Files.readAllLines(Paths.get(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void restartBot() {
        String displayName = getDisplayName(SkypeBot.getInstance().getEzSkype().getLocalUser());
        SkypeBot.getInstance().sendMessage("<b>" + displayName + "</b>" + Resource.VERSION + " Restarting...");
        StatisticsManager.instance().saveStatistics();
        System.out.println("Restarting...");

        try {
            Unirest.shutdown();
        } catch (IOException ignored) {
        }

        System.exit(0);
    }

    public static String serializeMessage(SkypeMessage message) {
        String s = "";
        String displayName = getDisplayName(message.getSender());

        try {
            s += "[" + new Date().toString() + "] " + displayName + ": " + message.getMessage();
        } catch (Exception ignored) {
        }

        return s;
    }

    public static Message lastSpoken(MessageStatistic statistic) {
        Message message = null;

        for (Message msg : statistic.messages()) {
            if (message == null) {
                message = msg;
                continue;
            }

            if (msg.time() > message.time()) {
                message = msg;
            }
        }

        return message;
    }

    public static Message firstSpoken(MessageStatistic statistic) {
        Message message = null;

        for (Message msg : statistic.messages()) {
            if (message == null) {
                message = msg;
                continue;
            }

            if (msg.time() < message.time()) {
                message = msg;
            }
        }

        return message;
    }

    public static <T> T optionalGet(Optional<T> optional) {
        return optional.isPresent() ? optional.get() : null;
    }

    public static String upload(Collection<String> s) {
        return upload(Joiner.on("\n").join(s));
    }

    public static String upload(String s) {
        try {
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
        } catch (java.io.IOException ignored) {
        }

        return null;
    }

    public static String getUrlSource(String urlInput) throws IOException {
        URL url = new URL(urlInput);
        URLConnection urlConnection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            a.append(inputLine);
        in.close();

        return a.toString();
    }

    public static SkypeUser getUser(String username) {
        return SkypeBot.getInstance().getEzSkype().getSkypeUser(username);
    }

    public static String getDisplayName(SkypeUser user) {
        Optional<String> displayOpt = user.getDisplayName();
        return displayOpt.isPresent() ? displayOpt.get() : user.getUsername();
    }
}
