package ru.yandex.practicum.DeviceService.controller;

import ru.yandex.practicum.DeviceService.dto.*;
import ru.yandex.practicum.DeviceService.service.DeviceTypeTelemetryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DeviceTypeTelemetryController {

    private final DeviceTypeTelemetryService service;

    @PostMapping("/device-type-telemetries")
    public ResponseEntity<DeviceTypeTelemetry> addDeviceTypeTelemetry(@Valid @RequestBody DeviceTypeTelemetryCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTelemetry(dto));
    }

    @GetMapping("/device-type-telemetries/{deviceTypeTelemetryId}")
    public ResponseEntity<DeviceTypeTelemetry> getDeviceTypeTelemetry(@PathVariable Long deviceTypeTelemetryId) {
        return ResponseEntity.ok(service.getTelemetryById(deviceTypeTelemetryId));
    }

    @PatchMapping("/device-type-telemetries/{deviceTypeTelemetryId}")
    public ResponseEntity<DeviceTypeTelemetry> updateDeviceTypeTelemetry(
            @PathVariable Long deviceTypeTelemetryId,
            @Valid @RequestBody DeviceTypeTelemetryUpdate dto) {
        return ResponseEntity.ok(service.updateTelemetry(deviceTypeTelemetryId, dto));
    }

    @DeleteMapping("/device-type-telemetries/{deviceTypeTelemetryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeviceTypeTelemetry(@PathVariable Long deviceTypeTelemetryId) {
        service.deleteTelemetry(deviceTypeTelemetryId);
    }

    @GetMapping("/device-types/{deviceTypeId}/telemetries")
    public ResponseEntity<List<DeviceTypeTelemetry>> getDeviceTypeTelemetriesByType(@PathVariable Long deviceTypeId) {
        return ResponseEntity.ok(service.getTelemetriesByDeviceTypeId(deviceTypeId));
    }
}