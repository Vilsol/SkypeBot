package me.vilsol.skypebot.api;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class API extends Application {

    public API(){
        setName("SkypeBot API");
    }

    @Override
    public Restlet createInboundRoot() {
        Router baseRouter = new Router(getContext());
        baseRouter.attach("/", Ping.class);
        return baseRouter;
    }

}
