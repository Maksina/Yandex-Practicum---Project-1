package ru.yandex.practicum.DeviceService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceTypeNameUpdate {
    @NotBlank
    private String name;
}