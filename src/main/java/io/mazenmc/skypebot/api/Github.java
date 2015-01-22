package io.mazenmc.skypebot.api;

import io.mazenmc.skypebot.engine.api.BaseResource;
import io.mazenmc.skypebot.engine.api.Path;
import io.mazenmc.skypebot.engine.api.ResponseParseFactory;
import io.mazenmc.skypebot.utils.Resource;
import org.json.JSONException;
import org.json.JSONObject;

public class Github extends BaseResource {

    @Path("/github")
    public String processRequest(String raw, JSONObject json, String method) {
        if (!getQuery().getValues("key").equals(Resource.KEY_GITHUB)) {
            return new ResponseParseFactory().getFailureJsonString("Invalid Key");
        }

        String out = "";

        try {
            out += "[Commit] " + json.getJSONArray("commits").getJSONObject(0).getJSONObject("committer").get("name");
            out += " '" + json.getJSONArray("commits").getJSONObject(0).get("message") + "' ";
            out += " (" + json.getJSONArray("commits").getJSONObject(0).get("url") + ")";
        } catch (JSONException ignored) {
        }

        Resource.sendMessage(out);

        return new ResponseParseFactory().getSuccessJsonString("Success");
    }

}
