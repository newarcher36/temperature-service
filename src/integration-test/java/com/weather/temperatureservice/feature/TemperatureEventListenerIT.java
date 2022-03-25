package com.weather.temperatureservice.feature;

import com.weather.temperatureservice.infrastructure.amqp.listener.TemperatureDataEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.inject.Inject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(initializers = {TemperatureEventListenerIT.Initializer.class})
public class TemperatureEventListenerIT {

    @Inject
    private RabbitTemplate rabbitTemplate;

    @Spy
    private TemperatureDataEventListener temperatureDataEventListener;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Container
    private final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management"));

    @Container
    public final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.rabbitmq.port=" + 5672
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test void
    receiveTemperatureData() {
        rabbitTemplate.convertAndSend("{meteoDataId: 1, temperatureValue : 20}");

//        await()
//                .atMost(10, TimeUnit.SECONDS)
//                .until(isMessagePublishedInQueue());

        verify(temperatureDataEventListener).listenTemperatureDataEvents(any(Message.class));
    }

//    private Callable<Boolean> isMessagePublishedInQueue() {
//        return () -> new RabbitAdmin(rabbitTemplate).getQueueInfo(rabbitTemplate.getDefaultReceiveQueue()).getMessageCount() > 1;
//    }
}
