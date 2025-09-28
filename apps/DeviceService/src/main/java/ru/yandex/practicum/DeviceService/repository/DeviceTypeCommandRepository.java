package ru.yandex.practicum.DeviceService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.DeviceService.entity.DeviceTypeCommandEntity;

import java.util.List;

@Repository
public interface DeviceTypeCommandRepository extends JpaRepository<DeviceTypeCommandEntity, Long> {
    List<DeviceTypeCommandEntity> findByDeviceTypeId(Long deviceTypeId);
    boolean existsByDeviceTypeIdAndName(Long deviceTypeId, String name);
}