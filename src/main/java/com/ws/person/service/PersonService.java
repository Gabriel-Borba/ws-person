package com.ws.person.service;

import com.ws.person.Model.Dto.Entity.PersonEntity;
import com.ws.person.Model.Dto.PersonDto;
import com.ws.person.Model.Dto.ResponseDto;

import java.util.List;

public interface PersonService {
    public ResponseDto savePerson(PersonDto personDto);
    public List<PersonEntity> getPersons();

}
