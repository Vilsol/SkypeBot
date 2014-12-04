package me.vilsol.skypebot.api;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Ping extends ServerResource {

    @Get("txt")
    public String represent() {
        return "HELLO";
    }

}
