package com.github.bruce_mig.mongodb_csfle.components;

import com.github.bruce_mig.mongodb_csfle.csfleService.DataEncryptionKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.data.spel.spi.Function;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * Will evaluate the SpEL expressions in the Entity classes like this: #{mongocrypt.keyId(#target)} and insert
 * the right encryption key for the right collection.
 */
@Component
@Slf4j
public class EntitySpelEvaluationExtension implements EvaluationContextExtension {

    private final DataEncryptionKeyService dataEncryptionKeyService;

    public EntitySpelEvaluationExtension(DataEncryptionKeyService dataEncryptionKeyService) {
        this.dataEncryptionKeyService = dataEncryptionKeyService;
    }

    @Override
    @NonNull
    public String getExtensionId() {
        return "mongocrypt";
    }

    @Override
    @NonNull
    public Map<String, Function> getFunctions() {
        try {
            return Collections.singletonMap("keyId", new Function(
                    EntitySpelEvaluationExtension.class.getMethod("computeKeyId", String.class), this));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public String computeKeyId(String target) {
        String dek = dataEncryptionKeyService.getDataEncryptionKeysB64().get(target);
        log.info("=> Computing dek for target {} => {}", target, dek);
        return dek;
    }
}
