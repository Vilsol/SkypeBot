package me.vilsol.skypebot.modules;

import me.vilsol.skypebot.modules.http.headers.UserAgent;
import me.vilsol.skypebot.modules.http.request.post.HttpPostRequestBuilder;

import java.io.IOException;
import java.util.ArrayList;

public class CompilerConnection {

    private HttpPostRequestBuilder compilerRequest;

    public CompilerConnection() throws IOException {
        this.compilerRequest = new HttpPostRequestBuilder("http://www.javalaunch.com/JavaLaunch");
        this.compilerRequest.userAgent(UserAgent.CHROME_WINDOWS);
    }

    public void setCode(String code) {
        this.compilerRequest.addBodyInfo("program", code);
    }

    public void send() throws IOException {
        this.compilerRequest.addBodyInfo("but", "Run");
        this.compilerRequest.send();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createResult();
    }

    public HttpPostRequestBuilder getConnection() {
        return this.compilerRequest;
    }

    private String report;
    private String result;

    private void createResult() throws IOException {
        ArrayList<String> value = getConnection().getResult().getResult();
        this.report = value.get(0);
        report = report.substring(report.indexOf("<b>") + 3, report.indexOf("</b>"));
        this.result = value.get(1);
        result = result.substring(result.indexOf(">") + 1, result.indexOf("<br>"));
    }

    public String getReport() {
        return this.report;
    }

    public String getResult() {
        return this.result;
    }

}
