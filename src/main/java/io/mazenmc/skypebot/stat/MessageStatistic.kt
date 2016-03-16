package io.mazenmc.skypebot.stat

import com.samczsun.skype4j.chat.messages.ReceivedMessage
import org.json.JSONArray
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.Collectors
import java.util.stream.IntStream

public class MessageStatistic(var name: String) {
    private var messages: MutableList<Message> = ArrayList<Message>()

    constructor(name: String, messages: JSONArray) : this(name) {
        IntStream.range(0, messages.length()).forEach { i ->
            var obj = messages.getJSONObject(i)
            var contents = obj.getString("contents")

            if (!contents.equals("<never sent message>")) {
                this.messages.add(Message(contents, obj.getLong("time")))
            }
        }

        if (this.messages.isEmpty()) {
            this.messages.add(Message("<never sent message>", System.currentTimeMillis()))
        }
    }

    public fun name(): String {
        return name
    }

    public fun words(): List<String> {
        var words = ArrayList<String>()

        messages.map { s -> s.contents.splitBy(" ") }.forEach { s -> s.forEach { w -> words.add(w) } }

        return words
    }

    public fun wordCount(): Int {
        return messages.map { m -> m.contents().splitBy(" ").size() }
                .filter { i -> i > 2 }
                .sum()
    }
    
    public fun letterCount(): Int {
        return messages.map { m -> m.contents().toCharList().filter { i -> i != ' ' }.size() }
                .sum()
    }
    
    public fun messageAmount(): Int {
        return messages.size()
    }
    
    public fun averageWords(): Int {
        return wordCount() / messageAmount()
    }
    
    public fun randomMessage(): Message {
        return messages.get(ThreadLocalRandom.current().nextInt(messageAmount()))
    }
    
    public fun commandCount(): Int {
        return messages.map(Message::contents)
                .filter { s -> s.startsWith("@") }
                .size()
    }

    public fun commandPercent(): Double {
        return (commandCount().toDouble() / messageAmount().toDouble()) * 100
    }

    public fun addMessage(message: ReceivedMessage) {
        addMessage(Message(message.getContent().asPlaintext(), System.currentTimeMillis()))
    }

    public fun addMessage(message: Message) {
        messages.add(message)
    }

    public fun messages(): List<Message> {
        return messages
    }
}