package com.github.bruce_mig.mongodb_csfle.serviceImpl;

import com.github.bruce_mig.mongodb_csfle.dto.PersonDTO;
import com.github.bruce_mig.mongodb_csfle.exception.EntityNotFoundException;
import com.github.bruce_mig.mongodb_csfle.model.PersonEntity;
import com.github.bruce_mig.mongodb_csfle.repository.PersonRepository;
import com.github.bruce_mig.mongodb_csfle.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Person Service.
 * Contains the business logic layer between the Controller and Repositories (database).
 */
@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<PersonDTO> findAll() {
        log.info("=> Find all persons.");
        List<PersonDTO> result = personRepository.findAll().stream().map(PersonDTO::new).toList();
        if (result.isEmpty()) {
            throw new EntityNotFoundException("PersonServiceImpl#findAll");
        }
        return result;
    }

    @Override
    public PersonDTO save(PersonDTO personDTO) {
        PersonEntity person = personDTO.toPersonEntity();
        log.info("=> Saving person: {}", person);
        PersonEntity personSaved = personRepository.save(person);
        return new PersonDTO(personSaved);
    }

    @Override
    public PersonDTO findBySsn(String ssn) {
        log.info("=> Find by SSN {}.", ssn);
        PersonEntity personEntity = personRepository.findFirstBySsn(ssn);
        if (personEntity == null) {
            throw new EntityNotFoundException("PersonServiceImpl#findBySsn", ssn);
        }
        return new PersonDTO(personEntity);
    }
}
