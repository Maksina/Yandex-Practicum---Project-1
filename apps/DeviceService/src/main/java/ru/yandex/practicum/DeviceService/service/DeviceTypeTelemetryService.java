package ru.yandex.practicum.DeviceService.service;



import ru.yandex.practicum.DeviceService.dto.DeviceTypeTelemetry;
import ru.yandex.practicum.DeviceService.dto.DeviceTypeTelemetryCreate;
import ru.yandex.practicum.DeviceService.dto.DeviceTypeTelemetryUpdate;
import ru.yandex.practicum.DeviceService.entity.DeviceTypeTelemetryEntity;
import ru.yandex.practicum.DeviceService.exception.ConflictException;
import ru.yandex.practicum.DeviceService.exception.DeviceTypeNotFoundException;
import ru.yandex.practicum.DeviceService.repository.DeviceTypeRepository;
import ru.yandex.practicum.DeviceService.repository.DeviceTypeTelemetryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceTypeTelemetryService {

    private final DeviceTypeTelemetryRepository repository;
    private final DeviceTypeRepository deviceTypeRepository;

    @Transactional
    public DeviceTypeTelemetry createTelemetry(DeviceTypeTelemetryCreate dto) {
        if (!deviceTypeRepository.existsById(dto.getDeviceTypeId())) {
            throw new DeviceTypeNotFoundException("Device type not found");
        }
        if (repository.existsByDeviceTypeIdAndName(dto.getDeviceTypeId(), dto.getName())) {
            throw new ConflictException("Telemetry '" + dto.getName() + "' already exists for this device type");
        }
        DeviceTypeTelemetryEntity entity = new DeviceTypeTelemetryEntity();
        entity.setDeviceTypeId(dto.getDeviceTypeId());
        entity.setName(dto.getName());
        entity.setTelemetryType(dto.getTelemetryType());
        DeviceTypeTelemetryEntity saved = repository.save(entity);
        return toDto(saved);
    }

    public DeviceTypeTelemetry getTelemetryById(Long id) {
        DeviceTypeTelemetryEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Telemetry not found"));
        return toDto(entity);
    }

    @Transactional
    public DeviceTypeTelemetry updateTelemetry(Long id, DeviceTypeTelemetryUpdate dto) {
        DeviceTypeTelemetryEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Telemetry not found"));
        if (!dto.getName().equals(entity.getName()) &&
                repository.existsByDeviceTypeIdAndName(entity.getDeviceTypeId(), dto.getName())) {
            throw new ConflictException("Telemetry with name '" + dto.getName() + "' already exists for this device type");
        }
        entity.setName(dto.getName());
        entity.setTelemetryType(dto.getTelemetryType());
        DeviceTypeTelemetryEntity updated = repository.save(entity);
        return toDto(updated);
    }

    @Transactional
    public void deleteTelemetry(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Telemetry not found");
        }
        repository.deleteById(id);
    }

    public List<DeviceTypeTelemetry> getTelemetriesByDeviceTypeId(Long deviceTypeId) {
        return repository.findByDeviceTypeId(deviceTypeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private DeviceTypeTelemetry toDto(DeviceTypeTelemetryEntity e) {
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