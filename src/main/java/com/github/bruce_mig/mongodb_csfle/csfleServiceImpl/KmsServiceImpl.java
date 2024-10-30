package com.github.bruce_mig.mongodb_csfle.csfleServiceImpl;

import com.github.bruce_mig.mongodb_csfle.csfleService.KmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Key Management System Service.
 * Avoid using a local file to store your Master Key in production.
 * Use a proper KMS provider instead.
 * <a href="https://www.mongodb.com/docs/manual/core/csfle/reference/kms-providers/#supported-key-management-services">MongoDB KMS provider documentation</a>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KmsServiceImpl implements KmsService {

    private final VaultMasterKeyService masterKeyService;
    @Value("${mongodb.kms.provider}")
    private String LOCAL;


    public Map<String, Map<String, Object>> getKmsProviders() {
        log.info("=> Creating local Key Management System using the master key.");
        return new HashMap<>() {{
            put(LOCAL, new HashMap<>() {{
                put("key", masterKeyService.getOrCreateMasterKey()); // MongoDB expects the key name to be "key" when using a local KMS provider
            }});
        }};
    }

}
