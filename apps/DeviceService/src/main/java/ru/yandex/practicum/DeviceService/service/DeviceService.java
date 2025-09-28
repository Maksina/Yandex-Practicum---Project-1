package ru.yandex.practicum.DeviceService.service;

import ru.yandex.practicum.DeviceService.dto.*;
import ru.yandex.practicum.DeviceService.entity.DeviceEntity;
import ru.yandex.practicum.DeviceService.exception.ConflictException;
import ru.yandex.practicum.DeviceService.exception.DeviceNotFoundException;
import ru.yandex.practicum.DeviceService.exception.DeviceTypeNotFoundException;
import ru.yandex.practicum.DeviceService.repository.DeviceRepository;
import ru.yandex.practicum.DeviceService.repository.DeviceTypeCommandRepository;
import ru.yandex.practicum.DeviceService.repository.DeviceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final DeviceTypeCommandRepository commandRepository;

    public List<Device> getDevicesByLocation(Long locationId) {
        return deviceRepository.findByLocationId(locationId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Device createDevice(DeviceCreate dto) {
        if (deviceRepository.existsBySerialNumber(dto.getSerialNumber())) {
            throw new ConflictException("Device with serial number '" + dto.getSerialNumber() + "' already exists");
        }
//        if (!deviceTypeRepository.existsById(dto.getDeviceTypeId())) {
//            throw new DeviceTypeNotFoundException("Device type not found");
//        }
        DeviceEntity entity = new DeviceEntity();
        entity.setName(dto.getName());
        entity.setLocationId(dto.getLocationId());
        entity.setDeviceTypeId(dto.getDeviceTypeId());
        entity.setSerialNumber(dto.getSerialNumber());
        entity.setIsActive(true);
        DeviceEntity saved = deviceRepository.save(entity);
        return toDto(saved);
    }

    public Device getDeviceById(Long id) {
        DeviceEntity entity = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));
        return toDto(entity);
    }

    @Transactional
    public Device updateDeviceName(Long deviceId, DeviceNameUpdate dto) {
        DeviceEntity entity = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));
        entity.setName(dto.getName());
        DeviceEntity updated = deviceRepository.save(entity);
        return toDto(updated);
    }

    @Transactional
    public void deleteDevice(Long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new DeviceNotFoundException("Device not found");
        }
        deviceRepository.deleteById(deviceId);
    }

    public void sendCommandToDevice(Long deviceId, DeviceCommand commandDto) {
        DeviceEntity device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found"));
        boolean supported = commandRepository.existsByDeviceTypeIdAndName(
                device.getDeviceTypeId(), commandDto.getCommandName()
        );
        if (!supported) {
            throw new ConflictException("Command '" + commandDto.getCommandName() + "' is not supported for device type " + device.getDeviceTypeId());
        }
        // В реальной системе: отправка в очередь (Kafka/RabbitMQ)
    }

    private Device toDto(DeviceEntity entity) {
        Device dto = new Device();
        dto.setDeviceId(entity.getDeviceId());
        dto.setName(entity.getName());
        dto.setLocationId(entity.getLocationId());
        dto.setDeviceTypeId(entity.getDeviceTypeId());
        dto.setIsActive(entity.getIsActive());
        dto.setSerialNumber(entity.getSerialNumber());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}