package com.github.bruce_mig.mongodb_csfle.components;

import com.github.bruce_mig.mongodb_csfle.configuration.EncryptedCollectionsConfiguration;
import com.github.bruce_mig.mongodb_csfle.csfleService.DataEncryptionKeyService;
import com.github.bruce_mig.mongodb_csfle.csfleService.KeyVaultService;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class initialize the Key Vault (collection + keyAltNames unique index) using a dedicated standard connection
 * to MongoDB.
 * Then it creates the Data Encryption Keys (DEKs) required to encrypt the documents in each of the
 * encrypted collections.
 */
@Component
@Slf4j
public class KeyVaultAndDekSetup {

    private final KeyVaultService keyVaultService;
    private final DataEncryptionKeyService dataEncryptionKeyService;
    @Value("${spring.data.mongodb.vault.uri}")
    private String CONNECTION_STR;

    public KeyVaultAndDekSetup(KeyVaultService keyVaultService, DataEncryptionKeyService dataEncryptionKeyService) {
        this.keyVaultService = keyVaultService;
        this.dataEncryptionKeyService = dataEncryptionKeyService;
    }

    @PostConstruct
    public void postConstruct() {
        log.info("=> Start Encryption Setup.");
        log.debug("=> MongoDB Connection String: {}", CONNECTION_STR);
        MongoClientSettings mcs = MongoClientSettings.builder()
                                                     .applyConnectionString(new ConnectionString(CONNECTION_STR))
                                                     .build();
        try (MongoClient client = MongoClients.create(mcs)) {
            log.info("=> Created the MongoClient instance for the encryption setup.");
            log.info("=> Creating the encryption key vault collection.");
            keyVaultService.setupKeyVaultCollection(client);
            log.info("=> Creating the Data Encryption Keys.");
            EncryptedCollectionsConfiguration.encryptedEntities.forEach(dataEncryptionKeyService::createOrRetrieveDEK);
            log.info("=> Encryption Setup completed.");
        } catch (Exception e) {
            log.error("=> Encryption Setup failed: {}", e.getMessage(), e);
        }

    }

}
