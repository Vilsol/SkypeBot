package io.mazenmc.skypebot.stat;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MessageStatistic {
    private final String name;
    private final List<Message> messages;

    MessageStatistic(String name) {
        this.name = name;
        messages = new ArrayList<>();
    }

    MessageStatistic(String name, JSONArray messages) {
        this(name);

        IntStream.range(0, messages.length()).forEach((i) -> {
            try {
                JSONObject object = messages.getJSONObject(i);

                this.messages.add(new Message(object.getString("contents"), object.getLong("time")));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        });

        if (this.messages.isEmpty()) {
            this.messages.add(new Message("<never sent message>", System.currentTimeMillis()));
        }
    }

    public String name() {
        return name;
    }

    public List<String> words() {
        List<String> words = new ArrayList<>();

        messages.stream()
                .map((s) -> s.contents().split(" "))
                .filter((s) -> !s.trim().isEmpty())
                .forEach((s) -> words.addAll(Arrays.asList(s)));

        return words;
    }

    public String mostCommonWord() {
        return words().stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet().stream()
                .sorted((e, e1) -> (int) (e1.getValue() - e.getValue()))
                .findFirst().get()
                .getKey();
    }

    public int wordCount() {
        return messages.stream()
                .mapToInt((m) -> m.contents().split(" ").length)
                .filter(i -> i < 50 && i > 2)
                .sum();
    }

    public double averageWords() {
        return wordCount() / messageAmount();
    }

    public int messageAmount() {
        return messages.size();
    }

    public Message randomMessage() {
        return messages.get(ThreadLocalRandom.current().nextInt(messages.size()));
    }

    public int commandCount() {
        return (int) messages.stream()
                .map(Message::contents)
                .filter((s) -> s.startsWith("@"))
                .count();
    }

    public double commandPercent() {
        return ((double) commandCount() / (double) messageAmount()) * 100;
    }

    public void addMessage(ChatMessage message) throws SkypeException {
        messages.add(new Message(message.getContent(), message.getTime().getTime()));
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> messages() {
        return messages;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MessageStatistic && messages.equals(((MessageStatistic) obj).messages);
    }
}
