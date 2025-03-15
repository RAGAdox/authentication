package com.ragadox.authentication.dto;

import com.ragadox.authentication.utils.HttpHeadersUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

public class SuccessResponseBuilderDTO<T> {

    private final T data;
    private HttpHeaders httpHeaders = new HttpHeaders();

    private SuccessResponseBuilderDTO(T data) {
        this.data = data;
    }

    public static <T> SuccessResponseBuilderDTO<T> withData(T data) {
        return new SuccessResponseBuilderDTO<>(data);
    }

    public SuccessResponseBuilderDTO<T> withCookies(ResponseCookie... responseCookies){
        this.httpHeaders = HttpHeadersUtils.getHeaders(responseCookies);
        return this;
    }

    public ResponseEntity<ResponseDTO<T>> build(){
        return ResponseEntity.ok().headers(httpHeaders).body(new ResponseDTO<>(data));
    }
}
