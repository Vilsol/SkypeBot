package me.vilsol.skypebot.api;

import me.vilsol.skypebot.engine.api.BaseResource;
import me.vilsol.skypebot.engine.api.ResponseParseFactory;

import java.util.Map;

public class Ping extends BaseResource {

    @Override
    public String processRequest(Map json, String method){
        return new ResponseParseFactory().getSuccessJsonString("Hello!");
    }

}
