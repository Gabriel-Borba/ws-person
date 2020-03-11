package com.ws.person.service;

import com.ws.person.Model.Dto.PersonDto;

public interface RabbitMqService {
    Boolean sendAccountQueue(PersonDto personDto);

}
