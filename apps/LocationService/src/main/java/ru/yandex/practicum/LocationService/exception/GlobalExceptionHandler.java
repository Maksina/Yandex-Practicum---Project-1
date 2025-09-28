package ru.yandex.practicum.LocationService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<Void> handleNotFound(LocationNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidLocationHierarchyException.class)
    public ResponseEntity<Void> handleInvalidHierarchy(InvalidLocationHierarchyException ex) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}