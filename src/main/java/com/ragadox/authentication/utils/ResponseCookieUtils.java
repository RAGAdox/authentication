package com.ragadox.authentication.utils;

import org.springframework.http.ResponseCookie;

public class ResponseCookieUtils {
    private static final String COOKIE_PATH="/";
    private static final int COOKIE_MAX_AGE=3600;
    private static final boolean COOKIE_SECURE=false;
    private static final boolean COOKIE_HTTP_ONLY=true;

    private ResponseCookieUtils() {}

    public static ResponseCookie getCookie(String cookieName, String cookieValue) {
        return ResponseCookie.from(cookieName, cookieValue).httpOnly(COOKIE_HTTP_ONLY).path(COOKIE_PATH).secure(COOKIE_SECURE).maxAge(COOKIE_MAX_AGE).sameSite("LAX").build();
    }
    public static ResponseCookie getCookie(String cookieName) {
        return ResponseCookie.from(cookieName).httpOnly(COOKIE_HTTP_ONLY).path(COOKIE_PATH).secure(COOKIE_SECURE).maxAge(0).sameSite("LAX").build();
    }

    public static ResponseCookie[] asArray(ResponseCookie... cookies) {
        return cookies;
    }
}
