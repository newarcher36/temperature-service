package com.weather.temperatureservice.infrastructure.amqp.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder(setterPrefix = "with")
@Getter
@AllArgsConstructor
@ToString
public class TemperatureDataEvent {
    private Long meteoDataId;
    private Float temperatureValue;
}