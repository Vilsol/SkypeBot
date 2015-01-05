package me.vilsol.skypebot.api;

import me.vilsol.skypebot.engine.api.BaseResource;
import me.vilsol.skypebot.engine.api.Path;
import me.vilsol.skypebot.engine.api.ResponseParseFactory;
import me.vilsol.skypebot.utils.R;
import org.json.JSONException;
import org.json.JSONObject;

public class Github extends BaseResource {

    @Path("/github")
    public String processRequest(String raw, JSONObject json, String method) {
        if (!getQuery().getValues("key").equals(R.KEY_GITHUB)) {
            return new ResponseParseFactory().getFailureJsonString("Invalid Key");
        }

        String out = "";

        try {
            out += "[Commit] " + json.getJSONArray("commits").getJSONObject(0).getJSONObject("committer").get("name");
            out += " '" + json.getJSONArray("commits").getJSONObject(0).get("message") + "' ";
            out += " (" + json.getJSONArray("commits").getJSONObject(0).get("url") + ")";
        } catch (JSONException ignored) {
        }

        R.s(out);

        return new ResponseParseFactory().getSuccessJsonString("Success");
    }

}
