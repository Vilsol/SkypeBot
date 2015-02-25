package io.mazenmc.skypebot.modules;

import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.utils.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.skype.ChatMessage;

public class FuckingWeather implements Module {

	//Imperial because fuck Celsius right?
	private static String url = "http://api.openweathermap.org/data/2.5/weather?units=imperial&q=";

	@Command(name = "fuckingweather")
	public static void cmdFuckingWeather(ChatMessage chat, String location) {
		try {
			Resource.sendMessage(chat, getWeather(location));
		} catch (Exception e) {
			Resource.sendMessage(chat, "THE FUCKING WEATHER MODULE FAILED FUCK!");
			e.printStackTrace();
		}
	}

	public static String getWeather(String location) throws JSONException, Exception {
		String call = (url + location).replace(' ', '+');
		JSONObject json = new JSONObject(sendGet(call));

		if (json.getInt("cod") != 200 || json == null) {
			return "I CAN'T GET THE FUCKING WEATHER!";
		}

		double temp = json.getJSONObject("main").getDouble("temp");

		if (temp <= 32) {
			return "ITS FUCKING FREEZING!";
		}
		else if (temp >= 33 && temp <= 60) {
			return "ITS FUCKING COLD!";
		}
		else if (temp >= 61 && temp <= 75) {
			return "ITS FUCKING NICE!";
		}

		return "ITS FUCKING HOT!";
	}

	public static String sendGet(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

}
