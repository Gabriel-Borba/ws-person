package com.ws.person.Factory;

import com.ws.person.Model.Dto.Entity.PersonEntity;
import com.ws.person.Model.Dto.PersonDto;

public class PersonFactory {
    public static PersonEntity buildPersonEntity(PersonDto personDto) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setName(personDto.getName());
        personEntity.setDocumentNumber(personDto.getDocumentNumber());
        personEntity.setScore(personDto.getScore());
        personEntity.setType(personDto.getType());
        return personEntity;
    }

    public static PersonDto buildEntityToPersonDto(PersonEntity personEntity) {
        return PersonDto.builder().documentNumber(personEntity.getDocumentNumber())
                .name(personEntity.getName())
                .score(personEntity.getScore())
                .type(personEntity.getType())
                .id(personEntity.getIdPerson()).build();
    }
}
