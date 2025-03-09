package com.ragadox.authentication.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{

    private Map<String,String> errorMap;
    public BadRequestException(String message){
        super(message);
    }
    public BadRequestException(Map<String,String> errors){
        super();
        this.errorMap=errors;
    }

}
