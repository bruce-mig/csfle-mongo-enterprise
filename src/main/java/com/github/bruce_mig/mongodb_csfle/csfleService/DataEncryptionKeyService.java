package com.github.bruce_mig.mongodb_csfle.csfleService;

import com.github.bruce_mig.mongodb_csfle.configuration.EncryptedEntity;

import java.util.Map;

public interface DataEncryptionKeyService {
    String createOrRetrieveDEK(EncryptedEntity encryptedEntity);

    Map<String, String> getDataEncryptionKeysB64();
}
