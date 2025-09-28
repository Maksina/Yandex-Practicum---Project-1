package ru.yandex.practicum.DeviceService.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeviceType {
    private Long deviceTypeId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}