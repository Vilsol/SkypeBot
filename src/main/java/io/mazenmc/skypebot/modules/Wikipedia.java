package io.mazenmc.skypebot.modules;

import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.Matcher;

import org.json.JSONException;
import org.json.JSONObject;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public class Wikipedia implements Module{

    String api = "";

    @Command(name = "en.wikipedia.org/wiki/", command = false, exact = false)
    public static void cmdWikipedia(ChatMessage chat) throws IOException, JSONException, SkypeException{
        String wholeMessage = chat.getContent();
        Matcher idMatcher = Resource.WIKIPEDIA_REGEX.matcher(wholeMessage);

        String articleSlug = null;

        if (idMatcher.find()) {
            articleSlug = idMatcher.group(1);
        }

        if (articleSlug == null) {
            Resource.sendMessage(chat, "Invalid Wikipedia URL!");
            return;
        }
        Resource.sendMessage(chat, getWikipediaSnip(articleSlug));
    }

    public static String getWikipediaSnip(String slug) throws IOException, JSONException{
        URL obj = new URL("http://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exsentences=10&titles=" + slug + "&redirects=true&continue=&explaintext=");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(response.toString()).getJSONObject("query").getJSONObject("pages");

        String articleKey = "";
        Iterator<String> keys = json.keys();

        if( keys.hasNext() ){
            articleKey = keys.next();
        }

        String snippet = "Error finding Wikipedia article.";

        if (!articleKey.equals("-1")) {
            snippet = "*" + json.getJSONObject(articleKey).getString("title") + "*\n" + json.getJSONObject(articleKey).getString("extract");
        }

        return snippet;
    }

}
