package com.ragadox.authentication.exceptions;

import com.ragadox.authentication.dto.ResponseDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDTO<Object>> handleBadRequestException(BadRequestException exception){

        if(exception.getErrorMap()!=null){
            return ResponseDTO.error(exception.getErrorMap(),HttpStatus.BAD_REQUEST);
        }

        return ResponseDTO.error(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDTO<Object>> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex){
        return ResponseDTO.error(ex.getMessage(),HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ResponseDTO<Object>> handleResourceAlreadyExists(ResourceAlreadyExists ex){
        return ResponseDTO.error(ex.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ResponseDTO<Object>> handleResourceNotFound(ResourceNotFound ex){
        return ResponseDTO.error(ex.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseDTO.error(errors,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<Object>> handleConstraintViolationException(ConstraintViolationException ex){
        return ResponseDTO.error(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
