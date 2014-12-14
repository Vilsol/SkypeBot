package me.vilsol.skypebot.modules.http;

import java.util.AbstractMap;
import java.util.Map;

public class Entry<K, V> extends AbstractMap.SimpleEntry<K, V> {

    /**
     * Simple AbstractMap.SimpleEntry wrapper.
     */

    public Entry(K key, V value) {
        super(key, value);
    }

    public Entry(Map.Entry<K, V> entry) {
        super(entry);
    }

    public static Entry<String, String> fromString(String string) {
        String[] values = string.split("=");
        return new Entry<>(values[0], values[1]);
    }

    public static <K, V> void addToMap(Map<K, V> map, Entry<K, V> entry) {
        map.put(entry.getKey(), entry.getValue());
    }

    @Override
    public String toString() {
        return getKey() + "=" + getValue();
    }
}
