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

    @GetMapping("/device-type-telemetries/{device_type_telemetry_id}")
    public ResponseEntity<DeviceTypeTelemetry> getDeviceTypeTelemetry(@PathVariable Long device_type_telemetry_id) {
        return ResponseEntity.ok(service.getTelemetryById(device_type_telemetry_id));
    }

    @PatchMapping("/device-type-telemetries/{device_type_telemetry_id}")
    public ResponseEntity<DeviceTypeTelemetry> updateDeviceTypeTelemetry(
            @PathVariable Long device_type_telemetry_id,
            @Valid @RequestBody DeviceTypeTelemetryUpdate dto) {
        return ResponseEntity.ok(service.updateTelemetry(device_type_telemetry_id, dto));
    }

    @DeleteMapping("/device-type-telemetries/{device_type_telemetry_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeviceTypeTelemetry(@PathVariable Long device_type_telemetry_id) {
        service.deleteTelemetry(device_type_telemetry_id);
    }

    @GetMapping("/device-types/{device_type_id}/telemetries")
    public ResponseEntity<List<DeviceTypeTelemetry>> getDeviceTypeTelemetriesByType(@PathVariable Long device_type_id) {
        return ResponseEntity.ok(service.getTelemetriesByDeviceTypeId(device_type_id));
    }
}