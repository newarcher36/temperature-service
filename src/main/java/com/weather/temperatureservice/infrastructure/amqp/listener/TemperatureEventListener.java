package com.weather.temperatureservice.infrastructure.amqp.listener;

import com.weather.temperatureservice.application.usecase.SaveTemperature;
import com.weather.temperatureservice.domain.Temperature;
import com.weather.temperatureservice.infrastructure.amqp.event.TemperatureDataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TemperatureEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemperatureEventListener.class);
    private final SaveTemperature saveTemperature;

    public TemperatureEventListener(SaveTemperature saveTemperature) {
        this.saveTemperature = saveTemperature;
    }

    @RabbitListener(queues = "${amqp.default-queue}")
    public void listenTemperatureDataEvents(TemperatureDataEvent temperatureDataEvent) {
        LOGGER.info("TemperatureDataEvent received : " + temperatureDataEvent);
        saveTemperature.save(map(temperatureDataEvent));
    }

    private Temperature map(TemperatureDataEvent temperatureDataEvent) {
        return Temperature.builder().withMeteoDataId(temperatureDataEvent.getMeteoDataId())
                .withTemperatureValue(temperatureDataEvent.getTemperatureValue())
                .build();
    }
}
