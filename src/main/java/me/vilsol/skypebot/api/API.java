package me.vilsol.skypebot.api;

import me.vilsol.skypebot.engine.api.BaseResource;
import me.vilsol.skypebot.engine.api.Path;
import org.reflections.Reflections;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import java.lang.reflect.Method;
import java.util.Map;

public class API extends Application {

    public API(){
        setName("SkypeBot API");
    }

    @Override
    public Restlet createInboundRoot() {
        Router baseRouter = new Router(getContext());

        Reflections r = new Reflections("me.vilsol.skypebot.api");
        for(Class<? extends BaseResource> c : r.getSubTypesOf(BaseResource.class)){
            try{
                Method m = c.getMethod("processRequest", Map.class, String.class);
                Path p = m.getAnnotation(Path.class);
                if(p != null){
                    baseRouter.attach(p.value(), c);
                }
            }catch(NoSuchMethodException ignored){
            }
        }

        return baseRouter;
    }

}
