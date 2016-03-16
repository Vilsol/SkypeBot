package io.mazenmc.skypebot

import com.samczsun.skype4j.events.EventHandler
import com.samczsun.skype4j.events.Listener
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent
import com.samczsun.skype4j.events.chat.sent.PictureReceivedEvent
import com.samczsun.skype4j.formatting.Message
import com.samczsun.skype4j.formatting.Text
import io.mazenmc.skypebot.engine.bot.ModuleManager
import io.mazenmc.skypebot.stat.StatisticsManager
import io.mazenmc.skypebot.utils.Utils
import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

public class SkypeEventListener: Listener {
    EventHandler
    public fun onMessage(event: MessageReceivedEvent) {
        ModuleManager.parseText(event.getMessage())
        StatisticsManager.logMessage(event.getMessage())
    }

    EventHandler
    public fun onImage(event: PictureReceivedEvent) {
        var file = File("lastImage.png")

        if (file.exists()) {
            file.delete()
        }

        try {
            if (!SkypeBot.groupConv().getAllUsers().any { u -> u.getUsername().equals(event.getSender().getUsername()) }) {
                return; // what you doin? random tryin' to send pics. I dun want your nudes bruh.
            }

            ImageIO.write(event.getSentImage(), "png", file)
            event.getChat().sendMessage(Message.create().with(Text.rich("Uploading image...").withColor(Color.CYAN)))

            var link = Utils.upload(file)

            event.getChat().sendMessage(Message.create().with())
        }
    }
}