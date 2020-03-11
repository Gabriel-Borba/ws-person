package com.ws.person.service;

import com.ws.person.Model.Dto.PersonDto;
import com.ws.person.Model.Dto.ResponseDto;
import com.ws.person.repository.PersonRepository;
import com.ws.person.service.impl.PersonServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;


import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

public class PersonServiceImplTests {

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private RabbitMqService rabbitMqService;


    private PersonServiceImpl personService;

    @Before
    public void setUp() throws Exception {
        this.personService = new PersonServiceImpl(personRepository, rabbitMqService);
    }

    @Test
    public void mustReturnBadRequestWhenDocumentNumberIsInvalid() {
        //Given
        PersonDto personDto = buildInvalidPerson();
        //when
        Throwable exception = assertThrows(ResponseStatusException.class, () -> personService.savePerson(personDto));
        //Then
        assertEquals("400 BAD_REQUEST", exception.getMessage());

    }

    private PersonDto buildInvalidPerson() {
        return PersonDto.builder().type("PF").documentNumber("123425678901").score(5).name("Alberto Silva").build();

    }
}
