package io.mazenmc.skypebot.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UpdateChecker extends Thread {

    private String lastSha = "--";

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }

            try {
                HttpResponse<JsonNode> response = Unirest.get("https://api.github.com/repos/MazenMC/SkypeBot/commits?page=1")
                        .asJson();

                JsonNode node = response.getBody();
                JSONObject recentCommit = node.getArray().getJSONObject(0);
                String sha = recentCommit.getString("sha");

                if(!lastSha.equals(sha) && !lastSha.equals("--")) {
                    URL url = new URL("https://github.com/MazenMC/SkypeBot/archive/master.zip");
                    HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
                    JSONObject commit = recentCommit.getJSONObject("commit");

                    Resource.sendMessage("Found new commit: " +
                            commit.getJSONObject("author").getString("name") + " - " +
                            commit.getString("message") + " (" + sha + ")");
                    Resource.sendMessage("Updating...");

                    try (InputStream stream = c.getInputStream()) {
                        File f = new File("master.zip");

                        if(f.exists())
                            f.delete();

                        Files.copy(stream, Paths.get("master.zip"));
                        stream.close();
                    }

                    File output = new File("SkypeBot-master");

                    if(output.exists())
                        output.delete();

                    ZipFile zip = new ZipFile(new File("master.zip"));

                    zip.extractAll(System.getProperty("user.dir"));

                    Resource.sendMessage("Copied repository and extracted! Compiling...");

                    ProcessBuilder builder = new ProcessBuilder("/usr/bin/mvn", "clean", "compile", "assembly:single")
                            .redirectErrorStream(true).directory(output);
                    Process process = builder.start();

                    process.waitFor();

                    File compiled = new File(output, "target/skypebot-1.0-SNAPSHOT-jar-with-dependencies.jar");
                    File current = new File("skypebot-1.0-SNAPSHOT-jar-with-dependencies.jar");

                    if(!compiled.exists()) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String tmp;
                        List<String> lines = new ArrayList<>();

                        while((tmp = in.readLine()) != null) {
                            lines.add(tmp);
                        }

                        in.close();

                        Resource.sendMessage("Whoops! Project did not compile correctly " + Utils.upload(lines));
                        lastSha = sha;
                        continue;
                    }

                    current.delete();
                    current.createNewFile();

                    FileOutputStream fos = new FileOutputStream(current);
                    FileInputStream fis = new FileInputStream(compiled);
                    byte[] buffer = new byte[1024];
                    int i;

                    while((i = fis.read(buffer)) > 0) {
                        fos.write(buffer, 0, i);
                    }

                    fis.close();
                    fos.close();
                    process.destroy();

                    Resource.sendMessage("Finished compiling! Restarting...");
                    Utils.restartBot();
                } else {
                    lastSha = sha;
                }
            } catch (Exception e) {
                Resource.sendMessage("Was unable to check for new commits (" +
                        Utils.upload(ExceptionUtils.getStackTrace(e)) + ")");
            }
        }
    }

}
