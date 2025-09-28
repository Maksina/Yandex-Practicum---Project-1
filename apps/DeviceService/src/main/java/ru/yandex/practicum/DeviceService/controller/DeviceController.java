package ru.yandex.practicum.DeviceService.controller;

import ru.yandex.practicum.DeviceService.dto.*;
import ru.yandex.practicum.DeviceService.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<Device>> getDevices(@RequestParam Long location_id) {
        return ResponseEntity.ok(deviceService.getDevicesByLocation(location_id));
    }

    @PostMapping
    public ResponseEntity<Device> createDevice(@Valid @RequestBody DeviceCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceService.createDevice(dto));
    }

    @GetMapping("/{device_id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable("device_id") Long deviceId) {
        return ResponseEntity.ok(deviceService.getDeviceById(deviceId));
    }

    @PatchMapping("/{device_id}")
    public ResponseEntity<Device> updateDeviceName(
            @PathVariable("device_id") Long deviceId,
            @Valid @RequestBody DeviceNameUpdate dto) {
        return ResponseEntity.ok(deviceService.updateDeviceName(deviceId, dto));
    }

    @DeleteMapping("/{device_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDevice(@PathVariable("device_id") Long deviceId) {
        deviceService.deleteDevice(deviceId);
    }

    @PostMapping("/{device_id}/commands")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendCommand(
            @PathVariable("device_id") Long deviceId,
            @Valid @RequestBody DeviceCommand commandDto) {
        deviceService.sendCommandToDevice(deviceId, commandDto);
    }
}