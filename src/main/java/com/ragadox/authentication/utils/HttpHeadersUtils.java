package com.ragadox.authentication.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;


public class HttpHeadersUtils {
    private HttpHeadersUtils() {}

    public static HttpHeaders getHeaders(ResponseCookie responseCookie) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return httpHeaders;
    }

    public static HttpHeaders getHeaders(ResponseCookie[] responseCookies) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (responseCookies != null) {
            for (ResponseCookie responseCookie : responseCookies) {
                httpHeaders.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
            }
        }
        return httpHeaders;
    }


}
