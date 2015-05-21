package io.mazenmc.skypebot.stat;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class StatisticsManager {
    private static final StatisticsManager INSTANCE = new StatisticsManager();
    private Map<String, MessageStatistic> statistics = new HashMap<>();

    public static StatisticsManager instance() {
        return INSTANCE;
    }

    public void logMessage(ChatMessage message) throws SkypeException {
        if (!statistics.containsKey(message.getSenderId()))
            statistics.put(message.getSenderId(), new MessageStatistic());

        try {
            statistics.get(message.getSenderId()).addMessage(message.getContent());
        } catch (SkypeException ex) {
            ex.printStackTrace();
        }
    }

    public String ownerFor(MessageStatistic statistic) {
        for (Map.Entry<String, MessageStatistic> entry : statistics.entrySet()) {
            if (entry.getValue().equals(statistic))
                return entry.getKey();
        }

        return null;
    }

    public Map<String, MessageStatistic> statistics() {
        return statistics;
    }

    public void loadStatistics() {
        StringBuilder source = new StringBuilder();

        try {
            for (String s : Files.readAllLines(Paths.get("statistics.json"))) {
                source.append(s)
                        .append("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            JSONObject object = new JSONObject(source.toString());

            object.keys().forEachRemaining((obj) -> {
                if (!(obj instanceof String)) {
                    return;
                }

                String key = (String) obj;

                try {
                    statistics.put(key, new MessageStatistic(object.getJSONArray(key)));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void saveStatistics() {
        JSONObject object = new JSONObject();

        for (Map.Entry<String, MessageStatistic> entry : statistics.entrySet()) {
            try {
                object.put(entry.getKey(), new JSONArray(entry.getValue().messages()));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

        try {
            Files.write(Paths.get("statistics.json"), object.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ignored) {
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
