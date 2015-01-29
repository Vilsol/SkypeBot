package io.mazenmc.skypebot.modules;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;
import io.mazenmc.skypebot.utils.Utils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spotify implements Module {
    @Command(name = "open.spotify.com/track/", command = false, exact = false)
    public static void cmdSpotify(ChatMessage chat) throws SkypeException, IOException {
        String wholeMessage = chat.getContent();
        Matcher idMatcher = Resource.SPOTIFY_REGEX.matcher(wholeMessage);

        String spotifyURI = null;

        while (idMatcher.find()) {
            spotifyURI = idMatcher.group();
        }

        if (spotifyURI == null) {
            Resource.sendMessage(chat, "Invalid Spotify ID!");
        } else {
            // TODO: Do this properly
            StringBuilder songMetaData = new StringBuilder();
            String spotifyID = spotifyURI.substring(spotifyURI.lastIndexOf('/') + 1, spotifyURI.length());
            String response = Utils.getUrlSource("https://ws.spotify.com/lookup/1/?uri=http://open.spotify.com/track/" + spotifyID);

            // If it works but it is stupid, it is still stupid.
            Matcher songMetadataMatcher = Pattern.compile("<name>(.*?)</name>").matcher(response);

            while (songMetadataMatcher.find()) {
                String metadataItem = songMetadataMatcher.group(1);

                if (!songMetaData.toString().isEmpty()) {
                    songMetaData.append(" - ");
                }

                songMetaData.append(metadataItem);
            }

            Resource.sendMessage(chat, songMetaData.toString());
        }
    }
}
    