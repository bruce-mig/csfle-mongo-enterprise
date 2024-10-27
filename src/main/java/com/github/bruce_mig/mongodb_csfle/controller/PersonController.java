package com.github.bruce_mig.mongodb_csfle.controller;

import com.github.bruce_mig.mongodb_csfle.dto.PersonDTO;
import com.github.bruce_mig.mongodb_csfle.exception.EntityNotFoundException;
import com.github.bruce_mig.mongodb_csfle.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for the persons.
 */
@Slf4j
@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/persons")
    public List<PersonDTO> getAllPersons() {
        return personService.findAll();
    }

    @PostMapping("/person")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDTO createPerson(@RequestBody PersonDTO personDTO) {
        return personService.save(personDTO);
    }

    @GetMapping("/person/ssn/{ssn}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO findBySsn(@PathVariable String ssn) {
        return personService.findBySsn(ssn);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "MongoDB didn't find any document.")
    public final void handleNotFoundExceptions(EntityNotFoundException e) {
        log.info("=> Person not found: {}", e.toString());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Server Error")
    public final void handleAllExceptions(RuntimeException e) {
        log.error("=> Internal server error.", e);
    }
}
