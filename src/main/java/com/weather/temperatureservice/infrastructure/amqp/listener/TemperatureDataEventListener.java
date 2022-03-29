package com.weather.temperatureservice.infrastructure.amqp.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TemperatureDataEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemperatureDataEventListener.class);

    @RabbitListener(queues = "${amqp.queue}")
    public void listenTemperatureDataEvents(Message message) {
        LOGGER.info("TemperatureDataEvent received : " + message);
    }
}
