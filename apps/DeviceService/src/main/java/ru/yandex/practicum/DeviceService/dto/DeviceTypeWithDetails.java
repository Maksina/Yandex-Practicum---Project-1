package ru.yandex.practicum.DeviceService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DeviceTypeWithDetails {
    private Long deviceTypeId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DeviceTypeCommand> commands;
    private List<DeviceTypeTelemetry> telemetries;
}