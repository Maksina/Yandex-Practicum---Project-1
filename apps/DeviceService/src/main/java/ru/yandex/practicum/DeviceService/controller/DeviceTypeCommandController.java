package ru.yandex.practicum.DeviceService.controller;

import ru.yandex.practicum.DeviceService.dto.*;
import ru.yandex.practicum.DeviceService.service.DeviceTypeCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DeviceTypeCommandController {

    private final DeviceTypeCommandService service;

    @PostMapping("/device-type-commands")
    public ResponseEntity<DeviceTypeCommand> addDeviceTypeCommand(@Valid @RequestBody DeviceTypeCommandCreate dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCommand(dto));
    }

    @GetMapping("/device-type-commands/{deviceTypeCommandId}")
    public ResponseEntity<DeviceTypeCommand> getDeviceTypeCommand(@PathVariable Long deviceTypeCommandId) {
        return ResponseEntity.ok(service.getCommandById(deviceTypeCommandId));
    }

    @PatchMapping("/device-type-commands/{deviceTypeCommandId}")
    public ResponseEntity<DeviceTypeCommand> updateDeviceTypeCommand(
            @PathVariable Long deviceTypeCommandId,
            @Valid @RequestBody DeviceTypeCommandUpdate dto) {
        return ResponseEntity.ok(service.updateCommand(deviceTypeCommandId, dto));
    }

    @DeleteMapping("/device-type-commands/{deviceTypeCommandId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeviceTypeCommand(@PathVariable Long deviceTypeCommandId) {
        service.deleteCommand(deviceTypeCommandId);
    }

    @GetMapping("/device-types/{deviceTypeId}/commands")
    public ResponseEntity<List<DeviceTypeCommand>> getDeviceTypeCommandsByType(@PathVariable Long deviceTypeId) {
        return ResponseEntity.ok(service.getCommandsByDeviceTypeId(deviceTypeId));
    }
}