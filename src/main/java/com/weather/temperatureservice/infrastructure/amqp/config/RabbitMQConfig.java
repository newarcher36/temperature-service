package com.weather.temperatureservice.infrastructure.amqp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queue(RabbitTemplate rabbitTemplate) {
        return QueueBuilder.durable(rabbitTemplate.getDefaultReceiveQueue()).build();
    }

    @Bean
    public DirectExchange exchange(RabbitTemplate rabbitTemplate) {
        return new DirectExchange(rabbitTemplate.getExchange());
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange, RabbitTemplate rabbitTemplate) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(rabbitTemplate.getRoutingKey());
    }
}