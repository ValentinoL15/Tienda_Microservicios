package com.valentino.carrito_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        String message = ex.getReason();
        try {
            // Intentamos parsear el mensaje si viene como JSON
            Map<String, Object> parsed = new ObjectMapper().readValue(message, Map.class);
            return ResponseEntity.status(ex.getStatusCode()).body(parsed);
        } catch (Exception e) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", message));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> body = Map.of(
                "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
