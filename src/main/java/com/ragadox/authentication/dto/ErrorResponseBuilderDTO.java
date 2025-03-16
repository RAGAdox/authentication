package com.ragadox.authentication.dto;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import com.ragadox.authentication.utils.HttpHeadersUtils;

public class ErrorResponseBuilderDTO<T> {
    private HttpStatus status=HttpStatus.INTERNAL_SERVER_ERROR;
    private final String error;
    private final Map<String, String> errorObject;
    private HttpHeaders httpHeaders = new HttpHeaders();

    private ErrorResponseBuilderDTO(String error) {
        this.error = error;
        this.errorObject = null;
    }

    private ErrorResponseBuilderDTO(Map<String, String> errorObject) {
        this.errorObject = errorObject;
        this.error = null;
    }

    private ErrorResponseBuilderDTO(HttpStatus status) {
        this.status = status;
        this.error = status.getReasonPhrase();
        this.errorObject = null;
    }

    public static <T> ErrorResponseBuilderDTO<T> withError(String errorMessage) {
        return new ErrorResponseBuilderDTO<T>(errorMessage);
    }

    public static <T> ErrorResponseBuilderDTO<T> withErrorObject(Map<String, String> errorObject) {
        return new ErrorResponseBuilderDTO<T>(errorObject);
    }

    public static <T> ErrorResponseBuilderDTO<T> withStatus(HttpStatus status) {
        return new ErrorResponseBuilderDTO<T>(status);
    }

    public ErrorResponseBuilderDTO<T> withHttpStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public ErrorResponseBuilderDTO<T> withCookies(ResponseCookie... responseCookies) {
        this.httpHeaders = HttpHeadersUtils.getHeaders(responseCookies);
        return this;
    }

    public ResponseEntity<ResponseDTO<Object>> build(){
        if(this.errorObject != null){
            return ResponseEntity
                    .status(this.status)
                    .headers(this.httpHeaders)
                    .body( new ResponseDTO<>(this.errorObject));
        }
        return ResponseEntity
                .status(this.status)
                .headers(this.httpHeaders)
                .body( new ResponseDTO<>(this.error));
    }


}
