package io.mazenmc.skypebot.stat;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class MessageStatistic {
    private final List<String> messages;

    MessageStatistic() {
        messages = new ArrayList<>();
    }

    MessageStatistic(JSONArray messages) {
        this();

        IntStream.range(0, messages.length()).forEach((i) -> {
            try {
                this.messages.add(messages.getString(i));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        });
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
