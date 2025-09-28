package ru.yandex.practicum.TelemetryService.controller;

import ru.yandex.practicum.TelemetryService.model.TelemetryPoint;
import ru.yandex.practicum.TelemetryService.service.TelemetryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TelemetryController {

    private final TelemetryService telemetryService;

    public TelemetryController(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    @GetMapping("/telemetry")
    public List<TelemetryPoint> getTelemetry(
            @RequestParam Long device_id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant stop,
            @RequestParam(required = false) List<String> fields,
            @RequestParam(required = false) String agg,
            @RequestParam(required = false) String window) {

        return telemetryService.getTelemetry(device_id, start, stop, fields, agg, window);
    }
}