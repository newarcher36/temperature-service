package com.weather.temperatureservice.infrastructure.amqp;

import com.weather.temperatureservice.infrastructure.amqp.event.SaveTemperatureDataEvent;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
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
@ContextConfiguration(initializers = {GetTemperatureIT.Initializer.class})
@Sql(scripts = {"/schema.sql" ,""}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class GetTemperatureIT {

    private static final String GET_TEMPERATURE_QUEUE = "get-temperature";
    // TODO : find a workaround to execute script to create rabbitmq objects in test containers
    private static final String SAVE_TEMPERATURE_QUEUE = "save-temperature";
    private static final String TEMPERATURE_EXCHANGE = "temperature-exchange";
    private static final String TEMPERATURE_ROUTING_KEY = "temperature-rk-queue";
    private static final String EXCHANGE_TYPE_DIRECT = "direct";
    private static final String DESTINATION_TYPE = "queue";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    @Inject
    private RabbitTemplate rabbitTemplate;

    @Container
    private final static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management"))
            .withExchange(TEMPERATURE_EXCHANGE, EXCHANGE_TYPE_DIRECT)
            .withQueue(GET_TEMPERATURE_QUEUE)
            .withQueue(SAVE_TEMPERATURE_QUEUE)
            .withBinding(TEMPERATURE_EXCHANGE, GET_TEMPERATURE_QUEUE, Collections.emptyMap(), TEMPERATURE_ROUTING_KEY, DESTINATION_TYPE);

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
                    "spring.datasource.url=" + postgresSQLContainer.getJdbcUrl()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test void
    getTemperatureData() {
        SaveTemperatureDataEvent saveTemperatureDataEvent = rabbitTemplate.convertSendAndReceiveAsType(TEMPERATURE_EXCHANGE, TEMPERATURE_ROUTING_KEY, aTemperatureDataEvent(), new ParameterizedTypeReference<SaveTemperatureDataEvent>() {});
        assertThat(saveTemperatureDataEvent)
                .isNotNull()
                .extracting(SaveTemperatureDataEvent::getMeteoDataId, SaveTemperatureDataEvent::getTemperatureValue)
                .containsExactly(1L, 23f);
    }

    private SaveTemperatureDataEvent aTemperatureDataEvent() {
        return SaveTemperatureDataEvent.builder().withMeteoDataId(1L).withTemperatureValue(23f).build();
    }
}
