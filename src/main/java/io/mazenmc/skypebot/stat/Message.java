package io.mazenmc.skypebot.stat;

public class Message {
    private String contents;
    private long time;

    public Message(String contents, long time) {
        this.contents = contents;
        this.time = time;
    }

    public String contents() {
        return contents;
    }

    public long time() {
        return time;
    }
}
