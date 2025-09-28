package ru.yandex.practicum.LocationService.exception;

public class InvalidLocationHierarchyException extends RuntimeException {
    public InvalidLocationHierarchyException(String message) {
        super(message);
    }
}