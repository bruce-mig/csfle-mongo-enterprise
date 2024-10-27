package com.github.bruce_mig.mongodb_csfle.model;

import com.github.bruce_mig.mongodb_csfle.components.EntitySpelEvaluationExtension;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Encrypted;

/**
 * This is the entity class for the "persons" collection.
 * The SpEL expression of the @Encrypted annotation is used to determine the DEK's keyId to use for the encryption.
 *
 * @see EntitySpelEvaluationExtension
 */
@Document("persons")
@Encrypted(keyId = "#{mongocrypt.keyId(#target)}")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonEntity {
    @Id
    private ObjectId id;
    private String firstName;
    private String lastName;
    @Encrypted(algorithm = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic")
    private String ssn;
    @Encrypted(algorithm = "AEAD_AES_256_CBC_HMAC_SHA_512-Random")
    private String bloodType;
}
