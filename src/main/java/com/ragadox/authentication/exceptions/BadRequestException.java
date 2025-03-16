package com.ragadox.authentication.exceptions;

import java.util.Map;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private Map<String, String> errorMap;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Map<String, String> errors) {
        super();
        this.errorMap = errors;
    }

}
