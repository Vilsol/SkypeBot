package me.vilsol.skypebot.engine;

public class ParameterData {

    private String name;
    private String regex;
    private boolean optional;

    public ParameterData(String name, String regex, boolean optional){
        this.name = name;
        this.regex = regex;
        this.optional = optional;
    }

    public String getName(){
        return name;
    }

    public String getRegex(){
        return regex;
    }

    public boolean isOptional(){
        return optional;
    }
}
