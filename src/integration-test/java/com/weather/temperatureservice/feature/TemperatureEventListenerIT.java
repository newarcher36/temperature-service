package com.weather.temperatureservice.feature;

import com.weather.temperatureservice.infrastructure.repository.TemperatureRepository;
import com.weather.temperatureservice.infrastructure.repository.entity.Temperature;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.inject.Inject;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(initializers = {TemperatureEventListenerIT.Initializer.class})
public class TemperatureEventListenerIT {

    @Inject
    private RabbitTemplate rabbitTemplate;

    @Inject
    private TemperatureRepository temperatureRepository;

    private static final String TEST_QUEUE = "test-queue";
    private static final String TEST_EXCHANGE = "test-exchange";
    private static final String TEST_BINDING_KEY = "test-queue";
    private static final String TEST_QUEUE_DLQ = "test-queue-dlq";
    private static final String EXCHANGE_TYPE_DIRECT = "direct";
    private static final String DESTINATION_TYPE = "queue";
    private static final String DATABASE_NAME = "temperature-db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    @Container
    private final static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(DockerImageName.parse("newarcher/rabbitmq:v1").asCompatibleSubstituteFor("rabbitmq"))
            .withImagePullPolicy(PullPolicy.alwaysPull())
            .withExchange(TEST_EXCHANGE, EXCHANGE_TYPE_DIRECT)
            .withQueue(TEST_QUEUE)
            .withQueue(TEST_QUEUE_DLQ)
            .withBinding(TEST_EXCHANGE, TEST_QUEUE, Collections.emptyMap(), TEST_BINDING_KEY, DESTINATION_TYPE);

    @Container
    public final static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12")
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.rabbitmq.port=" + rabbitMQContainer.getMappedPort(5672),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "amqp.queue=" + TEST_QUEUE,
                    "amqp.dlq=" + TEST_QUEUE_DLQ
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test void
    receiveTemperatureData() {
        rabbitTemplate.convertAndSend(TEST_EXCHANGE, TEST_QUEUE, "{meteoDataId: 1, temperatureValue : 20}");
        Temperature temperature = temperatureRepository.findById(1L);
        assertThat(temperature).isNotNull();
    }
}
