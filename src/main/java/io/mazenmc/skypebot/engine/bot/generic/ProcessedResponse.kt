package io.mazenmc.skypebot.engine.bot.generic

import com.samczsun.skype4j.chat.messages.ReceivedMessage

public class ProcessedResponse(template: String, val processor: CommandProcessor): StringResponse(template) {
    override fun process(message: ReceivedMessage): String {
        var response = super.process(message)

        if (response.equals("N/A"))
            return response // do not process if predicate failed

        processor.process(message, lastArgs.toTypedArray())
        return response
    }
}