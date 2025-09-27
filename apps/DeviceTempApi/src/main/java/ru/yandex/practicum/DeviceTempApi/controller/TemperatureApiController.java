package ru.yandex.practicum.DeviceTempApi.controller;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Random;

@RestController
public class TemperatureApiController {

    private final Random random = new Random();

    /**
     * Endpoint: GET /temperature?location=...&sensorId=...
     */
    @GetMapping("/temperature")
    public TemperatureResponse getTemperatureByQuery(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String sensorId) {

        return buildResponse(location, sensorId);
    }

    /**
     * Endpoint: GET /temperature/{sensorId}
     */
    @GetMapping("/temperature/{sensorId}")
    public TemperatureResponse getTemperatureByPath(@PathVariable String sensorId) {
        return buildResponse(null, sensorId);
    }

    /**
     * Общая логика формирования ответа с сопоставлением location ↔ sensorId
     */
    private TemperatureResponse buildResponse(String location, String sensorId) {
        // Если location не задан — определяем по sensorId
        if (location == null || location.isEmpty()) {
            if ("1".equals(sensorId)) {
                location = "Living Room";
            } else if ("2".equals(sensorId)) {
                location = "Bedroom";
            } else if ("3".equals(sensorId)) {
                location = "Kitchen";
            } else {
                location = "Unknown";
            }
        }

        // Если sensorId не задан — определяем по location
        if (sensorId == null || sensorId.isEmpty()) {
            if ("Living Room".equals(location)) {
                sensorId = "1";
            } else if ("Bedroom".equals(location)) {
                sensorId = "2";
            } else if ("Kitchen".equals(location)) {
                sensorId = "3";
            } else {
                sensorId = "0";
            }
        }

        double value = -10 + (50 * random.nextDouble()); // Диапазон: [-10, 40)

        return new TemperatureResponse(
                value,
                "Celsius",
                Instant.now(),
                location,
                "active",
                sensorId,
                "temperature",
                "Temperature sensor for " + location
        );
    }

    /**
     * DTO, полностью совместимый с Go-структурой TemperatureResponse
     */
    public static class TemperatureResponse {
        private final double value;
        private final String unit;
        private final Instant timestamp;
        private final String location;
        private final String status;

        @JsonProperty("sensor_id")
        private final String sensorId;

        @JsonProperty("sensor_type")
        private final String sensorType;

        private final String description;

        public TemperatureResponse(double value, String unit, Instant timestamp,
                                   String location, String status, String sensorId,
                                   String sensorType, String description) {
            this.value = value;
            this.unit = unit;
            this.timestamp = timestamp;
            this.location = location;
            this.status = status;
            this.sensorId = sensorId;
            this.sensorType = sensorType;
            this.description = description;
        }

        // Геттеры — обязательны для сериализации Jackson
        public double getValue() { return value; }
        public String getUnit() { return unit; }
        public Instant getTimestamp() { return timestamp; }
        public String getLocation() { return location; }
        public String getStatus() { return status; }
        public String getSensorId() { return sensorId; }
        public String getSensorType() { return sensorType; }
        public String getDescription() { return description; }
    }
}