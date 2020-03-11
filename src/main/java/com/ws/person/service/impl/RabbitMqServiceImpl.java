package com.ws.person.service.impl;

import com.google.gson.Gson;
import com.ws.person.Model.Dto.PersonDto;
import com.ws.person.service.RabbitMqService;
import com.ws.person.config.rabbitmq.RabbitConfig;
import org.slf4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RabbitMqServiceImpl implements RabbitMqService {
    private static Logger logger = getLogger(RabbitMqServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;
    private RabbitConfig rabbitConfig;

    @Autowired
    public RabbitMqServiceImpl(RabbitTemplate rabbitTemplate,
                               AmqpAdmin amqpAdmin,
                               RabbitConfig rabbitConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
        this.rabbitConfig = rabbitConfig;
    }

    @PostConstruct
    public void setupRabbit() {
        amqpAdmin.declareExchange(rabbitConfig.exchange());
        amqpAdmin.declareQueue(rabbitConfig.pushQueue());
        amqpAdmin.declareBinding(rabbitConfig.bindingPushQueue());

    }

    public Boolean sendAccountQueue(PersonDto personDto) {
        logger.info("Sending to account queue: " + personDto.toString());
        try {
            String personJson = new Gson().toJson(personDto);
            rabbitTemplate.convertAndSend(this.rabbitConfig.accountQueue, personJson);
            logger.info("Successfully sended to account queue: " + personDto.toString());
            return true;
        } catch (AmqpException ex) {
            logger.error(ex.getMessage());
            logger.error("Error for sending to account queue: " + personDto.toString());
            return false;
        }
    }

}
