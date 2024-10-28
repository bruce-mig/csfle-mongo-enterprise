package com.github.bruce_mig.mongodb_csfle.csfleServiceImpl;

import com.github.bruce_mig.mongodb_csfle.configuration.EncryptedEntity;
import com.github.bruce_mig.mongodb_csfle.csfleService.DataEncryptionKeyService;
import com.mongodb.client.model.vault.DataKeyOptions;
import com.mongodb.client.vault.ClientEncryption;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.util.List.of;

/**
 * Service responsible for creating and remembering the Data Encryption Keys (DEKs).
 * We need to retrieve the DEKs when we evaluate the SpEL expressions in the Entities to create the JSON Schemas.
 */
@Service
@Slf4j
public class DataEncryptionKeyServiceImpl implements DataEncryptionKeyService {

    private final ClientEncryption clientEncryption;
    private final Map<String, String> dataEncryptionKeysB64 = new HashMap<>();
    @Value("${mongodb.kms.provider}")
    private String KMS_PROVIDER;

    public DataEncryptionKeyServiceImpl(ClientEncryption clientEncryption) {
        this.clientEncryption = clientEncryption;
    }

    public Map<String, String> getDataEncryptionKeysB64() {
        log.info("=> Getting Data Encryption Keys Base64 Map.");
        log.info("=> Keys in DEK Map: {}", dataEncryptionKeysB64.entrySet());
        return dataEncryptionKeysB64;
    }

    public String createOrRetrieveDEK(EncryptedEntity encryptedEntity) {
        Base64.Encoder b64Encoder = Base64.getEncoder();
        String dekName = encryptedEntity.getDekName();
        BsonDocument dek = clientEncryption.getKeyByAltName(dekName);
        BsonBinary dataKeyId;
        if (dek == null) {
            log.info("=> Creating Data Encryption Key: {}", dekName);
            DataKeyOptions dko = new DataKeyOptions().keyAltNames(of(dekName));
            dataKeyId = clientEncryption.createDataKey(KMS_PROVIDER, dko);
            log.debug("=> DEK ID: {}", dataKeyId);
        } else {
            log.info("=> Existing Data Encryption Key: {}", dekName);
            dataKeyId = dek.get("_id").asBinary();
            log.debug("=> DEK ID: {}", dataKeyId);
        }
        String dek64 = b64Encoder.encodeToString(dataKeyId.getData());
        log.debug("=> Base64 DEK ID: {}", dek64);
        log.info("=> Adding Data Encryption Key to the Map with key: {}",
                    encryptedEntity.getEntityClass().getSimpleName());
        dataEncryptionKeysB64.put(encryptedEntity.getEntityClass().getSimpleName(), dek64);
        return dek64;
    }

}
