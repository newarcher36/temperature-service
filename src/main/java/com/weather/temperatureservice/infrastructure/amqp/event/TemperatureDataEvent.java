package com.weather.temperatureservice.infrastructure.amqp.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class TemperatureDataEvent {
    private Long meteoDataId;
    private Float temperatureValue;
}
