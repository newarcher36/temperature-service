package com.weather.temperatureservice.feature.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTestConfig {

    @Value("${amqp.queue.name}")
    private String queueName;

    @Bean
    public Queue queue(RabbitTemplate rabbitTemplate) {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange","")
                .withArgument("x-dead-letter-routing-key", queueName + ".dlq")
                .build();
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

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(queueName + ".dlq").build();
    }

//    @Bean
//    public DirectExchange deadLetterExchange() {
//        return new DirectExchange(queueName + ".dlx");
//    }
//
//
//    @Bean
//    public Binding deadLetterBinding() {
//        return BindingBuilder
//                .bind(deadLetterQueue())
//                .to(deadLetterExchange())
//                .with(queueName + ".dlq");
//    }
}