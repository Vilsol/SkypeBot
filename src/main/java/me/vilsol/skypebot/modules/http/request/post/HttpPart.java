package me.vilsol.skypebot.modules.http.request.post;

import java.util.HashMap;
import java.util.Map;

public abstract class HttpPart {

    private Map<String, String> contents = new HashMap<>();

    /**
     * Adds information to the contents of the part.
     *
     * @param keyValue A String with the key and value separated by ": " which will be separated put into the contents.
     */
    public void addContent(String keyValue) {
        addContent(keyValue.split(": ")[0], keyValue.split(": ")[1]);
    }

    /**
     * Adds information to the contents of the part.
     *
     * @param entry A map entry that is used to get the key and value, which will be put into the contents.
     */
    public void addContent(Map.Entry<String, String> entry) {
        addContent(entry.getKey(), entry.getValue());
    }

    /**
     * Adds information to the contents of the part.
     *
     * @param key   The key of the content.
     * @param value The value given to the key.
     */
    public void addContent(String key, Object value) {
        this.contents.put(key, value.toString());
    }

    /**
     * Gives the contents of the part.
     *
     * @return The contents Map.
     */
    public Map<String, String> getContents() {
        return contents;
    }
}
