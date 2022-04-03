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
public class SaveTemperatureDataEvent implements Serializable {
    private Long meteoDataId;
    private Float temperatureValue;
}
