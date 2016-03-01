package io.mazenmc.skypebot.engine.bot.generic

import com.samczsun.skype4j.chat.messages.ReceivedMessage

public class MethodResponse(val responder: Responder): StringResponse("N/A") {
    override fun process(message: ReceivedMessage): String {
        return responder.process(message, lastArgs.copyToArray())
    }
}