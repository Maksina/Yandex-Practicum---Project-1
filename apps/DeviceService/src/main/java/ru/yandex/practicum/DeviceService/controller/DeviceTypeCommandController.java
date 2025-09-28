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

    @GetMapping("/device-type-commands/{device_type_command_id}")
    public ResponseEntity<DeviceTypeCommand> getDeviceTypeCommand(@PathVariable Long device_type_command_id) {
        return ResponseEntity.ok(service.getCommandById(device_type_command_id));
    }

    @PatchMapping("/device-type-commands/{device_type_command_id}")
    public ResponseEntity<DeviceTypeCommand> updateDeviceTypeCommand(
            @PathVariable Long device_type_command_id,
            @Valid @RequestBody DeviceTypeCommandUpdate dto) {
        return ResponseEntity.ok(service.updateCommand(device_type_command_id, dto));
    }

    @DeleteMapping("/device-type-commands/{device_type_command_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeviceTypeCommand(@PathVariable Long device_type_command_id) {
        service.deleteCommand(device_type_command_id);
    }

    @GetMapping("/device-types/{device_type_id}/commands")
    public ResponseEntity<List<DeviceTypeCommand>> getDeviceTypeCommandsByType(@PathVariable Long device_type_id) {
        return ResponseEntity.ok(service.getCommandsByDeviceTypeId(device_type_id));
    }
}