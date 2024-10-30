package com.github.bruce_mig.mongodb_csfle.csfleServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.vault.VaultException;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class VaultMasterKeyService {

    private static final int SIZE_MASTER_KEY = 96; // MongoCrypt local key must be 96 bytes
    public static final String CSFLE_MASTER_KEY = "key"; // MongoDB expects the key name to be "key" when using a local KMS provider

    @Value("${mongodb.master.key.vault.path}")
    private String masterKeyPath; // Vault path for the key

    private final VaultTemplate vaultTemplate;

    /**
     * Retrieve or create a master key from HashiCorp Vault.
     */
    public byte[] getOrCreateMasterKey() {
        try {
            log.debug("Attempting to read master key from Vault at path: {}", masterKeyPath);
            VaultResponse response = vaultTemplate.read(masterKeyPath);

            if (response != null && response.getData() != null) {
                // Check if the "csfle-master-key" exists in the response data
                String encodedMasterKey = (String) response.getData().get(CSFLE_MASTER_KEY);
                if (encodedMasterKey != null) {
                    log.debug("Master key found in Vault, decoding...");
                    byte[] decodedKey = Base64.getDecoder().decode(encodedMasterKey);
                    if (decodedKey.length != SIZE_MASTER_KEY) {
                        throw new IllegalArgumentException("Decoded master key must be 96 bytes long.");
                    }
                    return decodedKey;
                } else {
                    log.warn("No master key found in Vault at path: {}", masterKeyPath);
                }
            } else {
                log.warn("Response from Vault is null or does not contain data.");
            }

            // If no key is found, generate and store a new master key in Vault
            byte[] masterKey = generateMasterKey();
            saveMasterKeyToVault(masterKey);
            return masterKey;
        } catch (VaultException e) {
            log.error("Error reading from Vault: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve or create master key", e);
        }
    }

    private byte[] generateMasterKey() {
        byte[] masterKey = new byte[SIZE_MASTER_KEY];
        new SecureRandom().nextBytes(masterKey);
        return masterKey;
    }

    private void saveMasterKeyToVault(byte[] masterKey) {
        String encodedMasterKey = Base64.getEncoder().encodeToString(masterKey);
        Map<String, Object> data = new HashMap<>();
        data.put(CSFLE_MASTER_KEY, encodedMasterKey);
        vaultTemplate.write(masterKeyPath, data);
        log.debug("Master key saved to Vault at path: {}", masterKeyPath);
    }
}

