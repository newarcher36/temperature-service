package com.weather.temperatureservice.infrastructure.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder(setterPrefix = "with")
@Getter
@AllArgsConstructor
@ToString
public class TemperatureEntity {
    private Long meteoDataId;
    private Float temperatureValue;
}
