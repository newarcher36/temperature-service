package com.weather.temperatureservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder(setterPrefix = "with")
@Getter
@AllArgsConstructor
@ToString
public class TemperatureStatistics {
    private Float currentTemperature;
    private Float avgTemperature;
    private Float maxTemperature;
    private Float minTemperature;
}
