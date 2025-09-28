package ru.yandex.practicum.DeviceService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceNameUpdate {
    @NotBlank
    private String name;
}