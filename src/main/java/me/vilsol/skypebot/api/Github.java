package me.vilsol.skypebot.api;

import me.vilsol.skypebot.engine.api.BaseResource;
import me.vilsol.skypebot.engine.api.Path;
import me.vilsol.skypebot.engine.api.ResponseParseFactory;
import org.json.JSONObject;

public class Github extends BaseResource {

    @Path("/github")
    public String processRequest(String raw, JSONObject json, String method) {

        // Unless someone finds a way to validate a github payload, then it will be impossible to re-enable

        return new ResponseParseFactory().getFailureJsonString("Temporarily Disabled!");

        /*
        if(getRequestAttributes().get("org.restlet.http.headers") == null){
            return new ResponseParseFactory().getFailureJsonString("Invalid Secret!");
        }

        String key = ((Form) getRequestAttributes().get("org.restlet.http.headers")).getValues("X-Hub-Signature");
        String generated = "sha1=" + Utils.generateSignature(R.KEY_GITHUB, getRequest().toString());

        System.out.println(key);
        System.out.println(generated);

        if(!generated.equals(key)){

        }

        String out = "";
        out += "[Commit] " + json.getJSONArray("commits").getJSONObject(0).getJSONObject("committer").get("name");
        out += " '" + json.getJSONArray("commits").getJSONObject(0).get("message") + "' ";
        out += " (" + json.getJSONArray("commits").getJSONObject(0).get("url") + ")";

        R.s(out);

        return new ResponseParseFactory().getSuccessJsonString("Success");*/
    }

}
