package com.weather.temperatureservice.infrastructure.amqp.listener;

import com.weather.temperatureservice.application.usecase.SaveTemperature;
import com.weather.temperatureservice.domain.Temperature;
import com.weather.temperatureservice.infrastructure.amqp.event.SaveTemperatureDataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SaveTemperatureEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveTemperatureEventListener.class);
    private final SaveTemperature saveTemperature;

    public SaveTemperatureEventListener(SaveTemperature saveTemperature) {
        this.saveTemperature = saveTemperature;
    }

    @RabbitListener(queues = "${amqp.save-temperature-queue}")
    public void listenTemperatureDataEvents(SaveTemperatureDataEvent saveTemperatureDataEvent) {
        LOGGER.info("TemperatureDataEvent received : " + saveTemperatureDataEvent);
        saveTemperature.save(map(saveTemperatureDataEvent));
    }

    private Temperature map(SaveTemperatureDataEvent saveTemperatureDataEvent) {
        return Temperature.builder().withMeteoDataId(saveTemperatureDataEvent.getMeteoDataId())
                .withTemperatureValue(saveTemperatureDataEvent.getTemperatureValue())
                .build();
    }
}
