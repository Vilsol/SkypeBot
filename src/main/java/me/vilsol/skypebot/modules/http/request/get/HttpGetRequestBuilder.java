package me.vilsol.skypebot.modules.http.request.get;

import me.vilsol.skypebot.modules.http.Files;
import me.vilsol.skypebot.modules.http.headers.Header;
import me.vilsol.skypebot.modules.http.request.HttpRequestBuilder;
import me.vilsol.skypebot.modules.http.result.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpGetRequestBuilder extends HttpRequestBuilder {

    private HttpURLConnection httpURLConnection;
    private StringBuilder     url;
    private List<Header> headers = new ArrayList<>();

    public HttpGetRequestBuilder(String url) throws MalformedURLException {
        this.url = new StringBuilder(url);
        this.url.append("?");
    }

    private HttpGetRequestBuilder sendRequest() throws IOException {
        this.httpURLConnection.connect();
        return this;
    }

    /**
     * Adds a part to the URL.
     *
     * @param key The key of the part to add.
     * @param value The value of the part.
     * @return Request for further action.
     */
    public HttpGetRequestBuilder addPart(String key, String value) {
        this.url.append("&");
        this.url.append(key);
        this.url.append("=");
        this.url.append(value);
        return this;
    }

    /**
     * Closes the HttpURLConnection.
     *
     * @return Request for further action.
     */
    public HttpGetRequestBuilder closeConnection() {
        this.httpURLConnection.disconnect();
        return this;
    }

    /**
     * Sends a request.
     *
     * @return Request for further action.
     */
    @Override
    public HttpGetRequestBuilder send() {
        try {
            return sendRequest();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates the connection, but does not send it.
     *
     * @return Request for further action.
     * @throws java.io.IOException
     */
    public HttpGetRequestBuilder createConnection() throws IOException {
        this.httpURLConnection = (HttpURLConnection) new URL(url.toString()).openConnection();
        for (Header header : headers) this.httpURLConnection.setRequestProperty(header.getHeader().getKey(), header.getHeader().getValue());
        return this;
    }

    /**
     * Applies a request property such as the user agent.
     *
     * @param header The header to apply.
     * @return Request for further action.
     */
    @Override
    public HttpGetRequestBuilder applyHeader(Header header) {
        this.headers.add(header);
        return this;
    }

    /**
     * Gets the Result object.
     *
     * @return The Result object.
     * @throws java.io.IOException
     */
    @Override
    public Result getResult() throws IOException {
        ArrayList<String> result = Files.readBuffer(new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())));
        return new Result(result, this.httpURLConnection.getResponseCode());
    }

    /**
     * Returns the HttpURLConnection.
     *
     * @return The HttpURLConnection object.
     */
    @Override
    protected HttpURLConnection getConnection() {
        return httpURLConnection;
    }
}
