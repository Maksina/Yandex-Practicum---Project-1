package ru.yandex.practicum.DeviceService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.DeviceService.entity.DeviceTypeTelemetryEntity;

import java.util.List;

@Repository
public interface DeviceTypeTelemetryRepository extends JpaRepository<DeviceTypeTelemetryEntity, Long> {
    List<DeviceTypeTelemetryEntity> findByDeviceTypeId(Long deviceTypeId);
    boolean existsByDeviceTypeIdAndName(Long deviceTypeId, String name);
}