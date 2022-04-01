package com.weather.temperatureservice.infrastructure.amqp;

import com.weather.temperatureservice.infrastructure.amqp.event.TemperatureDataEvent;
import com.weather.temperatureservice.infrastructure.repository.TemperatureRepository;
import com.weather.temperatureservice.infrastructure.repository.entity.TemperatureEntity;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.inject.Inject;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(initializers = {GetTemperatureEventIT.Initializer.class})
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class GetTemperatureEventIT {

    @Inject
    private RabbitTemplate rabbitTemplate;

    @Inject
    private TemperatureRepository temperatureRepository;

    private static final String TEST_QUEUE = "test-queue";
    private static final String TEST_REPLY_QUEUE = "test-reply-queue";
    private static final String TEST_EXCHANGE = "test-exchange";
    private static final String TEST_ROUTING_KEY = "test-rk-queue";
    private static final String EXCHANGE_TYPE_DIRECT = "direct";
    private static final String DESTINATION_TYPE = "queue";
    private static final String DATABASE_NAME = "temperature-db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    @Container
    private final static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management"))
            .withExchange(TEST_EXCHANGE, EXCHANGE_TYPE_DIRECT)
            .withQueue(TEST_QUEUE)
            .withBinding(TEST_EXCHANGE, TEST_QUEUE, Collections.emptyMap(), TEST_ROUTING_KEY, DESTINATION_TYPE);

    @Container
    public final static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:12")
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.rabbitmq.port=" + rabbitMQContainer.getMappedPort(5672),
                    "spring.datasource.username=" + postgresSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgresSQLContainer.getPassword(),
                    "spring.datasource.url=" + postgresSQLContainer.getJdbcUrl(),
                    "amqp.default-queue=" + TEST_QUEUE,
                    "amqp.reply-queue=" + TEST_REPLY_QUEUE
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test void
    receiveTemperatureData() {
        rabbitTemplate.convertAndSend(TEST_EXCHANGE, TEST_ROUTING_KEY, aTemperatureDataEvent());
        TemperatureEntity temperatureEntity = temperatureRepository.findById(1L);
        assertThat(temperatureEntity).isNotNull()
                .extracting(TemperatureEntity::getId, TemperatureEntity::getTemperatureValue, TemperatureEntity::getMeteoDataId)
                .containsExactly(1L, 23f, 1L);
    }

    private TemperatureDataEvent aTemperatureDataEvent() {
        return TemperatureDataEvent.builder().withMeteoDataId(1L).withTemperatureValue(23f).build();
    }
}
