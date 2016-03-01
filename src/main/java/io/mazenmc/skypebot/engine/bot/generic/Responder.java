package io.mazenmc.skypebot.engine.bot.generic;

import com.samczsun.skype4j.chat.messages.ReceivedMessage;

@FunctionalInterface
public interface Responder {
    String process(ReceivedMessage message, String[] args);
}
