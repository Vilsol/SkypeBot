package me.vilsol.skypebot.modules.http.cookie;

import me.vilsol.skypebot.modules.http.Entry;

import java.util.HashMap;
import java.util.Map;

public class Cookie {

    private Map<String, String> values = new HashMap<>();
    private String name;
    private String val;

    public Cookie(Entry<String, String> entry) {
        registerCookie(entry.getValue());
    }

    public Cookie(String value) {
        registerCookie(value);
    }

    public Cookie() {
    }

    /**
     * Gets the content or value to the cookie.
     *
     * @return Content of the cookie.
     */
    public String getContent() {
        return this.values.get(name);
    }

    /**
     * Sets the content or value to the cookie.
     *
     * @param value The value to set the content to.
     * @return The cookie object for further building.
     */
    public Cookie setContent(String value) {
        this.values.put(name, value);
        return this;
    }

    /**
     * Adds a value to the cookie.
     *
     * @param key The key of the value.
     * @param value The value to add.
     * @return The cookie object for further building.
     */
    public Cookie addValue(String key, String value) {
        this.values.put(key, value);
        return this;
    }

    /**
     * Adds a value to the cookie.
     *
     * @param keyVal The key and the value to add separated by a "=".
     * @return The cookie object for further building.
     */
    public Cookie addValue(String keyVal) {
        String[] strings = keyVal.split("=");
        if (strings.length == 1) this.values.put(strings[0], null);
        else this.values.put(strings[0], strings[1]);
        return this;
    }

    /**
     * Sets all values to the cookie, including the content.
     *
     * @param string The cookie in string form.
     */
    public void registerCookie(String string) {
        this.val = string;
        String[] strings = string.split("; ");
        setName1(strings[0]);
        for (String string1 : strings) addValue(string1);
    }

    /**
     * Sets the name of the cookie.
     *
     * @param string The value to set the cookie name to.
     */
    private void setName1(String string) {
        String[] strings = string.split("=");
        this.name = strings[0];
    }

    /**
     * Gets the name of the cookie.
     *
     * @return The name of the cookie.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the cookie directly.
     *
     * @param name The value to set the cookie name to.
     * @return The cookie object for further building.
     */
    public Cookie setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets values of the cookie.
     *
     * @return Map of the values of the cookie.
     */
    public Map<String, String> getValues() {
        return this.values;
    }

    @Override
    public String toString() {
        return val;
    }

}
