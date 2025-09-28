package ru.yandex.practicum.DeviceService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceTypeTelemetryUpdate {
    @NotBlank
    private String name;
    @NotBlank
    private String telemetryType;
}
