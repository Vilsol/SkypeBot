package me.vilsol.skypebot.api;

import me.vilsol.skypebot.engine.api.BaseResource;
import me.vilsol.skypebot.engine.api.Path;
import me.vilsol.skypebot.engine.api.ResponseParseFactory;
import me.vilsol.skypebot.utils.R;
import org.json.JSONObject;
import org.restlet.data.Form;

public class Github extends BaseResource {

    @Path("/github")
    public String processRequest(JSONObject json, String method){
        if(getRequestAttributes().get("org.restlet.http.headers") == null || !((Form) getRequestAttributes().get("org.restlet.http.headers")).getValues("X-Hub-Signature").equals(R.KEY_GITHUB)){
            return new ResponseParseFactory().getFailureJsonString("Invalid Secret!");
        }

        String out = "";
        out += "[Commit] " + json.getJSONArray("commits").getJSONObject(0).getJSONObject("committer").get("name");
        out += " '" + json.getJSONArray("commits").getJSONObject(0).get("message") + "' ";
        out += " (" + json.getJSONArray("commits").getJSONObject(0).get("url") + ")";

        R.s(out);

        return new ResponseParseFactory().getSuccessJsonString("Success");
    }

}
