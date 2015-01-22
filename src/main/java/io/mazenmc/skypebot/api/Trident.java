package io.mazenmc.skypebot.api;

import io.mazenmc.skypebot.engine.api.BaseResource;
import io.mazenmc.skypebot.engine.api.Path;
import io.mazenmc.skypebot.engine.api.ResponseParseFactory;
import io.mazenmc.skypebot.utils.Resource;
import org.json.JSONObject;
import org.restlet.data.Form;

public class Trident extends BaseResource {

    @Path("/trident")
    public String processRequest(String raw, JSONObject json, String method) {
        if (!getQuery().getValues("key").equals(Resource.KEY_TRIDENT)) {
            return new ResponseParseFactory().getFailureJsonString("Invalid Key");
        }

        Form a = (Form) getRequestAttributes().get("org.restlet.http.headers");
        String out = "[Post] " + a.getValues("user") + ": " + a.getValues("title") + " (" + a.getValues("url") + ")";
        Resource.sendMessage(out);

        return new ResponseParseFactory().getSuccessJsonString("Success");
    }

}
