package ru.yandex.practicum.DeviceService.controller;


import ru.yandex.practicum.DeviceService.dto.*;
import ru.yandex.practicum.DeviceService.service.DeviceTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/device-types")
@RequiredArgsConstructor
public class DeviceTypeController {

    private final DeviceTypeService deviceTypeService;

    @GetMapping
    public ResponseEntity<List<DeviceType>> getDeviceTypes() {
        return ResponseEntity.ok(deviceTypeService.getAllDeviceTypes());
    }

    @PostMapping
    public ResponseEntity<DeviceType> createDeviceType(@Valid @RequestBody DeviceTypeCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceTypeService.createDeviceType(dto));
    }

    @GetMapping("/{device_type_id}")
    public ResponseEntity<DeviceTypeWithDetails> getDeviceTypeById(@PathVariable("device_type_id") Long id) {
        return ResponseEntity.ok(deviceTypeService.getDeviceTypeWithDetails(id));
    }

    @PatchMapping("/{device_type_id}")
    public ResponseEntity<DeviceType> updateDeviceTypeName(
            @PathVariable("device_type_id") Long id,
            @Valid @RequestBody DeviceTypeNameUpdate dto) {
        return ResponseEntity.ok(deviceTypeService.updateDeviceTypeName(id, dto));
    }

    @DeleteMapping("/{device_type_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeviceType(@PathVariable("device_type_id") Long id) {
        deviceTypeService.deleteDeviceType(id);
    }
}