package ru.yandex.practicum.DeviceService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceCommand {
    @NotBlank
    private String commandName;
}