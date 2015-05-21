package io.mazenmc.skypebot.stat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MessageStatistic {
    private final List<String> messages;

    MessageStatistic() {
        messages = new ArrayList<>();
    }

    MessageStatistic(String[] messages) {
        this();

        Collections.addAll(this.messages, messages);
    }

    public int messageAmount() {
        return messages.size();
    }

    public String randomMessage() {
        return messages.get(ThreadLocalRandom.current().nextInt(messages.size()));
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public List<String> messages() {
        return messages;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MessageStatistic && messages.equals(((MessageStatistic) obj).messages);
    }
}
