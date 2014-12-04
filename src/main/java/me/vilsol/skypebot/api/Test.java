package me.vilsol.skypebot.api;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Test extends ServerResource {

    @Get
    public String toString()	{
        return "hello,	world";
    }

}
