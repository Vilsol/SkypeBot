package io.mazenmc.skypebot.modules;

import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class FuckingWeather implements Module {

    //Imperial because fuck Celsius right?
    private static String url = "http://api.openweathermap.org/data/2.5/weather?units=imperial&q=";

    @Command(name = "fuckingweather")
    public static void cmdFuckingWeather(SkypeMessage chat, String location) {
        try {
            Resource.sendMessage(chat, getWeather(location));
        } catch (Exception e) {
            Resource.sendMessage(chat, "THE FUCKING WEATHER MODULE FAILED FUCK!");
            e.printStackTrace();
        }
    }

    public static String getWeather(String location) throws JSONException, IOException {
        String call = (url + location).replace(' ', '+');
        JSONObject json = new JSONObject(sendGet(call));

        if (json.getInt("cod") != 200) {
            return "I CAN'T GET THE FUCKING WEATHER!";
        }

        double temp = Math.round(json.getJSONObject("main").getDouble("temp"));
        double metric = Math.round((temp - 32) / 1.8000);

        if (temp <= 32) {
            Resource.sendMessage("ITS FUCKING FREEZING!");
        } else if (temp >= 33 && temp <= 60) {
            Resource.sendMessage("ITS FUCKING COLD!");
        } else if (temp >= 61 && temp <= 75) {
            Resource.sendMessage("ITS FUCKING NICE!");
        } else {
            Resource.sendMessage("ITS FUCKING HOT");
        }

        return "THE FUCKING WEATHER IN " + location.toUpperCase() + " IS " + temp + "F | " + metric + "C";
    }

    public static String sendGet(String url) throws IOException{
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString().trim();
    }

}
