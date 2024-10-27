package com.github.bruce_mig.mongodb_csfle.service;

import com.github.bruce_mig.mongodb_csfle.dto.CompanyDTO;

import java.util.List;

public interface CompanyService {
    List<CompanyDTO> findAll();

    CompanyDTO save(CompanyDTO company);
}
