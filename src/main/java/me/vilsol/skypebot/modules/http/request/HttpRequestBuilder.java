package me.vilsol.skypebot.modules.http.request;

import me.vilsol.skypebot.modules.http.cookie.Cookie;
import me.vilsol.skypebot.modules.http.headers.Header;
import me.vilsol.skypebot.modules.http.headers.UserAgent;
import me.vilsol.skypebot.modules.http.result.Result;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpRequestBuilder {

    private UserAgent   userAgent;

    public UserAgent getUserAgent() {
        return this.userAgent;
    }

    /**
     * Makes a UserAgent object then sets the connection's user agent.
     *
     * @param userAgent The String
     * @return
     */
    public HttpRequestBuilder userAgent(String userAgent) {
        this.userAgent = new UserAgent(userAgent);
        return this;
    }

    /**
     * Sets the connection's user agent.
     *
     * @param userAgent The UserAgent value to set the UserAgent to.
     * @return The HttpRequest object for further building.
     */
    public HttpRequestBuilder userAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * Universal send method.
     *
     * @return The HttpRequest object for further building.
     */
    public abstract HttpRequestBuilder send();

    public abstract HttpRequestBuilder applyHeader(Header header);

    /**
     * Returns the result object.
     *
     * @return Result object.
     * @throws java.io.IOException
     */
    public abstract Result getResult() throws IOException;

    /**
     * Closes the connection.
     *
     * @return The HttpRequest object for further building.
     * @throws java.io.IOException
     */
    public abstract HttpRequestBuilder closeConnection() throws IOException;

    /**
     * Gets the connection.
     *
     * @return The connection.
     */
    protected abstract HttpURLConnection getConnection();

    /**
     * Gets Cookie value from the request.
     *
     * @return A map of the Cookies.
     */
    public Map<String, Cookie> getCookies() {
        Map<String, Cookie> cookiesMap = new HashMap<>();
        for (String cookies : getConnection().getHeaderFields().get("Set-Cookie")) {
            Cookie cookie = new Cookie(cookies);
            cookiesMap.put(cookie.getName(), cookie);
        }
        return cookiesMap;
    }

    /**
     * Applies Cookies to the connection.
     *
     * @param cookies The cookies to apply to the connection.
     * @return The HttpRequest object for further building.
     */
    public HttpRequestBuilder applyCookies(Cookie... cookies) {
        StringBuilder cookieStringBuilder = new StringBuilder();
        for (int cookie = 0; cookie < cookies.length; cookie++) {
            cookieStringBuilder.append(cookies[cookie].toString());
            if (cookie != cookies.length - 1) cookieStringBuilder.append("; ");
        }
        getConnection().setRequestProperty("Cookie", cookieStringBuilder.toString());
        return this;
    }
}
