package com.mi.iam.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mi.iam.helpers.ResponseHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
    return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "constraint error", ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    // Get all errors
    List<String> exceptionalErrors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(x -> x.getField() + " " + x.getDefaultMessage())
        .collect(Collectors.toList());
    String msg = String.join(", ", exceptionalErrors);
    return ResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, "validation error", msg);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(Exception ex) {
    return ResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, "request failed", ex.getMessage());
  }
}
