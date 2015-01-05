package io.mazenmc.skypebot.engine.api;

import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseResource extends ServerResource {

    String jsonString = "";
    protected ResponseParseFactory parseFactory = null;

    @Get
    public Representation doGet() {
        parseFactory = new ResponseParseFactory();
        jsonString = "";
        try {
            Map json = getMapFromParam(getRequest().getResourceRef().getQueryAsForm());
            parseFactory = new ResponseParseFactory();
            jsonString = processRequest(null, new JSONObject(json), "get");

        } catch (Exception e) {
            jsonString = parseFactory.getFailureJsonString(e.getMessage());
        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    @Post("json")
    public Representation doPost(Representation entity) {
        jsonString = "";
        parseFactory = new ResponseParseFactory();
        try {
            String body = new Form(entity).toString();
            body = body.substring(2, body.length() - 2);
            JSONObject jsonobject = new JSONObject(body);
            String jsonText = jsonobject.toString();
            jsonString = processRequest(body, jsonobject, "post");
        } catch (Exception e) {
            e.printStackTrace();
            jsonString = parseFactory.getFailureJsonString(e.getMessage());
        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    public static Map<String, String> getMapFromParam(Form form) {
        Map<String, String> map = new HashMap<String, String>();
        for (Parameter parameter : form) {
            map.put(parameter.getName(), parameter.getValue());
        }
        return map;
    }

    public abstract String processRequest(String raw, JSONObject json, String method);

}