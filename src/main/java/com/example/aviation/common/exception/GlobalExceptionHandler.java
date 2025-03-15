package com.example.aviation.common.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.aviation.common.dto.response.BaseErrorResponse;
import com.example.aviation.common.exception.custom.LocationNotFoundExeption;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("Field validation error occured");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LocationNotFoundExeption.class)
    public ResponseEntity<BaseErrorResponse> handleAuthException(LocationNotFoundExeption ex) {
        BaseErrorResponse error = BaseErrorResponse.builder().errorMessage(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {

        ex.printStackTrace();
        String detailMessage = ex.getMostSpecificCause().getMessage();
        String detail = detailMessage.substring(detailMessage.indexOf("Detail:") + 8).trim();
        detail = Objects.isNull(detail) ? "Data integrity violation" : detail;
        BaseErrorResponse error = BaseErrorResponse.builder().errorMessage(detail).build();
        log.warn("Data integrity violation error occurred: {}", detail);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

}
