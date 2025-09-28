package ru.yandex.practicum.DeviceService.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeviceTypeCommand {
    private Long deviceTypeCommandId;
    private Long deviceTypeId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}