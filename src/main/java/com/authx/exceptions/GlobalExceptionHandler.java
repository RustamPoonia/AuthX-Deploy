package com.authx.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================================
    // 1. Validation Errors (400)
    // ================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String errorMessage = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        Map<String, Object> response = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorMessage,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<?> emailAlreadyExists(EmailAlreadyExistException ex){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "Conflict",
                "message", ex.getMessage()));
    }

    // ================================
    // Helper Method
    // ================================
    private Map<String, Object> buildErrorResponse(
            HttpStatus status,
            String message,
            String path) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        response.put("path", path);

        return response;
    }
}