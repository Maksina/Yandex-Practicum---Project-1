package ru.yandex.practicum.DeviceService.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Device {
    private Long deviceId;
    private String name;
    private Long locationId;
    private Long deviceTypeId;
    private Boolean isActive;
    private String serialNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}