package io.mazenmc.skypebot.modules;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.samczsun.skype4j.chat.messages.ReceivedMessage;
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

public class Wikipedia implements Module{

    String api = "";

    @Command(name = "en.wikipedia.org/wiki/", command = false, exact = false)
    public static void cmdWikipedia(ReceivedMessage chat) throws Exception {
        String wholeMessage = chat.getContent().asPlaintext();
        Matcher idMatcher = Resource.WIKIPEDIA_REGEX.matcher(wholeMessage);

        String articleSlug = null;

        if (idMatcher.find()) {
            articleSlug = idMatcher.group(1);
        }

        if (articleSlug == null) {
            Resource.sendMessage(chat, "Invalid Wikipedia URL!");
            return;
        }

        Resource.sendMessage(chat, getWikipediaSnip(articleSlug.split(" ")[0]));
    }

    public static String getWikipediaSnip(String slug) throws UnirestException, JSONException {
        JSONObject json = Unirest.get("http://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exsentences=10&titles=" + slug + "&redirects=true&continue=&explaintext=")
                .asJson().getBody().getObject().getJSONObject("query").getJSONObject("pages");

        String articleKey = "";
        Iterator<String> keys = json.keys();

        if( keys.hasNext() ){
            articleKey = keys.next();
        }

        String snippet = "Error finding Wikipedia article.";

        if (!articleKey.equals("-1")) {
            snippet = "*" + json.getJSONObject(articleKey).getString("title") + "*\n" + json.getJSONObject(articleKey).getString("extract").split("\n")[0];
        }

        return snippet;
    }

}
