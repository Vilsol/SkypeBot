package me.vilsol.skypebot.engine.api;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseResource extends ServerResource {

    protected ResponseParseFactory parseFactory = null;

    String jsonString = "";

    @Post("json")
    public Representation doPost(Representation entity){
        jsonString = "";
        parseFactory = new ResponseParseFactory();
        try{
            JsonRepresentation represent = new JsonRepresentation(entity);
            JSONObject jsonobject = represent.getJsonObject();
            JSONParser parser = new JSONParser();
            String jsonText = jsonobject.toString();
            Map json = (Map) parser.parse(jsonText);
            jsonString = processRequest(json, "post");
        }catch(Exception e){
            jsonString = parseFactory.getFailureJsonString(e.getMessage());
        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    @Get
    public Representation doGet(){
        parseFactory = new ResponseParseFactory();
        jsonString = "";
        try{
            Map json = getMapFromParam(getRequest().getResourceRef().getQueryAsForm());
            parseFactory = new ResponseParseFactory();
            jsonString = processRequest(json, "get");

        }catch(Exception e){
            jsonString = parseFactory.getFailureJsonString(e.getMessage());
        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    public abstract String processRequest(Map json, String method);

    public static Map<String, String> getMapFromParam(Form form){
        Map<String, String> map = new HashMap<String, String>();
        for(Parameter parameter : form){
            map.put(parameter.getName(), parameter.getValue());
        }
        return map;
    }

}