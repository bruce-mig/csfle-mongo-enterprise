package com.github.bruce_mig.mongodb_csfle.service;

import com.github.bruce_mig.mongodb_csfle.dto.PersonDTO;

import java.util.List;

public interface PersonService {
    List<PersonDTO> findAll();

    PersonDTO save(PersonDTO person);

    PersonDTO findBySsn(String ssn);
}
