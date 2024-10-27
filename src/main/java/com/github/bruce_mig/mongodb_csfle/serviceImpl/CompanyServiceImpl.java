package com.github.bruce_mig.mongodb_csfle.serviceImpl;

import com.github.bruce_mig.mongodb_csfle.dto.CompanyDTO;
import com.github.bruce_mig.mongodb_csfle.exception.EntityNotFoundException;
import com.github.bruce_mig.mongodb_csfle.model.CompanyEntity;
import com.github.bruce_mig.mongodb_csfle.repository.CompanyRepository;
import com.github.bruce_mig.mongodb_csfle.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Company Service.
 * Contains the business logic layer between the Controller and Repositories (database).
 */
@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<CompanyDTO> findAll() {
        log.info("=> Find all companies.");
        List<CompanyDTO> results = companyRepository.findAll().stream().map(CompanyDTO::new).toList();
        if (results.isEmpty()) {
            throw new EntityNotFoundException("CompanyServiceImpl#findAll");
        }
        return results;
    }

    @Override
    public CompanyDTO save(CompanyDTO companyDTO) {
        CompanyEntity company = companyDTO.toCompanyEntity();
        log.info("=> Saving company: {}", company);
        CompanyEntity companySaved = companyRepository.save(company);
        return new CompanyDTO(companySaved);
    }
}
