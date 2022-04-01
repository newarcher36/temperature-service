package com.weather.temperatureservice.infrastructure.postgres;

import com.weather.temperatureservice.infrastructure.repository.TemperatureRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.inject.Inject;

@Ignore("TODO")
@Testcontainers
@ContextConfiguration(initializers = {TemperatureRepositoryIT.Initializer.class})
public class TemperatureRepositoryIT {

    @Inject
    private TemperatureRepository temperatureRepository;

    private static final String DATABASE_NAME = "temperature-db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    @Container
    public final static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12")
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test void
    findTemperatureDataById() {

    }
}
