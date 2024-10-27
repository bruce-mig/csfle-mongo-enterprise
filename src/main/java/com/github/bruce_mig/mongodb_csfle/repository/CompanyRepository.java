package com.github.bruce_mig.mongodb_csfle.repository;

import com.github.bruce_mig.mongodb_csfle.model.CompanyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the CompanyEntity
 */
@Repository
public interface CompanyRepository extends MongoRepository<CompanyEntity, String> {
}
