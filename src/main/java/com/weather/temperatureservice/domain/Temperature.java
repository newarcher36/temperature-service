package com.weather.temperatureservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder(setterPrefix = "with")
@Getter
@AllArgsConstructor
@ToString
public class Temperature {
    private Long id;
    private Long meteoDataId;
    private Float temperatureValue;
}
