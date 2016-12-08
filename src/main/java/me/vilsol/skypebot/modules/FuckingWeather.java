package me.vilsol.skypebot.modules;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import me.vilsol.skypebot.engine.bot.Command;
import me.vilsol.skypebot.engine.bot.Module;
import me.vilsol.skypebot.engine.bot.Optional;
import me.vilsol.skypebot.utils.R;

import org.json.JSONException;
import org.json.JSONObject;

import com.skype.ChatMessage;

/*
 * @author mattrick
 *
 * A fun little thing to get the weather....and curse.
 *
 * Powered by OpenWeatherMap (http://openweathermap.org/)
 *
 */

public class FuckingWeather implements Module {

	private static String url = "http://api.openweathermap.org/data/2.5/weather?units=imperial&q=";

	@Command(name = "fuckingweather", alias={"fw"})
	public static void cmdFW(ChatMessage message, @Optional final String location) {
		if (location == null ) {
			R.s("GIVE ME A FUCKING LOCATION!");
		}
		else {
			try {
				getWeather(location);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String getWeather(String location) throws JSONException, Exception {
		String call = (url + location).replace(' ', '+');
		JSONObject json = new JSONObject(sendGet(call));

		if (json.getString("cod").equals("404")) {
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
