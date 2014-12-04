package me.vilsol.skypebot.api;

import me.vilsol.skypebot.engine.api.BaseResource;
import me.vilsol.skypebot.engine.api.Path;
import me.vilsol.skypebot.engine.api.ResponseParseFactory;
import me.vilsol.skypebot.utils.R;
import org.json.JSONObject;

public class Github extends BaseResource {

    @Path("/github")
    public String processRequest(JSONObject json, String method){
        if(!json.has("hook") || !json.getJSONObject("hook").has("config") || !json.getJSONObject("hook").getJSONObject("config").has("secret")){
            return new ResponseParseFactory().getFailureJsonString("Invalid Secret!");
        }

        if(!json.getJSONObject("hook").getJSONObject("config").getString("secret").equals(R.KEY_GITHUB)){
            return new ResponseParseFactory().getFailureJsonString("Invalid Secret!");
        }

        System.out.println(json);

        return new ResponseParseFactory().getSuccessJsonString("Hello!");
    }

}
