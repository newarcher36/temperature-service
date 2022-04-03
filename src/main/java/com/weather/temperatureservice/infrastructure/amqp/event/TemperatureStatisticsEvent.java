package com.weather.temperatureservice.infrastructure.amqp.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Builder(setterPrefix = "with")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TemperatureStatisticsEvent implements Serializable {
    private Float currentTemperature;
    private Float avgTemperature;
    private Float maxTemperature;
    private Float minTemperature;
}
