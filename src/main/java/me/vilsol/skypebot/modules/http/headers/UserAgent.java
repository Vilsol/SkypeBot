package me.vilsol.skypebot.modules.http.headers;

public class UserAgent extends Header {

    public static final UserAgent FIREFOX             = new UserAgent("Mozilla/5.0");
    public static final UserAgent CHROME_WINDOWS      = new UserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
    public static final UserAgent SAFARI_MACINTOSH    = new UserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10) AppleWebKit/600.1.25 (KHTML, like Gecko) Version/8.0 Safari/600.1.25");
    public static final UserAgent SAFARI_IPHONE_IOS_7 = new UserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 7_1_2 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D257 Safari/9537.53");
    public static final UserAgent SAFARI_IPHONE_IOS_8 = new UserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 8_1 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12B411 Safari/600.1.4");

    public UserAgent(String userAgent) {
        super("User-Agent", userAgent);
    }
}