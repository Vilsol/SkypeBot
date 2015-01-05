package me.vilsol.skypebot.api;

import me.vilsol.skypebot.engine.api.BaseResource;
import me.vilsol.skypebot.engine.api.Path;
import me.vilsol.skypebot.engine.api.ResponseParseFactory;
import me.vilsol.skypebot.utils.R;
import org.json.JSONObject;
import org.restlet.data.Form;

public class Trident extends BaseResource {

    @Path("/trident")
    public String processRequest(String raw, JSONObject json, String method) {
        if (!getQuery().getValues("key").equals(R.KEY_TRIDENT)) {
            return new ResponseParseFactory().getFailureJsonString("Invalid Key");
        }

        Form a = (Form) getRequestAttributes().get("org.restlet.http.headers");
        String out = "[Post] " + a.getValues("user") + ": " + a.getValues("title") + " (" + a.getValues("url") + ")";
        R.s(out);

        return new ResponseParseFactory().getSuccessJsonString("Success");
    }

}
