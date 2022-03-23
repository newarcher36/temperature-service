package com.weather.temperatureservice.feature;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.inject.Inject;

@SpringBootTest
@Testcontainers
public class TemperatureEventListenerIT {

    @Inject
    private RabbitProperties rabbitProperties;

    @Inject
    private RabbitTemplate rabbitTemplate;

    @Container
    public RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management"))
            .withExchange("meteodata_exchange","direct")
            .withQueue("temperaturedata")
            .withUser("testUser", "testPass");

    @BeforeEach
    public void init() {
        Integer mappedPort = rabbitMQContainer.getMappedPort(5672);
        RabbitTemplateConfigurer rabbitTemplateConfigurer = new RabbitTemplateConfigurer(rabbitProperties);
        rabbitTemplateConfigurer.configure(rabbitTemplate, new CachingConnectionFactory("localhost", mappedPort));
    }

    @Test void
    receiveTemperatureData() {
        rabbitTemplate.convertAndSend("{ val: 123 }");
    }
}
