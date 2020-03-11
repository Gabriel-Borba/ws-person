package com.ws.person.service.impl;

import com.ws.person.Model.Dto.Entity.PersonEntity;
import com.ws.person.Model.Dto.PersonDto;
import com.ws.person.Model.Dto.ResponseDto;
import com.ws.person.repository.PersonRepository;
import com.ws.person.service.PersonService;
import com.ws.person.service.RabbitMqService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.ws.person.Factory.PersonFactory.buildEntityToPersonDto;
import static com.ws.person.Factory.PersonFactory.buildPersonEntity;
import static org.slf4j.LoggerFactory.getLogger;

@Component
@Service
public class PersonServiceImpl implements PersonService {
    private static Logger logger = getLogger(PersonServiceImpl.class);
    private PersonRepository personRepository;
    private RabbitMqService rabbitMqService;

    @Value("${document.size.pf}")
    private int maxSizeDocumentPF;

    @Value("${document.size.pj}")
    private int maxSizeDocumentPJ;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, RabbitMqService rabbitMqService) {
        this.rabbitMqService = rabbitMqService;
        this.personRepository = personRepository;
    }

    @Override
    public ResponseDto savePerson(PersonDto personDto) {
        logger.info("Saving entity into database ");
        try {
            personDto.setScore(ThreadLocalRandom.current().nextInt(0, 9 + 1));
            validatePerson(personDto);
            PersonEntity personEntity = personRepository.save(buildPersonEntity(personDto));
            logger.info("Person with id saved successfully: " + personEntity.getIdPerson());
            rabbitMqService.sendAccountQueue(buildEntityToPersonDto(personEntity));
        } catch (DataAccessException data) {
            logger.error("Error saving entity into database");
            logger.error(data.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return ResponseDto.builder().message("Account created successfully").build();
    }

    @Override
    public List<PersonEntity> getPersons() {
        List<PersonEntity> persons;
        try {
            persons = personRepository.findAll();
        } catch (DataAccessException data) {
            logger.error("error for getting a list persons");
            logger.error(data.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return persons;
    }


    private void validatePerson(PersonDto personDto) {
        logger.info("Validating person");
        if (personDto.getDocumentNumber().length() != maxSizeDocumentPF && personDto.getDocumentNumber().length() != maxSizeDocumentPJ) {
            logger.error("Document number is not valid" + personDto.getDocumentNumber());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
