package me.vilsol.skypebot.modules.http.headers;

import me.vilsol.skypebot.modules.http.Entry;

public class Header {

    private Entry<String, String> header;

    public Header(Entry<String, String> header) {
        this.header = header;
    }

    public Header(String key, String value) {
        this.header = new Entry<>(key, value);
    }

    public Header(String keyVal) {
        String[] keyValSepar = keyVal.split(":");
        this.header = new Entry<>(keyValSepar[0], keyValSepar[1]);
    }

    @Override
    public String toString() {
        return this.header.getKey() + ": " + this.header.getValue();
    }

    public Entry<String, String> getHeader() {
        return this.header;
    }
}
