package ru.yandex.practicum.DeviceService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.DeviceService.entity.DeviceEntity;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
    List<DeviceEntity> findByLocationId(Long locationId);
    boolean existsBySerialNumber(String serialNumber);
    boolean existsByDeviceTypeId(Long deviceTypeId);
}