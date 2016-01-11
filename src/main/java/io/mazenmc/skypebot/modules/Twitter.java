package io.mazenmc.skypebot.modules;

import xyz.gghost.jskype.message.Message;
import io.mazenmc.skypebot.SkypeBot;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.regex.Matcher;

public class Twitter implements Module {
    @Command(name = "twitter\\.com\\/[A-z0-9]+\\/status\\/[0-9]{18}", command = false, exact = false)
    public static void cmdTwitter(Message chat) throws Exception {
        String wholeMessage = chat.getMessage();
        Matcher idMatcher = Resource.TWITTER_REGEX.matcher(wholeMessage);

        String tweetID = null;

        if (idMatcher.find()) {
            tweetID = idMatcher.group(2);
        }

        if (tweetID == null) {
            Resource.sendMessage(chat, "Invalid Twitter URL!");
            return;
        }

        try {
            Status status = SkypeBot.getInstance().getTwitter().tweets().showStatus(Long.parseLong(tweetID));
            Resource.sendMessage(chat, status.getUser().getName() + ": " + status.getText());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
    