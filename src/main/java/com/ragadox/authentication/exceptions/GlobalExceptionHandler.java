package com.ragadox.authentication.exceptions;

import com.ragadox.authentication.dto.ErrorResponseBuilderDTO;
import com.ragadox.authentication.dto.ResponseDTO;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

import static com.ragadox.authentication.utils.ResponseCookieUtils.getCookie;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<Class<?>, HttpStatus> exceptionMap = new HashMap<>();

    static {
        exceptionMap.put(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST);
        exceptionMap.put(AuthenticationException.class, HttpStatus.UNAUTHORIZED);
        exceptionMap.put(NoResourceFoundException.class, HttpStatus.METHOD_NOT_ALLOWED);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Object>> handleGenericExceptions(Exception exception) {

        log.error("Exception Occurred: {}", exception.getMessage());
        HttpStatus httpStatus = exceptionMap.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(exception.getClass()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);

        log.error("Exception Status: {}", httpStatus);
        return ErrorResponseBuilderDTO
                .withStatus(httpStatus)
                .build();

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDTO<Object>> handleBadCredentialsException(BadCredentialsException exception) {
        log.error("Bad Credentials Exception Occurred: {}", exception.getMessage());
        return ErrorResponseBuilderDTO
                .withError(exception.getMessage())
                .withHttpStatus(HttpStatus.UNAUTHORIZED)
                .withCookies(getCookie("token"))
                .build();

    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDTO<Object>> handleBadRequestException(BadRequestException exception) {

        log.error("Bad Request Exception Occurred: {}", exception.getMessage());
        if (exception.getErrorMap() != null) {
            log.error("Bad Request Errors: {}", exception.getErrorMap());
            return ErrorResponseBuilderDTO
                    .withErrorObject(exception.getErrorMap())
                    .withHttpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        log.error("Bad Request Error: {}", exception.getMessage());
        return ErrorResponseBuilderDTO
                .withError(exception.getMessage())
                .withHttpStatus(HttpStatus.BAD_REQUEST)
                .build();

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDTO<Object>> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {

        log.error("Method Not Allowed Exception Occurred: {}", ex.getMessage());
        return ErrorResponseBuilderDTO
                .withError(ex.getMessage())
                .withHttpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .build();

    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ResponseDTO<Object>> handleResourceAlreadyExists(ResourceAlreadyExists ex) {

        log.error("Resource Already Exists Exception Occurred: {}", ex.getMessage());
        return ErrorResponseBuilderDTO
                .withError(ex.getMessage())
                .withHttpStatus(HttpStatus.CONFLICT)
                .build();

    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ResponseDTO<Object>> handleResourceNotFound(ResourceNotFound ex) {

        log.error("Resource Not Found Exception Occurred: {}", ex.getMessage());
        return ErrorResponseBuilderDTO
                .withError(ex.getMessage())
                .withHttpStatus(HttpStatus.NOT_FOUND)
                .build();

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Object>> handleValidationException(MethodArgumentNotValidException ex) {



        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.error("Validation Errors Occurred: {}", errors);
        return ErrorResponseBuilderDTO
                .withErrorObject(errors)
                .withHttpStatus(HttpStatus.BAD_REQUEST)
                .build();

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<Object>> handleConstraintViolationException(ConstraintViolationException ex) {

        log.error("Constraint Violation Exception Occurred: {}", ex.getMessage());

        return ErrorResponseBuilderDTO
                .withError(ex.getMessage())
                .withHttpStatus(HttpStatus.BAD_REQUEST)
                .build();

    }

    @ExceptionHandler(JWTAuthenticationException.class)
    public ResponseEntity<ResponseDTO<Object>> handleCustomAuthenticationException(JWTAuthenticationException ex) {

        log.error("JWT Authentication Exception Occurred: {}", ex.getMessage());

        return ErrorResponseBuilderDTO.withError(ex.getMessage()).withHttpStatus(HttpStatus.UNAUTHORIZED).withCookies(getCookie("token")).build();

    }
}
