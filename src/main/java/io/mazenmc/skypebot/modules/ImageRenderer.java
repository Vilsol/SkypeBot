package io.mazenmc.skypebot.modules;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;
import io.mazenmc.skypebot.utils.Utils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageRenderer implements Module {

    @Command(name = "^https?://(?:[a-z\\-]+\\.)+[a-z]{2,6}(?:/[^/#?]+)+\\.(?:jpg|gif|png)$", command = false, exact = false)
    public static void imageRender(ChatMessage chat) throws SkypeException, UnirestException {
        String message = chat.getContent();

        if(message.split(" ").length != 1) {
            return;
        }

        try {
            BufferedImage image = ImageIO.read(Unirest.get(chat.getContent()).asBinary().getBody());
            Resource.sendMessage("Image: " + Utils.upload(ASCII.convert(image)));
        } catch (IOException e) {
            Resource.sendMessage("Failed " + Utils.upload(ExceptionUtils.getMessage(e)));
        }
    }

    private static final class ASCII {

        public static String convert(final BufferedImage image) {
            StringBuilder sb = new StringBuilder((image.getWidth() + 1) * image.getHeight());

            for (int y = 0; y < image.getHeight(); y++) {
                if (sb.length() != 0) sb.append("\n");

                for (int x = 0; x < image.getWidth(); x++) {
                    Color pixelColor = new Color(image.getRGB(x, y));
                    double gValue = (double) pixelColor.getRed() * 0.2989 + (double) pixelColor.getBlue() * 0.5870 + (double) pixelColor.getGreen() * 0.1140;
                    final char s = returnStrPos(gValue);

                    sb.append(s);
                }
            }

            return sb.toString();
        }

        /**
         * Create a new string and assign to it a string based on the grayscale value.
         * If the grayscale value is very high, the pixel is very bright and assign characters
         * such as . and , that do not appear very dark. If the grayscale value is very lowm the pixel is very dark,
         * assign characters such as # and @ which appear very dark.
         *
         * @param g grayscale
         * @return char
         */
        private static char returnStrPos(double g) {
            final char str;

            if (g >= 230.0) {
                str = ' ';
            } else if (g >= 200.0) {
                str = '.';
            } else if (g >= 180.0) {
                str = '*';
            } else if (g >= 160.0) {
                str = ':';
            } else if (g >= 130.0) {
                str = 'o';
            } else if (g >= 100.0) {
                str = '&';
            } else if (g >= 70.0) {
                str = '8';
            } else if (g >= 50.0) {
                str = '#';
            } else {
                str = '@';
            }
            return str; // return the character

        }
    }
}
