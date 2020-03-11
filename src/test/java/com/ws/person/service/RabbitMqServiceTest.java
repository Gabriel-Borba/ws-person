package com.ws.person.service;

import com.google.gson.Gson;
import com.ws.person.Model.Dto.PersonDto;
import com.ws.person.config.rabbitmq.RabbitConfig;
import com.ws.person.service.impl.RabbitMqServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.util.ReflectionTestUtils.setField;

public class RabbitMqServiceTest {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private AmqpAdmin amqpAdmin;

    @MockBean
    private RabbitConfig rabbitConfig;

    private RabbitMqService rabbitMqService;

    private static final String ACCOUNT_QUEUE = "account";


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        rabbitTemplate = mock(RabbitTemplate.class);
        amqpAdmin = mock(AmqpAdmin.class);
        rabbitConfig = mock(RabbitConfig.class);
        rabbitMqService = new RabbitMqServiceImpl(rabbitTemplate, amqpAdmin, rabbitConfig);
        setField(rabbitConfig, "accountQueue", ACCOUNT_QUEUE);
    }

    @Test
    public void mustCallSendPersonToAccountQueueAndReturnTrue() {
        //Given
        PersonDto personDto = buildMockPerson();
        String json = new Gson().toJson(personDto);
        doNothing().when(rabbitTemplate).convertAndSend(ACCOUNT_QUEUE, json);
        //When
        boolean result = rabbitMqService.sendAccountQueue(personDto);
        //Then
        verify(rabbitTemplate, times(1)).convertAndSend(ACCOUNT_QUEUE, json);
        assertEquals(result, TRUE);
    }

    @Test
    public void mustCallSendPersonToAccountQueueAndReturnFalseIfIsNotPossibleSendPersonToQueue() {
        //Given
        PersonDto personDto = buildMockPerson();
        String json = new Gson().toJson(personDto);
        doThrow(new AmqpException("Error for publishing.")).when(rabbitTemplate).convertAndSend(ACCOUNT_QUEUE, json);
        //When
        boolean result = rabbitMqService.sendAccountQueue(personDto);
        //Then
        verify(rabbitTemplate, times(1)).convertAndSend(ACCOUNT_QUEUE, json);
        assertEquals(result, FALSE);
    }

    private PersonDto buildMockPerson() {
        return PersonDto.builder().type("PF").documentNumber("12345678901").score(5).name("Alberto Silva").build();
    }
}
