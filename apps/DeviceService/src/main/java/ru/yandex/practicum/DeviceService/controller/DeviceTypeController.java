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

    @GetMapping("/{deviceTypeId}")
    public ResponseEntity<DeviceTypeWithDetails> getDeviceTypeById(@PathVariable("deviceTypeId") Long id) {
        return ResponseEntity.ok(deviceTypeService.getDeviceTypeWithDetails(id));
    }

    @PatchMapping("/{deviceTypeId}")
    public ResponseEntity<DeviceType> updateDeviceTypeName(
            @PathVariable("deviceTypeId") Long id,
            @Valid @RequestBody DeviceTypeNameUpdate dto) {
        return ResponseEntity.ok(deviceTypeService.updateDeviceTypeName(id, dto));
    }

    @DeleteMapping("/{deviceTypeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeviceType(@PathVariable("deviceTypeId") Long id) {
        deviceTypeService.deleteDeviceType(id);
    }
}