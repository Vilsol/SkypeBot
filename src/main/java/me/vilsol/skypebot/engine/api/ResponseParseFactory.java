package me.vilsol.skypebot.engine.api;

import org.json.simple.JSONValue;

import java.util.LinkedHashMap;

public class ResponseParseFactory {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getFailureJsonString(Object msg) {
        LinkedHashMap list = new LinkedHashMap();
        list.put("success", "false");
        list.put("result", msg.toString());
        return JSONValue.toJSONString(list);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getSuccessJsonString(Object msg) {
        LinkedHashMap list = new LinkedHashMap();
        list.put("success", "true");
        list.put("result", msg.toString());
        return JSONValue.toJSONString(list);
    }
}