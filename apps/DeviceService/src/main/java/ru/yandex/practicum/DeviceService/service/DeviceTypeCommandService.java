package ru.yandex.practicum.DeviceService.service;

import ru.yandex.practicum.DeviceService.dto.DeviceTypeCommand;
import ru.yandex.practicum.DeviceService.dto.DeviceTypeCommandCreate;
import ru.yandex.practicum.DeviceService.dto.DeviceTypeCommandUpdate;
import ru.yandex.practicum.DeviceService.entity.DeviceTypeCommandEntity;
import ru.yandex.practicum.DeviceService.exception.ConflictException;
import ru.yandex.practicum.DeviceService.exception.DeviceTypeNotFoundException;
import ru.yandex.practicum.DeviceService.repository.DeviceTypeCommandRepository;
import ru.yandex.practicum.DeviceService.repository.DeviceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceTypeCommandService {

    private final DeviceTypeCommandRepository repository;
    private final DeviceTypeRepository deviceTypeRepository;

    @Transactional
    public DeviceTypeCommand createCommand(DeviceTypeCommandCreate dto) {
        if (!deviceTypeRepository.existsById(dto.getDeviceTypeId())) {
            throw new DeviceTypeNotFoundException("Device type not found");
        }
        if (repository.existsByDeviceTypeIdAndName(dto.getDeviceTypeId(), dto.getName())) {
            throw new ConflictException("Command '" + dto.getName() + "' already exists for this device type");
        }
        DeviceTypeCommandEntity entity = new DeviceTypeCommandEntity();
        entity.setDeviceTypeId(dto.getDeviceTypeId());
        entity.setName(dto.getName());
        DeviceTypeCommandEntity saved = repository.save(entity);
        return toDto(saved);
    }

    public DeviceTypeCommand getCommandById(Long id) {
        DeviceTypeCommandEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Command not found"));
        return toDto(entity);
    }

    @Transactional
    public DeviceTypeCommand updateCommand(Long id, DeviceTypeCommandUpdate dto) {
        DeviceTypeCommandEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Command not found"));
        if (!dto.getName().equals(entity.getName()) &&
                repository.existsByDeviceTypeIdAndName(entity.getDeviceTypeId(), dto.getName())) {
            throw new ConflictException("Command with name '" + dto.getName() + "' already exists for this device type");
        }
        entity.setName(dto.getName());
        DeviceTypeCommandEntity updated = repository.save(entity);
        return toDto(updated);
    }

    @Transactional
    public void deleteCommand(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Command not found");
        }
        repository.deleteById(id);
    }

    public List<DeviceTypeCommand> getCommandsByDeviceTypeId(Long deviceTypeId) {
        return repository.findByDeviceTypeId(deviceTypeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private DeviceTypeCommand toDto(DeviceTypeCommandEntity e) {
        DeviceTypeCommand dto = new DeviceTypeCommand();
        dto.setDeviceTypeCommandId(e.getDeviceTypeCommandId());
        dto.setDeviceTypeId(e.getDeviceTypeId());
        dto.setName(e.getName());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }
}