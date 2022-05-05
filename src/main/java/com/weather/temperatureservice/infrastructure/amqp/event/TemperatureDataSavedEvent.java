package com.weather.temperatureservice.infrastructure.amqp.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder(setterPrefix = "with")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TemperatureDataSavedEvent {
    private Long id;
    private Long meteoDataId;
    private Float temperatureValue;
}
