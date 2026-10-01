package io.github.gomestkdev.backend.exeptions;

import io.github.gomestkdev.backend.exeptions.handler.ApiException;
import io.github.gomestkdev.backend.exeptions.handler.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> ArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String message = err.getDefaultMessage();

            response.put(fieldName, message);
        });

        return new ResponseEntity<Map<String, String>>(response, BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> ResourceNotFoundException(ResourceNotFoundException e) {
        String message = e.getMessage();

        return new ResponseEntity<>(message, NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> ApiException(ResourceNotFoundException e) {
        String message = e.getMessage();

        return new ResponseEntity<>(message, BAD_REQUEST);
    }
}
