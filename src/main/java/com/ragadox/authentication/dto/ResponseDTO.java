package com.ragadox.authentication.dto;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import com.ragadox.authentication.utils.HttpHeadersUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseDTO<T> {
    private boolean success;
    private T data;
    private String error;
    private Map<String, String> errorObject;

    public ResponseDTO(T data) {
        this.success = true;
        this.data = data;
        this.error = null;
        this.errorObject = null;
    }

    public ResponseDTO(String errorMessage) {
        this.success = false;
        this.error = errorMessage;
        this.data = null;
        this.errorObject = null;
    }

    public ResponseDTO(Map<String, String> errorObject) {
        this.success = false;
        this.error = errorObject.toString();
        this.data = null;
        this.errorObject = errorObject;
    }





    public static <T> ResponseEntity<ResponseDTO<T>> error(String errorMessage, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(new ResponseDTO<>(errorMessage));
    }

    public static <T> ResponseEntity<ResponseDTO<T>> error(String errorMessage, HttpStatus httpStatus,
                                                           ResponseCookie responseCookies) {
        return ResponseEntity.status(httpStatus).headers(HttpHeadersUtils.getHeaders(responseCookies)).body(new ResponseDTO<>(errorMessage));

    }


    public static <T> ResponseEntity<ResponseDTO<T>> error(String errorMessage) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(errorMessage));
    }

    public static <T> ResponseEntity<ResponseDTO<T>> error(Map<String, String> errorObject, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(new ResponseDTO<>(errorObject));
    }

    public static <T> ResponseEntity<ResponseDTO<T>> error(Map<String, String> errorObject) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(errorObject));
    }

    public static <T> ResponseEntity<ResponseDTO<T>> error(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(new ResponseDTO<>(httpStatus.getReasonPhrase()));
    }
}
