package ru.yandex.practicum.DeviceService.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeviceTypeTelemetry {
    private Long deviceTypeTelemetryId;
    private Long deviceTypeId;
    private String telemetryType;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}