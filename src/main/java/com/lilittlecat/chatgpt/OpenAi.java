package com.lilittlecat.chatgpt;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Get access token
 * </p>
 * see: <a href="https://github.com/rawandahmad698/PyChatGPT/blob/master/src/pychatgpt/classes/openai.py">openai.py</a>
 * <p>
 *
 * @author LiLittleCat
 * @link <a href="https://github.com/LiLittleCat">https://github.com/LiLittleCat</a>
 * @since 2022/12/7
 */
public class OpenAi {
    /*
     * todo there is Bad Request, set token manually temporarily
     * todo handle captcha when get access token
     */
    private String email;
    private String password;
    private Boolean useProxy;
    private String proxy;
    private String accessToken;
    private String sessionToken;

    public OpenAi(String email, String password) {
        this(email, password, false, null);
    }

    public OpenAi(String email, String password, Boolean useProxy, String proxy) {
        this.email = email;
        this.password = password;
        if (useProxy) {
            this.proxy = proxy;
        }
        Unirest.config().enableCookieManagement(false);
    }

    public boolean tokenExpired() {
        return false;
    }

    public String getAccessToken() {
        return "";
    }

    private Map<String, String> cookies = new HashMap<>();

    private void parseSetCookie(HttpResponse<JsonNode> response) {
        List<String> strings = response.getHeaders().get("Set-Cookie");
        if (strings != null && !strings.isEmpty()) {
            for (String string : strings) {
                String s = string.split(";")[0];
                String[] split = s.split("=");
                cookies.put(split[0], split[1]);
            }
            System.out.println(strings);
        }
    }

    private String getCookie() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
        }
        return stringBuilder.toString();
    }

    /**
     * Begin the auth process
     *
     * @return
     */
    public String createAccessToken() {
        HttpResponse<JsonNode> response = Unirest.get("https://chat.openai.com/auth/login")
//                .header("Host", "ask.openai.com")
                .header("Accept", "*/*")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15")
                .header("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Connection", "keep-alive")
                .asJson();
        parseSetCookie(response);
        if (response.getStatus() == HttpStatus.OK) {
            return partTwo();
        } else {
            throw new RuntimeException("[OpenAI][1] Failed to make the request, Try that again!");
        }
    }

    /**
     * In part two, We make a request to https://chat.openai.com/api/auth/csrf and grab a fresh csrf token
     *
     * @return
     */
    private String partTwo() {
        HttpResponse<JsonNode> response = Unirest.get("https://chat.openai.com/api/auth/csrf")
//                .header("Host", "ask.openai.com")
                .header("Accept", "*/*")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15")
                .header("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Connection", "keep-alive")
                .header("Referer", "https://chat.openai.com/auth/login")
                .cookie("cookie", getCookie())
                .asJson();
        parseSetCookie(response);
        if (response.getStatus() == HttpStatus.OK) {
            String csrfToken = ((String) response.getBody().getObject().get("csrfToken"));
            System.out.println("csrfToken: " + csrfToken);
            return partThree(csrfToken);
        } else {
            throw new RuntimeException("[OpenAI][2] Failed to make the request, Try that again!");
        }
    }

    /**
     * We reuse the token from part to make a request to /api/auth/signin/auth0?prompt=login
     *
     * @param token
     * @return
     */
    private String partThree(String token) {
        String payload = "callbackUrl=%2F&csrfToken=" + token + "&json=true";
        HttpResponse<JsonNode> response = Unirest.post("https://chat.openai.com/api/auth/signin/auth0?prompt=login")
//                .header("Host", "ask.openai.com")
                .header("Origin", "https://chat.openai.com")
                .header("Connection", "keep-alive")
                .header("Accept", "*/*")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15")
                .header("Referer", "https://chat.openai.com/auth/login")
//                .header("Content-Length", "100")
                .header("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .cookie("cookie", getCookie())
                .body(payload)
                .asJson();
        parseSetCookie(response);
        if (response.getStatus() == HttpStatus.OK && response.getHeaders().get("Content-Type").get(0).contains("json")) {
            String url = ((String) response.getBody().getObject().get("url"));
            System.out.println("url: " + url);
            return partFour(url);
        } else if (response.getStatus() == HttpStatus.BAD_REQUEST) {
            throw new RuntimeException("[OpenAI][3] Bad request from your IP address, try again in a few minutes");
        } else {
            throw new RuntimeException("[OpenAI][3] Failed to make the request, Try that again!");
        }

    }

    /**
     * We make a GET request to url
     *
     * @param url
     * @return
     */
    private String partFour(String url) {
        HttpResponse<JsonNode> response = Unirest.get(url)
//                .header("Host", "ask.openai.com")
                .header("Connection", "keep-alive")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15")
                .header("Referer", "https://chat.openai.com/")
                .header("Accept-Language", "en-US,en;q=0.9")
                .cookie("cookie", getCookie())
                .asJson();
        if (response.getStatus() == HttpStatus.FOUND) {
            String state = Pattern.compile("state=(.*)").matcher(response.getBody().toString()).group(1).split("\"")[0];
            return partFive(state);
        } else {
            throw new RuntimeException("[OpenAI][4] Failed to make the request, Try that again!");
        }
    }

    /**
     * WWe use the state to get the login page & check for a captcha
     *
     * @param state
     * @return
     */
    private String partFive(String state) {
        String url = String.format("https://auth0.openai.com/u/login/identifier?state=%s", state);
        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("Host", "ask.openai.com")
                .header("Connection", "keep-alive")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15")
                .header("Referer", "https://chat.openai.com/")
                .header("Accept-Language", "en-US,en;q=0.9")
                .asJson();
        if (response.getStatus() == HttpStatus.OK) {
            if (Pattern.compile("<img[^>]+alt=\"captcha\"[^>]+>").matcher(response.getBody().toString()).find()) {
                System.out.println("Captcha detected");
                throw new RuntimeException("[OpenAI][5] Captcha detected");
            }
            return partSix(state, null);
        } else {
            throw new RuntimeException("[OpenAI][5] Failed to make the request, Try that again!");
        }
    }

    /**
     * // URL encode
     *
     * @param string
     * @return
     */
    public static String urlEncode(String string) {
        return java.net.URLEncoder.encode(string);
    }

    /**
     * We make a POST request to the login page with the captcha, email
     *
     * @param state
     * @param captcha
     * @return
     */
    private String partSix(String state, String captcha) {
        String url = String.format("https://auth0.openai.com/u/login/identifier?state=%s", state);
        String emailUrlEncoded = urlEncode(email);
        String payload;
        if (captcha == null) {
            payload = String.format("state=%s&username=%s&js-available=false&webauthn-available" +
                            "=true&is-brave=false&webauthn-platform-available=true&action=default ",
                    state, emailUrlEncoded);
        } else {
            payload = String.format("state=%s&username=%s&captcha=%s&js-available=true" +
                    "&webauthn-available=true&is-brave=false&webauthn-platform-available=true&action" +
                    "=default ", state, emailUrlEncoded, captcha);
        }
        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("Host", "ask.openai.com")
                .header("Connection", "keep-alive")
                .header("Origin", "https://auth0.openai.com")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15")
                .header("Referer", url)
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(payload)
                .asJson();
        if (response.getStatus() == HttpStatus.FOUND) {
            return partSeven(state);
        } else {
            throw new RuntimeException("[OpenAI][6] Email not found, Check your email address and try again!");
        }
    }

    /**
     * We enter the password
     *
     * @param state
     * @return
     */
    private String partSeven(String state) {
        String url = String.format("https://auth0.openai.com/u/login/password?state=%s", state);
        String emailUrlEncoded = urlEncode(email);
        String passwordUrlEncoded = urlEncode(password);
        String payload = String.format("state=%s&username=%s&password=%s&action=default", state, emailUrlEncoded, passwordUrlEncoded);

        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("Host", "ask.openai.com")
                .header("Connection", "keep-alive")
                .header("Origin", "https://auth0.openai.com")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15")
                .header("Referer", url)
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(payload)
                .asJson();
        if (response.getStatus() == HttpStatus.FOUND) {
            String newState = response.getBody().toString().split("state=")[0].split("\"")[0];
            return partEight(state, newState);
        } else {
            throw new RuntimeException("[OpenAI][7] Password was incorrect or captcha was wrong");
        }
    }

    /**
     * Get access_token and an hour from now on CHATGPT_ACCESS_TOKEN CHATGPT_ACCESS_TOKEN_EXPIRY environment variables
     *
     * @param oldState
     * @param newState
     * @return
     */
    private String partEight(String oldState, String newState) {
        String url = String.format("https://auth0.openai.com/authorize/resume?state=%s", newState);
        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("Host", "ask.openai.com")
                .header("Connection", "keep-alive")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15")
                .header("Referer", String.format("https://auth0.openai.com/u/login/password?state=%s", oldState))
                .header("Accept-Language", "en-GB,en-US;q=0.9,en;q=0.8")
                .asJson();
        if (response.getStatus() == HttpStatus.OK) {
            Pattern pattern = Pattern.compile("accessToken\":\"(.*)\"");
            Matcher matcher = pattern.matcher(response.getBody().toString());
            if (matcher.find()) {
                // return access_token and an hour from now on ./classes/auth.json
                return matcher.group();
            } else {
                throw new RuntimeException("[OpenAI][8] While most of the process was successful, " +
                        "Auth0 didn't issue an access token, Use proxies or retry.");
            }
        } else {
            throw new RuntimeException("[OpenAI][8] While most of the process was successful, " +
                    "Auth0 didn't issue an access token, Use proxies or retry.");
        }
    }
}
