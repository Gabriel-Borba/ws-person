package com.ws.person.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {


    @Value("${rabbit.account.queue}")
    public String accountQueue;

    @Value("${rabbit.bank.exchange}")
    public String exchange;


    @Bean
    public Queue pushQueue() {
        return QueueBuilder.durable(accountQueue).build();
    }

    @Bean
    public DirectExchange exchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(exchange).durable(true).build();
    }


    @Bean
    public Binding bindingPushQueue() {
        return BindingBuilder.bind(this.pushQueue()).to(this.exchange()).with(this.accountQueue);
    }



}
