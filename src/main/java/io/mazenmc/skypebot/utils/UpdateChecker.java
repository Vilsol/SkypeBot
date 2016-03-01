package io.mazenmc.skypebot.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import io.mazenmc.skypebot.SkypeBot;
import io.mazenmc.skypebot.stat.StatisticsManager;
import net.lingala.zip4j.core.ZipFile;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UpdateChecker extends Thread {

    private String lastSha = "--";
    private String accessToken;

    public UpdateChecker() {
        accessToken = Utils.readFirstLine("key_github");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }

            try {
                HttpResponse<JsonNode> response = Unirest.get("https://api.github.com/repos/mkotb/SkypeBot/commits?page=1" +
                        "&access_token=" + accessToken)
                        .header("User-Agent", "Mazen-SkypeBot")
                        .header("Content-Type", "application/json")
                        .asJson();

                JsonNode node = response.getBody();
                JSONObject recentCommit = node.getArray().getJSONObject(0);
                String sha = recentCommit.getString("sha");

                if (!lastSha.equals(sha) && !lastSha.equals("--")) {
                    URL url = new URL("https://github.com/mkotb/SkypeBot/archive/master.zip");
                    HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
                    JSONObject commit = recentCommit.getJSONObject("commit");

                    Resource.sendMessage("Found new commit: " +
                            commit.getJSONObject("author").getString("name") + " - " +
                            commit.getString("message") + " (" + sha + ")");
                    Resource.sendMessage(recentCommit.getString("html_url"));

                    try (InputStream stream = c.getInputStream()) {
                        File f = new File("master.zip");

                        if (f.exists())
                            f.delete();

                        Files.copy(stream, Paths.get("master.zip"));
                        stream.close();
                    }

                    File output = new File("SkypeBot-master");

                    if (output.exists())
                        output.delete();

                    ZipFile zip = new ZipFile(new File("master.zip"));

                    zip.extractAll(System.getProperty("user.dir"));

                    Resource.sendMessage("Restarting...");

                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException ignored) {
                    }

                    StatisticsManager.instance().saveStatistics();

                    try {
                        Unirest.shutdown();
                    } catch (IOException ignored) {
                    }

                    SkypeBot.INSTANCE$.getSkype().logout();
                    System.exit(0);
                } else {
                    lastSha = sha;
                }

                System.out.println("checked commit");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
