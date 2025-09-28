package ru.yandex.practicum.DeviceService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.DeviceService.entity.DeviceTypeEntity;

@Repository
public interface DeviceTypeRepository extends JpaRepository<DeviceTypeEntity, Long> {}