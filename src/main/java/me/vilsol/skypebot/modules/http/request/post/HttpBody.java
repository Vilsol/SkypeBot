package me.vilsol.skypebot.modules.http.request.post;

import java.util.Map;

public class HttpBody extends HttpPart {

    private String  value;
    private boolean built;

    /**
     * Returns if the build method has been called.
     *
     * @return Whether or not the body has been built.
     */
    public boolean isBuilt() {
        return built;
    }

    /**
     * Builds the body into POST request format.
     */
    public void build() {
        StringBuilder bodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> bodyEntry : getContents().entrySet())
            bodyBuilder.append("&").append(bodyEntry.getKey()).append("=").append(bodyEntry.getValue());

        this.value = bodyBuilder.toString();
        this.built = true;
    }

    /**
     * Returns built String.
     *
     * @return String that has been built.
     */
    public String getValue() {
        return value;
    }
}
