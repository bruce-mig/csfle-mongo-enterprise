package com.github.bruce_mig.mongodb_csfle.model;

import com.github.bruce_mig.mongodb_csfle.components.EntitySpelEvaluationExtension;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Encrypted;

/**
 * This is the entity class for the "companies" collection.
 * The SpEL expression of the @Encrypted annotation is used to determine the DEK's keyId to use for the encryption.
 *
 * @see EntitySpelEvaluationExtension
 */
@Getter
@Setter
@ToString
@Document("companies")
@AllArgsConstructor
@NoArgsConstructor
@Encrypted(keyId = "#{mongocrypt.keyId(#target)}")
public class CompanyEntity {
    @Id
    private ObjectId id;
    private String name;
    @Encrypted(algorithm = "AEAD_AES_256_CBC_HMAC_SHA_512-Random")
    private Long money;
}
