package com.weather.temperatureservice.infrastructure.amqp.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeadLetterListener.class);

    @RabbitListener(queues = "${amqp.dlq}")
    public void listenFailedMessages(Message message) {
        LOGGER.info("Failed message received : " + message);
    }
}
