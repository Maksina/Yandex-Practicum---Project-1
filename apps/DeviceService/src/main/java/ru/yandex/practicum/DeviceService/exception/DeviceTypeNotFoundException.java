package ru.yandex.practicum.DeviceService.exception;

public class DeviceTypeNotFoundException extends RuntimeException {
    public DeviceTypeNotFoundException(String message) {
        super(message);
    }
}