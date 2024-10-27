package com.github.bruce_mig.mongodb_csfle.controller;

import com.github.bruce_mig.mongodb_csfle.csfleServiceImpl.MasterKeyServiceImpl;
import com.github.bruce_mig.mongodb_csfle.dto.CompanyDTO;
import com.github.bruce_mig.mongodb_csfle.exception.EntityNotFoundException;
import com.github.bruce_mig.mongodb_csfle.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for the companies.
 */
@Slf4j
@RestController
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public List<CompanyDTO> getAllPersons() {
        return companyService.findAll();
    }

    @PostMapping("/company")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDTO createPerson(@RequestBody CompanyDTO companyDTO) {
        return companyService.save(companyDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "MongoDB didn't find any document.")
    public final void handleNotFoundExceptions(EntityNotFoundException e) {
        log.info("=> Company not found: {}", e.toString());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Server Error")
    public final void handleAllExceptions(RuntimeException e) {
        log.error("=> Internal server error.", e);
    }
}
