package me.vilsol.skypebot.modules.http.request.post;

import me.vilsol.skypebot.modules.http.Files;
import me.vilsol.skypebot.modules.http.headers.Header;
import me.vilsol.skypebot.modules.http.request.HttpRequestBuilder;
import me.vilsol.skypebot.modules.http.result.Result;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class HttpPostRequestBuilder extends HttpRequestBuilder {

    public static class RequestHeader {
        public static final Header URL_ENCODED = new Header("Content-Type", "application/x-www-form-urlencoded");
        public static final Header MULTIPART = new Header("Content-Type", "multipart/form-data");
    }

    private HttpHeader header = new HttpHeader();
    private HttpBody   body   = new HttpBody();
    private URL        url    = null;
    private HttpURLConnection connection;
    private boolean sent = false;
    private int responseCode;

    /**
     * Used when only the POST request is required.
     *
     * @param url The URL of the server.
     * @throws java.io.IOException
     */
    public HttpPostRequestBuilder(String url) throws IOException {
        this.url = new URL(url);
    }

    {
        applyHeader(RequestHeader.URL_ENCODED);
    }

    /**
     * Used when the connection is already made.
     *
     * @param connection Already connected URLConnection.
     */
    public HttpPostRequestBuilder(HttpURLConnection connection) {
        this.connection = connection;
    }

    /**
     * Adds information to the header of the request.
     *
     * @param headerAndValue A String with the key and value separated by ": ".
     * @return Request for further action.
     */
    @Deprecated
    public HttpPostRequestBuilder addHeaderInfo(String headerAndValue) {
        getHeader().addContent(headerAndValue);
        return this;
    }

    /**
     * Adds information to the body of the request.
     *
     * @param keyAndValue A String with the key and value separated by ": ".
     * @return Request for further action.
     */
    public HttpPostRequestBuilder addBodyInfo(String keyAndValue) {
        getBody().addContent(keyAndValue);
        return this;
    }

    /**
     * Adds information to the header of the request.
     *
     * @param headerEntry A map entry that is used to get the key and value, which will be put into the header.
     * @return Request for further action.
     */
    @Deprecated
    public HttpPostRequestBuilder addHeaderInfo(Map.Entry<String, String> headerEntry) {
        getHeader().addContent(headerEntry);
        return this;
    }

    /**
     * Adds information to the body of the request.
     *
     * @param bodyEntry A map entry that is used to get the key and value, which will be put into the body.
     * @return Request for further action.
     */
    public HttpPostRequestBuilder addBodyInfo(Map.Entry<String, String> bodyEntry) {
        getBody().addContent(bodyEntry);
        return this;
    }

    /**
     * Adds information to the header of the request.
     *
     * @param header The header key put in the header map.
     * @param value  The value given to the header.
     * @return Request for further action.
     */
    @Deprecated
    public HttpPostRequestBuilder addHeaderInfo(String header, String value) {
        getHeader().addContent(header, value);
        return this;
    }

    /**
     * Adds information to the body of the request.
     *
     * @param key   The key used in the body.
     * @param value The value given to the key.
     * @return Request for further action.
     */
    public HttpPostRequestBuilder addBodyInfo(String key, Object value) {
        getBody().addContent(key, value);
        return this;
    }

    public HttpPart getBody() {
        return body;
    }

    public HttpPart getHeader() {
        return header;
    }

    /**
     * Opens the connection to the server, then sets the header. Builds the body after.
     *
     * @return Request for further action.
     * @throws java.io.IOException
     */
    public HttpPostRequestBuilder buildRequest() throws IOException {
        if (this.connection == null) this.connection = (HttpURLConnection) url.openConnection();

        for (Map.Entry<String, String> headerEntry : header.getContents().entrySet())
            connection.setRequestProperty(headerEntry.getKey(), headerEntry.getValue());

        buildBody();

        return this;
    }

    /**
     * Builds the body of the request.
     *
     * @return Request for further action.
     */
    public HttpPostRequestBuilder buildBody() {
        body.build();
        return this;
    }

    /**
     * Builds request if not already done so, then sends the POST request.
     *
     * @return Returns the object, to retrieve the result.
     * @throws java.io.IOException
     */
    private HttpPostRequestBuilder sendRequest() throws IOException {
        if (!body.isBuilt()) buildRequest();

        this.connection.setRequestMethod("POST");
        this.connection.setDoOutput(true);

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

        outputStream.writeBytes(body.getValue());
        outputStream.flush();
        outputStream.close();

        this.responseCode = this.connection.getResponseCode();
        this.sent = true;

        return this;
    }

    /**
     * Closes the connection preventing any further sending. Use this when you are finished with this object.
     *
     * @return The HttpPostRequest object for reading information.
     * @throws java.io.IOException
     */
    @Override
    public HttpPostRequestBuilder closeConnection() throws IOException {
        connection.disconnect();

        return this;
    }

    @Override
    public HttpPostRequestBuilder send() {
        try {
            return sendRequest();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HttpPostRequestBuilder applyHeader(Header header) {
        getHeader().addContent(header.getHeader());
        return this;
    }

    /**
     * Sends the request if not already done so. Returns a new Result object combining the response code and reading the information returned from the server.
     *
     * @return Returns the information sent back by the server, including the response code.
     */
    @Override
    public Result getResult() throws IOException {
        if (!this.sent) sendRequest();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        ArrayList<String> result = Files.readBuffer(reader);

        return new Result(result, responseCode);
    }

    @Override
    protected HttpURLConnection getConnection() {
        return connection;
    }
}