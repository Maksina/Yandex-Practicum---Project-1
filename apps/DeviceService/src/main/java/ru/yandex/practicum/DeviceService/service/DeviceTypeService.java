package ru.yandex.practicum.DeviceService.service;

import ru.yandex.practicum.DeviceService.dto.*;
import ru.yandex.practicum.DeviceService.entity.DeviceTypeEntity;
import ru.yandex.practicum.DeviceService.exception.ConflictException;
import ru.yandex.practicum.DeviceService.exception.DeviceTypeNotFoundException;
import ru.yandex.practicum.DeviceService.repository.DeviceRepository;
import ru.yandex.practicum.DeviceService.repository.DeviceTypeRepository;
import ru.yandex.practicum.DeviceService.repository.DeviceTypeCommandRepository;
import ru.yandex.practicum.DeviceService.repository.DeviceTypeTelemetryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceTypeService {

    private final DeviceTypeRepository deviceTypeRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceTypeCommandRepository commandRepository;
    private final DeviceTypeTelemetryRepository telemetryRepository;

    public List<DeviceType> getAllDeviceTypes() {
        return deviceTypeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeviceType createDeviceType(DeviceTypeCreate dto) {
        DeviceTypeEntity entity = new DeviceTypeEntity();
        entity.setName(dto.getName());
        DeviceTypeEntity saved = deviceTypeRepository.save(entity);
        return toDto(saved);
    }

    public DeviceTypeWithDetails getDeviceTypeWithDetails(Long id) {
        DeviceTypeEntity entity = deviceTypeRepository.findById(id)
                .orElseThrow(() -> new DeviceTypeNotFoundException("Device type not found"));

        DeviceTypeWithDetails dto = new DeviceTypeWithDetails();
        dto.setDeviceTypeId(entity.getDeviceTypeId());
        dto.setName(entity.getName());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCommands(
                commandRepository.findByDeviceTypeId(id).stream()
                        .map(this::commandToDto)
                        .collect(Collectors.toList())
        );
        dto.setTelemetries(
                telemetryRepository.findByDeviceTypeId(id).stream()
                        .map(this::telemetryToDto)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    @Transactional
    public DeviceType updateDeviceTypeName(Long id, DeviceTypeNameUpdate dto) {
        DeviceTypeEntity entity = deviceTypeRepository.findById(id)
                .orElseThrow(() -> new DeviceTypeNotFoundException("Device type not found"));
        entity.setName(dto.getName());
        DeviceTypeEntity updated = deviceTypeRepository.save(entity);
        return toDto(updated);
    }

    @Transactional
    public void deleteDeviceType(Long id) {
        if (!deviceTypeRepository.existsById(id)) {
            throw new DeviceTypeNotFoundException("Device type not found");
        }
        if (deviceRepository.existsByDeviceTypeId(id)) {
            throw new ConflictException("Cannot delete device type: devices of this type exist");
        }
        deviceTypeRepository.deleteById(id);
    }

    private DeviceType toDto(DeviceTypeEntity entity) {
        DeviceType dto = new DeviceType();
        dto.setDeviceTypeId(entity.getDeviceTypeId());
        dto.setName(entity.getName());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private DeviceTypeCommand commandToDto(ru.yandex.practicum.DeviceService.entity.DeviceTypeCommandEntity e) {
        DeviceTypeCommand dto = new DeviceTypeCommand();
        dto.setDeviceTypeCommandId(e.getDeviceTypeCommandId());
        dto.setDeviceTypeId(e.getDeviceTypeId());
        dto.setName(e.getName());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }

    private DeviceTypeTelemetry telemetryToDto(ru.yandex.practicum.DeviceService.entity.DeviceTypeTelemetryEntity e) {
        DeviceTypeTelemetry dto = new DeviceTypeTelemetry();
        dto.setDeviceTypeTelemetryId(e.getDeviceTypeTelemetryId());
        dto.setDeviceTypeId(e.getDeviceTypeId());
        dto.setTelemetryType(e.getTelemetryType());
        dto.setName(e.getName());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }
}