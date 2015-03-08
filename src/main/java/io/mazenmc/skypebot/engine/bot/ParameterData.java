package io.mazenmc.skypebot.engine.bot;

public class ParameterData {

    private String name;
    private boolean optional;
    private String regex;

    public ParameterData(String name, String regex, boolean optional) {
        this.name = name;
        this.regex = regex;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }

    public boolean isOptional() {
        return optional;
    }
}
