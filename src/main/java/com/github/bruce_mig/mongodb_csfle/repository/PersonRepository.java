package com.github.bruce_mig.mongodb_csfle.repository;

import com.github.bruce_mig.mongodb_csfle.model.PersonEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PersonEntity
 */
@Repository
public interface PersonRepository extends MongoRepository<PersonEntity, String> {

    PersonEntity findFirstBySsn(String ssn);
}
