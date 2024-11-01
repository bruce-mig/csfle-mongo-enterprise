package com.github.bruce_mig.mongodb_csfle.csfleServiceImpl;

import com.github.bruce_mig.mongodb_csfle.csfleService.MasterKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * Master Key service.
 * Responsible for the retrieval of an existing Master Key in the local file system if it exists.
 * Responsible for generating and saving a new Master Key to a local file if necessary.
 * It's not recommended to store a Master Key in the local file system in production.
 * <a href="https://www.mongodb.com/docs/manual/core/csfle/reference/kms-providers/#supported-key-management-services">MongoDB KMS provider documentation</a>
 */
@Service
@Slf4j
public class MasterKeyServiceImpl implements MasterKeyService {

    private static final int SIZE_MASTER_KEY = 96;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Value("${mongodb.master.key.file.path}")
    private String masterKeyFilename;

    public byte[] generateNewOrRetrieveMasterKeyFromFile() {
        byte[] masterKey = new byte[SIZE_MASTER_KEY];
        try {
            retrieveMasterKeyFromFile(masterKeyFilename, masterKey);
            log.info("=> An existing Master Key was found in file {}.", masterKeyFilename);
        } catch (IOException e) {
            masterKey = generateMasterKey();
            saveMasterKeyToFile(masterKeyFilename, masterKey);
            log.info("=> A new Master Key has been generated and saved to file {}.", masterKeyFilename);
        }
        return masterKey;
    }

    private void retrieveMasterKeyFromFile(String filename, byte[] masterKey) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            fis.read(masterKey, 0, SIZE_MASTER_KEY);
        }
    }

    private byte[] generateMasterKey() {
        byte[] masterKey = new byte[SIZE_MASTER_KEY];
        SECURE_RANDOM.nextBytes(masterKey);
        return masterKey;
    }

    private void saveMasterKeyToFile(String filename, byte[] masterKey) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(masterKey);
        } catch (IOException e) {
            log.error("=> Couldn't save the Master Key to file {}.", filename);
            log.error("=> Error message: {}", e.getMessage(), e);
        }
    }

}
