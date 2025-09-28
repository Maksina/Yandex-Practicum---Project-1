package ru.yandex.practicum.TelemetryService.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TelemetryPoint {

    private Instant time;
    private Float temperature;
    private Float humidity;
    private Float pressure;

    // Getters and Setters
    public Instant getTime() { return time; }
    public void setTime(Instant time) { this.time = time; }

    public Float getTemperature() { return temperature; }
    public void setTemperature(Float temperature) { this.temperature = temperature; }

    public Float getHumidity() { return humidity; }
    public void setHumidity(Float humidity) { this.humidity = humidity; }

    public Float getPressure() { return pressure; }
    public void setPressure(Float pressure) { this.pressure = pressure; }
}