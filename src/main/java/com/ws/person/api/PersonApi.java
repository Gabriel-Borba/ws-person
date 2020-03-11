package com.ws.person.api;

import com.ws.person.Model.Dto.PersonDto;
import com.ws.person.Model.Dto.ResponseDto;
import com.ws.person.service.PersonService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/person")
public class PersonApi {
    private static Logger logger = getLogger(PersonApi.class);
    private PersonService personService;

    @Autowired
    public PersonApi(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<?> createPerson(@RequestBody PersonDto personDto) {
        logger.info("Saving Movie entity");
        try {
            return ResponseEntity.ok(personService.savePerson(personDto));
        } catch (ResponseStatusException response) {
            logger.error("Error saving entity " + response.getMessage());
            logger.error(response.getReason());
            return ResponseEntity.status(response.getStatus()).body(ResponseDto.builder().message(response.getMessage()).build());
        }
    }

    @GetMapping(path = "/findAll")
    public ResponseEntity<?> createPerson() {
        logger.info("Listing Persons");
        try {
            return ResponseEntity.ok(personService.getPersons());
        } catch (ResponseStatusException response) {
            logger.error("Error listing persons " + response.getMessage());
            logger.error(response.getReason());
            return ResponseEntity.status(response.getStatus()).body(ResponseDto.builder().message(response.getMessage()).build());
        }
    }

}
