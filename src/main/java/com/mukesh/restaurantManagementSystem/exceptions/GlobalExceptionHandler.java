package com.mukesh.restaurantManagementSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> errorResponse(EntityNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), LocalDate.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTypeException.class)
    public ResponseEntity<ErrorResponse> errorResponse(InvalidTypeException exception) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), LocalDate.now()), HttpStatus.BAD_REQUEST);
    }
}
